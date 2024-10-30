package vn.edu.iuh.controllers.admin.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateTicketPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateTicketPriceRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.ValidationException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.TicketPriceService;

import java.time.LocalDate;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/ticket-prices")
public class TicketPriceController {
    private final TicketPriceService ticketPriceService;

    @GetMapping
    public SuccessResponse<Page<TicketPrice>> getTicketPrices(@PageableDefault Pageable pageable,
                                                              @RequestParam(required = false, defaultValue = "") String name,
                                                              @RequestParam(required = false) LocalDate startDate,
                                                              @RequestParam(required = false) LocalDate endDate,
                                                              @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", ticketPriceService.getAllTicketPrices(name, status, startDate, endDate, pageable));
    }

    @PostMapping
    public SuccessResponse<TicketPrice> createTicketPrice(@RequestBody @Valid CreateTicketPriceRequestDTO createTicketPriceRequestDTO) {
        if (createTicketPriceRequestDTO.getStartDate().isAfter(createTicketPriceRequestDTO.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(200, "success", "Thêm bảng giá vé thành công", ticketPriceService.createTicketPrice(createTicketPriceRequestDTO));
    }

    @PutMapping("/{id}")
    public SuccessResponse<TicketPrice> updateTicketPrice(@PathVariable int id, @RequestBody @Valid UpdateTicketPriceRequestDTO updateTicketPriceRequestDTO) {
        if (updateTicketPriceRequestDTO.getStartDate().isAfter(updateTicketPriceRequestDTO.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(200, "success", "Cập nhật bảng giá thành công", ticketPriceService.updateTicketPrice(id, updateTicketPriceRequestDTO));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<String> deleteTicketPrice(@PathVariable int id) {
        ticketPriceService.deleteTicketPrice(id);
        return new SuccessResponse<>(200, "success", "Xóa bảng giá thành công", "Xóa giá vé thành công");
    }

    @PostMapping("/{id}/lines")
    public SuccessResponse<?> createTicketPriceLine(@PathVariable int id, @RequestBody @Valid CreateTicketPriceLineRequestDTO createTicketPriceLineRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thêm giá vé thành công", ticketPriceService.createTicketPriceLine(id, createTicketPriceLineRequestDTO));
    }

    @PutMapping("/{id}/lines/{lineId}")
    public SuccessResponse<?> updateTicketPriceLine(@PathVariable int id, @PathVariable int lineId, @RequestBody @Valid UpdateTicketPriceLineRequestDTO updateTicketPriceLineRequestDTO) {
        return new SuccessResponse<>(200, "success", "Cập nhật giá vé thành công", ticketPriceService.updateTicketPriceLine(id, lineId, updateTicketPriceLineRequestDTO));
    }
}
