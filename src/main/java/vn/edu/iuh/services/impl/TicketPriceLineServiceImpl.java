package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.ticketprice.detail.req.CreateTicketPriceDetailRequest;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.TicketPriceDetail;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.TicketPriceDetailRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.services.TicketPriceLineService;

@Service
@RequiredArgsConstructor
public class TicketPriceLineServiceImpl implements TicketPriceLineService {
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final TicketPriceDetailRepository ticketPriceDetailRepository;
    private final ModelMapper modelMapper;


    @Override
    public TicketPriceDetail createTicketPriceDetail(int lineId, CreateTicketPriceDetailRequest request) {
        TicketPriceLine ticketPriceLine = ticketPriceLineRepository.findByIdAndDeleted(lineId, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy dòng giá vé"));

        boolean seatTypeExists = ticketPriceLine.getTicketPriceDetails().stream()
                .anyMatch(detail -> detail.getSeatType() == request.getSeatType());

        if (seatTypeExists) {
            throw new BadRequestException("Loại ghế đã tồn tại");
        }

        TicketPriceDetail ticketPriceDetail = modelMapper.map(request, TicketPriceDetail.class);
        ticketPriceDetail.setTicketPriceLine(ticketPriceLine);
        return ticketPriceDetailRepository.save(ticketPriceDetail);
    }

    @Override
    public void deleteTicketPriceLine(int id) {
        TicketPriceLine ticketPriceLine = ticketPriceLineRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá vé"));

        if (ticketPriceLine.getTicketPrice().getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa giá vé đang hoạt động");
        }

        ticketPriceLine.setDeleted(true);
        ticketPriceLine.setStatus(BaseStatus.INACTIVE);
        ticketPriceLine.getTicketPriceDetails().forEach(detail -> detail.setDeleted(true));
        ticketPriceLineRepository.save(ticketPriceLine);
    }
}
