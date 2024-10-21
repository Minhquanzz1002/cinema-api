package vn.edu.iuh.controllers.admin.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceDetailRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.ValidationException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.services.TicketPriceService;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/ticket-prices")
public class TicketPriceController {
    private final TicketPriceService ticketPriceService;

    @GetMapping
    public SuccessResponse<Page<TicketPrice>> getTicketPrices(@PageableDefault Pageable pageable) {
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceService.getAllTicketPrices(pageable));
    }

    @PostMapping
    public SuccessResponse<TicketPrice> createTicketPrice(@RequestBody @Valid CreateTicketPriceRequestDTO createTicketPriceRequestDTO) {
        if (createTicketPriceRequestDTO.getStartDate().isAfter(createTicketPriceRequestDTO.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceService.createTicketPrice(createTicketPriceRequestDTO));
    }

    @PutMapping("/{id}")
    public SuccessResponse<TicketPrice> updateTicketPrice(@PathVariable int id, @RequestBody @Valid UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO) {
        if (updateTicketPriceRequestDTO.getStartDate().isAfter(updateTicketPriceRequestDTO.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceService.updateTicketPrice(id, updateTicketPriceRequestDTO));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<String> deleteTicketPrice(@PathVariable int id) {
        ticketPriceService.deleteTicketPrice(id);
        return new SuccessResponse<>(200, "success", "Thành công", "Xóa giá vé thành công");
    }

    @PostMapping("/{id}/lines")
    public SuccessResponse<?> createTicketPriceLine(@PathVariable int id, @RequestBody @Valid CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceService.createTicketPriceLine(id, createTicketPriceLineRequestDTO));
    }
}
