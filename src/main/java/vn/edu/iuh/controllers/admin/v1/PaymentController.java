package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;
import vn.edu.iuh.dto.common.zalopay.res.GetOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.services.ZaloPayService;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Payment.BASE)
@RestController("paymentControllerAdminV1")
@Tag(name = "ADMIN V1: Payment Management", description = "Quản lý thanh toán")
public class PaymentController {
    private final ZaloPayService zaloPayService;

    @Operation(summary = AdminSwagger.Payment.CREATE_ORDER_ZALOPAY_SUM)
    @PostMapping(AdminPaths.Payment.CREATE_ORDER_ZALOPAY)
    public SuccessResponse<CreateOrderZaloPayResponseDTO> createZaloPay(
            @RequestBody @Valid CreateOrderZaloPayRequestDTO body
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                zaloPayService.createOrderTransaction(body)
        );
    }

    @Operation(summary = AdminSwagger.Payment.STATUS_ORDER_ZALOPAY_SUM)
    @GetMapping(AdminPaths.Payment.STATUS_ORDER_ZALOPAY)
    public SuccessResponse<GetOrderZaloPayResponseDTO> getOrderZalo(
            @RequestParam String transId
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                zaloPayService.getOrderTransaction(transId)
        );
    }
}
