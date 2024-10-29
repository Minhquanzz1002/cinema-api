package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.*;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

public interface TicketPriceService {
    TicketPrice getByDate(LocalDate date);

    Page<TicketPrice> getAllTicketPrices(String name, BaseStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    TicketPrice createTicketPrice(CreateTicketPriceRequestDTO createTicketPriceRequestDTO);
    TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO);

    void deleteTicketPrice(int id);

    TicketPrice getTicketPriceById(int id);

    TicketPriceLine createTicketPriceLine(int ticketPriceId, CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO);

    TicketPriceLine updateTicketPriceLine(int ticketPriceId, int lineId, UpdateTicketPriceLineRequestDTO updateTicketPriceLineRequestDTO);
}
