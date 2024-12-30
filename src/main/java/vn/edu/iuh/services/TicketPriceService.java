package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.CreateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.UpdateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CopyTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CreateTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.UpdateTicketPriceRequest;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

public interface TicketPriceService {
    TicketPrice createTicketPrice(CreateTicketPriceRequest request);

    /**
     * Copy a ticket price with new start and end date
     *
     * @param id      ID of original ticket price
     * @param request DTO contains new start and end date
     * @return Copied ticket price with new start and end date
     */
    TicketPrice copyTicketPrice(int id, CopyTicketPriceRequest request);

    /**
     * Find ticket price by ID
     *
     * @param id ID of ticket price
     * @return Ticket price
     */
    TicketPrice findById(int id);

    TicketPrice getByDate(LocalDate date);

    Page<TicketPrice> getAllTicketPrices(
            String name,
            BaseStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );


    TicketPrice updateTicketPrice(int id, UpdateTicketPriceRequest request);

    void deleteTicketPrice(int id);

    TicketPrice getTicketPriceById(int id);

    TicketPriceLine createTicketPriceLine(
            int ticketPriceId,
            CreateTicketPriceLineRequest request
    );

    TicketPriceLine updateTicketPriceLine(
            int ticketPriceId,
            int lineId,
            UpdateTicketPriceLineRequest request
    );
}
