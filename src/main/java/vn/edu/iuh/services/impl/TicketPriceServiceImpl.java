package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.CreateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.UpdateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CopyTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CreateTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.UpdateTicketPriceRequest;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceDetail;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.DayType;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.repositories.TicketPriceDetailRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.repositories.TicketPriceRepository;
import vn.edu.iuh.services.TicketPriceService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.TicketPriceSpecification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketPriceServiceImpl implements TicketPriceService {
    private final TicketPriceRepository ticketPriceRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final TicketPriceDetailRepository ticketPriceDetailRepository;
    private final ModelMapper modelMapper;


    @Override
    public TicketPrice findById(int id) {
        return ticketPriceRepository.findByIdAndDeleted(id, false)
                                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
    }

    @Override
    public TicketPrice getByDate(LocalDate date) {
        return ticketPriceRepository.findByStatusAndDate(date, BaseStatus.ACTIVE)
                                    .orElseThrow(() -> new DataNotFoundException("Giá vé chưa được xử lý"));
    }

    @Override
    public Page<TicketPrice> getAllTicketPrices(
            String name,
            BaseStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Specification<TicketPrice> specification = Specification.where(TicketPriceSpecification.withName(name));
        specification = specification.and(GenericSpecifications.withStatus(status))
                                     .and(TicketPriceSpecification.betweenDates(startDate, endDate));
        Page<TicketPrice> ticketPrices = ticketPriceRepository.findAll(specification, pageable);

        ticketPrices.getContent().forEach(ticketPrice -> {
            if (ticketPrice.getTicketPriceLines() != null) {
                List<TicketPriceLine> activeLines = ticketPrice.getTicketPriceLines().stream()
                                                               .filter(line -> !line.isDeleted())
                                                               .collect(Collectors.toList());
                ticketPrice.setTicketPriceLines(activeLines);
            }
        });
        return ticketPrices;
    }

    @Override
    public TicketPrice createTicketPrice(CreateTicketPriceRequest request) {
        TicketPrice ticketPrice = modelMapper.map(request, TicketPrice.class);
        return ticketPriceRepository.save(ticketPrice);
    }

    @Override
    public TicketPrice copyTicketPrice(int id, CopyTicketPriceRequest request) {
        TicketPrice original = findById(id);

        TicketPrice copy = new TicketPrice();
        copy.setName(original.getName() + " - Sao chép");
        copy.setStatus(BaseStatus.INACTIVE);
        copy.setStartDate(request.getStartDate());
        copy.setEndDate(request.getEndDate());

        TicketPrice saved = ticketPriceRepository.save(copy);
        original.getTicketPriceLines().stream()
                .filter(line -> !line.isDeleted())
                .forEach(line -> {
                    TicketPriceLine copyLine = new TicketPriceLine();
                    copyLine.setApplyForDays(line.getApplyForDays());
                    copyLine.setStartTime(line.getStartTime());
                    copyLine.setEndTime(line.getEndTime());
                    copyLine.setAudienceType(line.getAudienceType());
                    copyLine.setTicketPrice(saved);
                    copyLine.setStatus(BaseStatus.INACTIVE);
                    TicketPriceLine savedLine = ticketPriceLineRepository.save(copyLine);

                    line.getTicketPriceDetails().stream()
                        .filter(detail -> !detail.isDeleted())
                        .forEach(detail -> {
                            TicketPriceDetail copyDetail = new TicketPriceDetail();
                            copyDetail.setPrice(detail.getPrice());
                            copyDetail.setSeatType(detail.getSeatType());
                            copyDetail.setTicketPriceLine(savedLine);
                            copyDetail.setStatus(BaseStatus.INACTIVE);
                            ticketPriceDetailRepository.save(copyDetail);
                        });
                });
        return saved;
    }

    @Transactional
    @Override
    public TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequest request) {
        TicketPrice ticketPrice = findById(id);
        LocalDate currentDate = LocalDate.now();

        // Handle status transition from INACTIVE to ACTIVE
        if (ticketPrice.getStatus() == BaseStatus.INACTIVE && request.getStatus() == BaseStatus.ACTIVE) {
            validateAndActiveTicketPriceLines(ticketPrice);
        }

        // Handle updates for ACTIVE ticket prices
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            if (request.getEndDate().isBefore(currentDate)) {
                throw new BadRequestException(
                        "Chỉ được phép sửa ngày kết thúc thành ngày hiện tại cho giá vé đang hoạt động");
            }

            if (request.getEndDate().isAfter(ticketPrice.getEndDate())) {
                List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
                        ticketPrice.getEndDate().plusDays(1),
                        request.getEndDate()
                );


                if (!overlappingTicketPrices.isEmpty()) {
                    throw new BadRequestException(
                            "Không thể kéo dài giá vé vì xung đột với giá vé khác trong khoảng thời gian này");
                }
            }

            ticketPrice.setEndDate(request.getEndDate());
        } else {
            if (request.getStatus() == BaseStatus.INACTIVE) {
                modelMapper.map(request, ticketPrice);
            } else {
                validateOverlappingTicketsWithTimeDetails(
                        request.getStartDate(),
                        request.getEndDate(),
                        ticketPrice
                );

                modelMapper.map(request, ticketPrice);
            }

        }
        return ticketPriceRepository.save(ticketPrice);
    }

    private void validateAndActiveTicketPriceLines(TicketPrice ticketPrice) {
        if (ticketPrice.getTicketPriceLines() == null || ticketPrice.getTicketPriceLines().isEmpty()) {
            throw new BadRequestException("Không thể kích hoạt giá vé khi chưa có dữ liệu giá");
        }

        ticketPrice.getTicketPriceLines().stream()
                   .filter(line -> !line.isDeleted())
                   .forEach(line -> {
                       line.setStatus(BaseStatus.ACTIVE);
                       Optional.ofNullable(line.getTicketPriceDetails())
                               .ifPresent(details -> details.stream()
                                                            .filter(detail -> !detail.isDeleted())
                                                            .forEach(detail -> detail.setStatus(BaseStatus.ACTIVE)));
                   });
    }

    private void validateOverlappingTicketsWithTimeDetails(
            LocalDate startDate,
            LocalDate endDate,
            TicketPrice currentTicket
    ) {
        List<TicketPrice> overlappingTicketPrices = ticketPriceRepository
                .findOverlappingTicketPrices(startDate, endDate)
                .stream()
                .filter(ticketPrice -> ticketPrice.getId() != currentTicket.getId())
                .toList();

        if (overlappingTicketPrices.isEmpty()) {
            return;
        }

        Set<TimeDetail> currentTimeDetails = extractTimeDetails(currentTicket);

        for (TicketPrice overlappingTicket : overlappingTicketPrices) {
            Set<TimeDetail> overlappingTimeDetails = extractTimeDetails(overlappingTicket);

            if (hasTimeConflict(currentTimeDetails, overlappingTimeDetails)) {
                throw new BadRequestException(
                        String.format(
                                "Đã tồn tại giá vé có cùng thời gian hoạt động trong khoảng từ %s đến %s",
                                startDate, endDate
                        )
                );
            }
        }

    }

    private boolean hasOverlappingDays(List<DayType> days1, List<DayType> days2) {
        return days1.stream().anyMatch(days2::contains);
    }

    private Set<TimeDetail> extractTimeDetails(TicketPrice ticketPrice) {
        return ticketPrice.getTicketPriceLines().stream()
                          .filter(line -> !line.isDeleted())
                          .filter(line -> line.getStatus() == BaseStatus.ACTIVE)
                          .map(line -> new TimeDetail(
                                  line.getApplyForDays(),
                                  line.getStartTime(),
                                  line.getEndTime()
                          ))
                          .collect(Collectors.toSet());
    }

    @Override
    public void deleteTicketPrice(int id) {
        TicketPrice ticketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
                "Không tìm thấy giá vé"));
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa giá vé đang hoạt động");
        }
        ticketPriceRepository.delete(ticketPrice);
    }

    @Override
    public TicketPrice getTicketPriceById(int id) {
        return ticketPriceRepository.findByIdAndDeleted(id, false)
                                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
    }

    @Transactional
    @Override
    public TicketPriceLine createTicketPriceLine(
            int ticketPriceId,
            CreateTicketPriceLineRequest request
    ) {
        TicketPrice ticketPrice = getTicketPriceById(ticketPriceId);
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể thêm vào giá vé đang hoạt động");
        }

        String[] applyForDays = request.getApplyForDays()
                                       .stream()
                                       .map(Enum::name)
                                       .toArray(String[]::new);

        boolean hasOverlap = ticketPriceLineRepository.hasOverlappingTicketPriceLine(
                ticketPrice.getId(),
                applyForDays,
                request.getStartTime(),
                request.getEndTime()
        );

        if (hasOverlap) {
            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
        }

        TicketPriceLine ticketPriceLine = modelMapper.map(request, TicketPriceLine.class);
        ticketPriceLine.setTicketPrice(ticketPrice);

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(request.getNormalPrice())
                                                              .seatType(SeatType.NORMAL)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(request.getVipPrice())
                                                              .seatType(SeatType.VIP)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(request.getCouplePrice())
                                                              .seatType(SeatType.COUPLE)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        return ticketPriceLineRepository.save(ticketPriceLine);
    }

    @Override
    public TicketPriceLine updateTicketPriceLine(
            int ticketPriceId,
            int lineId,
            UpdateTicketPriceLineRequest request
    ) {
        TicketPrice ticketPrice = getTicketPriceById(ticketPriceId);
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể sửa giá vé đang hoạt động");
        }

        // Check if there is an overlapping ticket price line
        String[] applyForDays = request.getApplyForDays()
                                       .stream()
                                       .map(Enum::name)
                                       .toArray(String[]::new);
        boolean hasOverlap = ticketPriceLineRepository.hasOverlappingTicketPriceLine(
                ticketPrice.getId(),
                applyForDays,
                request.getStartTime(),
                request.getEndTime(),
                lineId
        );
        if (hasOverlap) {
            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
        }

        TicketPriceLine ticketPriceLine = ticketPriceLineRepository.findByIdAndDeleted(lineId, false)
                                                                   .orElseThrow(() -> new DataNotFoundException(
                                                                           "Không tìm thấy dòng giá vé"));
        ticketPriceLine.setApplyForDays(request.getApplyForDays());
        ticketPriceLine.setStartTime(request.getStartTime());
        ticketPriceLine.setEndTime(request.getEndTime());
        ticketPriceLine.setStatus(request.getStatus());
        ticketPriceLine.getTicketPriceDetails().forEach(ticketPriceDetail -> {
            switch (ticketPriceDetail.getSeatType()) {
                case NORMAL:
                    ticketPriceDetail.setPrice(request.getNormalPrice());
                    break;
                case VIP:
                    ticketPriceDetail.setPrice(request.getVipPrice());
                    break;
                case COUPLE:
                    ticketPriceDetail.setPrice(request.getCouplePrice());
                    break;
            }
        });
        return ticketPriceLineRepository.save(ticketPriceLine);
    }

    private record TimeDetail(List<DayType> dayTypes, LocalTime startTime, LocalTime endTime) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeDetail that = (TimeDetail) o;
            return Objects.equals(dayTypes, that.dayTypes) &&
                    Objects.equals(startTime, that.startTime) &&
                    Objects.equals(endTime, that.endTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dayTypes, startTime, endTime);
        }
    }

    private boolean hasTimeConflict(Set<TimeDetail> details1, Set<TimeDetail> details2) {
        for (TimeDetail time1 : details1) {
            for (TimeDetail time2 : details2) {
                // Kiểm tra xem có ngày nào trùng nhau không
                if (hasOverlappingDays(time1.dayTypes(), time2.dayTypes()) &&
                        isTimeOverlapping(
                                time1.startTime(), time1.endTime(),
                                time2.startTime(), time2.endTime()
                        )) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }
}
