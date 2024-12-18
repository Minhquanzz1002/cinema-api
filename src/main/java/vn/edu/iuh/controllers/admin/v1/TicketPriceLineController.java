package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceDetailRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.TicketPriceLineService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/ticket-price-lines")
@Tag(name = "ADMIN V1: Ticket Price Line Management", description = "Quản lý chi tiết giá vé")
public class TicketPriceLineController {
    private final TicketPriceLineService ticketPriceLineService;

    @PostMapping("/{id}/details")
    public SuccessResponse<?> createTicketPriceDetail(@PathVariable int id, @RequestBody @Valid CreateTicketPriceDetailRequestDTO createTicketPriceDetailRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceLineService.createTicketPriceDetail(id, createTicketPriceDetailRequestDTO));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<?> deleteTicketPriceLine(@PathVariable int id) {
        ticketPriceLineService.deleteTicketPriceLine(id);
        return new SuccessResponse<>(200, "success", "Xóa giá vé thành công", null);
    }
}
