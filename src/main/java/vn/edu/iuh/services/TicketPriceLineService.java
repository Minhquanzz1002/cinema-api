package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceDetailRequestDTO;
import vn.edu.iuh.models.TicketPriceDetail;

public interface TicketPriceLineService {
    TicketPriceDetail createTicketPriceDetail(int lineId, CreateTicketPriceDetailRequestDTO createTicketPriceDetailRequestDTO);
    void deleteTicketPriceLine(int id);
}
