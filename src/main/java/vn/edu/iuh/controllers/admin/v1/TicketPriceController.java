package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.CreateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.line.req.UpdateTicketPriceLineRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CopyTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.CreateTicketPriceRequest;
import vn.edu.iuh.dto.admin.v1.ticketprice.req.UpdateTicketPriceRequest;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.exceptions.ValidationException;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.TicketPriceService;

import java.time.LocalDate;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AdminPaths.TicketPrice.BASE)
@Tag(name = "ADMIN V1: Ticket Price Management", description = "Quản lý bảng giá vé")
public class TicketPriceController {
    private final TicketPriceService ticketPriceService;

    @Operation(summary = AdminSwagger.TicketPrice.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<TicketPrice>> getTicketPrices(
            @PageableDefault(sort = {"startDate", "endDate"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BaseStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                ticketPriceService.getAllTicketPrices(name, status, startDate, endDate, pageable)
        );
    }

    @Operation(summary = AdminSwagger.TicketPrice.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<TicketPrice> createTicketPrice(
            @RequestBody @Valid CreateTicketPriceRequest request
    ) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm bảng giá vé thành công",
                ticketPriceService.createTicketPrice(request)
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = AdminSwagger.TicketPrice.COPY_SUM)
    @PostMapping(AdminPaths.TicketPrice.COPY)
    public SuccessResponse<TicketPrice> copyTicketPrice(
            @PathVariable int id,
            @RequestBody @Valid CopyTicketPriceRequest request
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Sao chép bảng giá vé thành công",
                ticketPriceService.copyTicketPrice(id, request)
        );
    }

    @Operation(summary = AdminSwagger.TicketPrice.UPDATE_SUM)
    @PutMapping(AdminPaths.TicketPrice.UPDATE)
    public SuccessResponse<TicketPrice> updateTicketPrice(
            @PathVariable int id,
            @RequestBody @Valid UpdateTicketPriceRequest request
    ) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new ValidationException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật bảng giá thành công",
                ticketPriceService.updateTicketPrice(id, request)
        );
    }

    @Operation(summary = AdminSwagger.TicketPrice.DELETE_SUM)
    @DeleteMapping(AdminPaths.TicketPrice.DELETE)
    public SuccessResponse<String> deleteTicketPrice(@PathVariable int id) {
        ticketPriceService.deleteTicketPrice(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa bảng giá thành công",
                "Xóa giá vé thành công"
        );
    }

    @Operation(summary = AdminSwagger.TicketPrice.CREATE_LINES_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(AdminPaths.TicketPrice.CREATE_LINES)
    public SuccessResponse<?> createTicketPriceLine(
            @PathVariable int id,
            @RequestBody @Valid CreateTicketPriceLineRequest request
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm giá vé thành công",
                ticketPriceService.createTicketPriceLine(id, request)
        );
    }

    @PutMapping("/{id}/lines/{lineId}")
    public SuccessResponse<?> updateTicketPriceLine(
            @PathVariable int id,
            @PathVariable int lineId,
            @RequestBody @Valid UpdateTicketPriceLineRequest request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật giá vé thành công",
                ticketPriceService.updateTicketPriceLine(id, lineId, request)
        );
    }
}
