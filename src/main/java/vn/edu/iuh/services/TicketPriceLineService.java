package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.ticketprice.detail.req.CreateTicketPriceDetailRequest;
import vn.edu.iuh.models.TicketPriceDetail;

public interface TicketPriceLineService {
    TicketPriceDetail createTicketPriceDetail(int lineId, CreateTicketPriceDetailRequest request);
    void deleteTicketPriceLine(int id);
}
