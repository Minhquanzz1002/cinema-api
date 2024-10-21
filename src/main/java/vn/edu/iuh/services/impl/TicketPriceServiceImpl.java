package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceDetailRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceDetail;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.TicketPriceDetailRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.repositories.TicketPriceRepository;
import vn.edu.iuh.services.TicketPriceService;

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
    public Page<TicketPrice> getAllTicketPrices(Pageable pageable) {
        return ticketPriceRepository.findAll(pageable);
    }

    @Override
    public TicketPrice createTicketPrice(CreateTicketPriceRequestDTO createTicketPriceRequestDTO) {
        List<TicketPrice> overlappingTicketPrices = ticketPriceRepository.findOverlappingTicketPrices(
                createTicketPriceRequestDTO.getStartDate(),
                createTicketPriceRequestDTO.getEndDate()
        );

        if (!overlappingTicketPrices.isEmpty()) {
            throw new BadRequestException("Đã tồn tại giá vé trong khoảng thời gian này");
        }

        TicketPrice ticketPrice = modelMapper.map(createTicketPriceRequestDTO, TicketPrice.class);
        return ticketPriceRepository.save(ticketPrice);
    }

    @Override
    public TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO) {
        TicketPrice ticketPrice = ticketPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
        LocalDate currentDate = LocalDate.now();
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

            modelMapper.map(updateTicketPriceRequestDTO, ticketPrice);
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
        return ticketPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));
    }

    @Override
    public TicketPriceLine createTicketPriceLine(int ticketPriceId, CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO) {
        TicketPrice ticketPrice = getTicketPriceById(ticketPriceId);
        if (ticketPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể thêm vào giá vé đang hoạt động");
        }
        TicketPriceLine ticketPriceLine = modelMapper.map(createTicketPriceLineRequestDTO, TicketPriceLine.class);
        ticketPriceLine.setTicketPrice(ticketPrice);
        return ticketPriceLineRepository.save(ticketPriceLine);
    }
}
