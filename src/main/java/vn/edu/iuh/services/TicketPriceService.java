package vn.edu.iuh.services;

import vn.edu.iuh.models.TicketPrice;

import java.time.LocalDate;

public interface TicketPriceService {
    TicketPrice getByDate(LocalDate date);
}
