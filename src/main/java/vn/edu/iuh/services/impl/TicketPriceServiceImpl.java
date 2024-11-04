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
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.repositories.TicketPriceDetailRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.repositories.TicketPriceRepository;
import vn.edu.iuh.services.TicketPriceService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.TicketPriceSpecification;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketPriceServiceImpl implements TicketPriceService {
    private final TicketPriceRepository ticketPriceRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final TicketPriceDetailRepository ticketPriceDetailRepository;
    private final ModelMapper modelMapper;


    @Override
    public TicketPrice getByDate(LocalDate date) {
        return ticketPriceRepository.findByStatusAndDate(date, BaseStatus.ACTIVE).orElseThrow(() -> new DataNotFoundException("Giá vé chưa được xử lý"));
    }

    @Override
    public Page<TicketPrice> getAllTicketPrices(String name, BaseStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
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
//        List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
//                createTicketPriceRequestDTO.getStartDate(), // kiem tra
//                createTicketPriceRequestDTO.getEndDate()
//        );
//
//        if (!overlappingTicketPrices.isEmpty()) {
//            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
//        }

        TicketPrice ticketPrice = modelMapper.map(createTicketPriceRequestDTO, TicketPrice.class);
        return ticketPriceRepository.save(ticketPrice);
    }

    @Override
    public TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO) {
        TicketPrice ticketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
        LocalDate currentDate = LocalDate.now();

        // Kiểm tra nếu đang từ INACTIVE chuyển sang ACTIVE
        if (ticketPrice.getStatus() == BaseStatus.INACTIVE && updateTicketPriceRequestDTO.getStatus() == BaseStatus.ACTIVE) {
            // Kiểm tra xem có dữ liệu (price) không
            if (ticketPrice.getTicketPriceLines() == null || ticketPrice.getTicketPriceLines().isEmpty()) {
                throw new BadRequestException("Không thể kích hoạt giá vé khi chưa có dữ liệu giá");
            }
        }

        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            if (updateTicketPriceRequestDTO.getEndDate().isBefore(currentDate)) {
                throw new BadRequestException("Chỉ được phép sửa ngày kết thúc thành ngày hiện tại cho giá vé đang hoạt động");
            }

            if (updateTicketPriceRequestDTO.getEndDate().isAfter(ticketPrice.getEndDate())) {
                List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
                        ticketPrice.getEndDate().plusDays(1),
                        updateTicketPriceRequestDTO.getEndDate()
                );

                if (!overlappingTicketPrices.isEmpty()) {
                    throw new BadRequestException("Không thể kéo dài giá vé vì xung đột với giá vé khác trong khoảng thời gian này");
                }
            }

            ticketPrice.setEndDate(updateTicketPriceRequestDTO.getEndDate());
        } else {
            if (updateTicketPriceRequestDTO.getStatus() == BaseStatus.INACTIVE) {
                modelMapper.map(updateTicketPriceRequestDTO, ticketPrice);
            } else {
                List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
                        updateTicketPriceRequestDTO.getStartDate(),
                        updateTicketPriceRequestDTO.getEndDate()
                );

                overlappingTicketPrices = overlappingTicketPrices.stream()
                        .filter(tp -> tp.getId() != id)
                        .toList();

                if (!overlappingTicketPrices.isEmpty()) {
                    throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
                }
                // Prevent activation if the ticket's date includes the current date
                if (!updateTicketPriceRequestDTO.getStartDate().isAfter(currentDate) &&
                        !updateTicketPriceRequestDTO.getEndDate().isBefore(currentDate)) {
                    throw new BadRequestException("Giá vé không thể hoạt động khi nằm trong ngày đang mở vé");
                }


                modelMapper.map(updateTicketPriceRequestDTO, ticketPrice);
            }

        }
        return ticketPriceRepository.save(ticketPrice);
    }

    @Override
    public void deleteTicketPrice(int id) {
        TicketPrice ticketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa giá vé đang hoạt động");
        }
        ticketPriceRepository.delete(ticketPrice);
    }

    @Override
    public TicketPrice getTicketPriceById(int id) {
        return ticketPriceRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
    }

    @Transactional
    @Override
    public TicketPriceLine createTicketPriceLine(int ticketPriceId, CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO) {
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
                .status(BaseStatus.ACTIVE)
                .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                .price(createTicketPriceLineRequestDTO.getVipPrice())
                .seatType(SeatType.VIP)
                .status(BaseStatus.ACTIVE)
                .build());

        ticketPriceLine.addTicketPriceDetail(TicketPriceDetail.builder()
                .price(createTicketPriceLineRequestDTO.getCouplePrice())
                .seatType(SeatType.COUPLE)
                .status(BaseStatus.ACTIVE)
                .build());

        return ticketPriceLineRepository.save(ticketPriceLine);
    }

    @Override
    public TicketPriceLine updateTicketPriceLine(int ticketPriceId, int lineId, UpdateTicketPriceLineRequestDTO updateTicketPriceLineRequestDTO) {
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

        TicketPriceLine ticketPriceLine = ticketPriceLineRepository.findByIdAndDeleted(lineId, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dòng giá vé"));
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
                case TRIPLE:
                    ticketPriceDetail.setPrice(updateTicketPriceLineRequestDTO.getTriplePrice());
                    break;
            }
        });
        return ticketPriceLineRepository.save(ticketPriceLine);
    }
}
