package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.TicketPriceRepository;
import vn.edu.iuh.services.TicketPriceService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TicketPriceServiceImpl implements TicketPriceService {
    private final TicketPriceRepository ticketPriceRepository;


    @Override
    public TicketPrice getByDate(LocalDate date) {
        return ticketPriceRepository.findByStatusAndDate(date, BaseStatus.ACTIVE).orElseThrow(() -> new DataNotFoundException("Giá vé chưa được xử lý"));
    }
}
