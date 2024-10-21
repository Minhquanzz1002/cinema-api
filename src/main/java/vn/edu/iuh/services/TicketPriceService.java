package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceDetailRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceRequestDTO;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceDetail;
import vn.edu.iuh.models.TicketPriceLine;

import java.time.LocalDate;

public interface TicketPriceService {
    TicketPrice getByDate(LocalDate date);

    Page<TicketPrice> getAllTicketPrices(Pageable pageable);

    TicketPrice createTicketPrice(CreateTicketPriceRequestDTO createTicketPriceRequestDTO);
    TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO);

    void deleteTicketPrice(int id);

    TicketPrice getTicketPriceById(int id);

    TicketPriceLine createTicketPriceLine(int ticketPriceId, CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO);
}
