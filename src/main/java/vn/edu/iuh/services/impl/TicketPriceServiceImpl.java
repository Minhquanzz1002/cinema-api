package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceRequestDTO;
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
    public TicketPrice createTicketPrice(CreateTicketPriceRequestDTO createTicketPriceRequestDTO) {
        TicketPrice ticketPrice = modelMapper.map(createTicketPriceRequestDTO, TicketPrice.class);
        return ticketPriceRepository.save(ticketPrice);
    }

    @Override
    public TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO) {
        TicketPrice ticketPrice = findById(id);
        LocalDate currentDate = LocalDate.now();

        // Handle status transition from INACTIVE to ACTIVE
        if (ticketPrice.getStatus() == BaseStatus.INACTIVE && updateTicketPriceRequestDTO.getStatus() == BaseStatus.ACTIVE) {
            validateAndActiveTicketPriceLines(ticketPrice);
        }

        // Handle updates for ACTIVE ticket prices
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            if (updateTicketPriceRequestDTO.getEndDate().isBefore(currentDate)) {
                throw new BadRequestException(
                        "Chỉ được phép sửa ngày kết thúc thành ngày hiện tại cho giá vé đang hoạt động");
            }

            if (updateTicketPriceRequestDTO.getEndDate().isAfter(ticketPrice.getEndDate())) {
                List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
                        ticketPrice.getEndDate().plusDays(1),
                        updateTicketPriceRequestDTO.getEndDate()
                );


                if (!overlappingTicketPrices.isEmpty()) {
                    throw new BadRequestException(
                            "Không thể kéo dài giá vé vì xung đột với giá vé khác trong khoảng thời gian này");
                }
            }

            ticketPrice.setEndDate(updateTicketPriceRequestDTO.getEndDate());
        } else {
            if (updateTicketPriceRequestDTO.getStatus() == BaseStatus.INACTIVE) {
                modelMapper.map(updateTicketPriceRequestDTO, ticketPrice);
            } else {
                validateOverlappingTicketsWithTimeDetails(
                        updateTicketPriceRequestDTO.getStartDate(),
                        updateTicketPriceRequestDTO.getEndDate(),
                        ticketPrice
                );

                modelMapper.map(updateTicketPriceRequestDTO, ticketPrice);
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
                        String.format("Đã tồn tại giá vé có cùng thời gian hoạt động trong khoảng từ %s đến %s",
                                      startDate, endDate)
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
            CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO
    ) {
        TicketPrice ticketPrice = getTicketPriceById(ticketPriceId);
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể thêm vào giá vé đang hoạt động");
        }

        String[] applyForDays = createTicketPriceLineRequestDTO.getApplyForDays()
                                                               .stream()
                                                               .map(Enum::name)
                                                               .toArray(String[]::new);

        boolean hasOverlap = ticketPriceLineRepository.hasOverlappingTicketPriceLine(
                ticketPrice.getId(),
                applyForDays,
                createTicketPriceLineRequestDTO.getStartTime(),
                createTicketPriceLineRequestDTO.getEndTime()
        );

        if (hasOverlap) {
            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
        }

        TicketPriceLine ticketPriceLine = modelMapper.map(createTicketPriceLineRequestDTO, TicketPriceLine.class);
        ticketPriceLine.setTicketPrice(ticketPrice);

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(createTicketPriceLineRequestDTO.getNormalPrice())
                                                              .seatType(SeatType.NORMAL)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(createTicketPriceLineRequestDTO.getVipPrice())
                                                              .seatType(SeatType.VIP)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                                                              .price(createTicketPriceLineRequestDTO.getCouplePrice())
                                                              .seatType(SeatType.COUPLE)
                                                              .status(BaseStatus.INACTIVE)
                                                              .build());

        return ticketPriceLineRepository.save(ticketPriceLine);
    }

    @Override
    public TicketPriceLine updateTicketPriceLine(
            int ticketPriceId,
            int lineId,
            UpdateTicketPriceLineRequestDTO updateTicketPriceLineRequestDTO
    ) {
        TicketPrice ticketPrice = getTicketPriceById(ticketPriceId);
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể sửa giá vé đang hoạt động");
        }

        // Check if there is an overlapping ticket price line
        String[] applyForDays = updateTicketPriceLineRequestDTO.getApplyForDays()
                                                               .stream()
                                                               .map(Enum::name)
                                                               .toArray(String[]::new);
        boolean hasOverlap = ticketPriceLineRepository.hasOverlappingTicketPriceLine(
                ticketPrice.getId(),
                applyForDays,
                updateTicketPriceLineRequestDTO.getStartTime(),
                updateTicketPriceLineRequestDTO.getEndTime(),
                lineId
        );
        if (hasOverlap) {
            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
        }

        TicketPriceLine ticketPriceLine = ticketPriceLineRepository.findByIdAndDeleted(lineId, false)
                                                                   .orElseThrow(() -> new DataNotFoundException(
                                                                           "Không tìm thấy dòng giá vé"));
        ticketPriceLine.setApplyForDays(updateTicketPriceLineRequestDTO.getApplyForDays());
        ticketPriceLine.setStartTime(updateTicketPriceLineRequestDTO.getStartTime());
        ticketPriceLine.setEndTime(updateTicketPriceLineRequestDTO.getEndTime());
        ticketPriceLine.setStatus(updateTicketPriceLineRequestDTO.getStatus());
        ticketPriceLine.getTicketPriceDetails().forEach(ticketPriceDetail -> {
            switch (ticketPriceDetail.getSeatType()) {
                case NORMAL:
                    ticketPriceDetail.setPrice(updateTicketPriceLineRequestDTO.getNormalPrice());
                    break;
                case VIP:
                    ticketPriceDetail.setPrice(updateTicketPriceLineRequestDTO.getVipPrice());
                    break;
                case COUPLE:
                    ticketPriceDetail.setPrice(updateTicketPriceLineRequestDTO.getCouplePrice());
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
                        isTimeOverlapping(time1.startTime(), time1.endTime(),
                                          time2.startTime(), time2.endTime())) {
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
