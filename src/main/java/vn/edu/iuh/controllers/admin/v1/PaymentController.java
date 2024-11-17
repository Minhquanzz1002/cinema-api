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
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.ZaloPayService;

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.GET_ADMIN_PAYMENT_ZALOPAY_SUM;
import static vn.edu.iuh.constant.SwaggerConstant.POST_ADMIN_PAYMENT_ZALOPAY_SUM;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_PAYMENT_BASE_PATH)
@RestController("paymentControllerAdminV1")
@Tag(name = "Payment Management", description = "Quản lý thanh toán")
public class PaymentController {
    private final ZaloPayService zaloPayService;

    @Operation(summary = POST_ADMIN_PAYMENT_ZALOPAY_SUM)
    @PostMapping(POST_ADMIN_ZALOPAY_SUB_PATH)
    public SuccessResponse<CreateOrderZaloPayResponseDTO> createZaloPay(@RequestBody @Valid CreateOrderZaloPayRequestDTO body) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                zaloPayService.createOrderTransaction(body)
        );
    }

    @Operation(summary = GET_ADMIN_PAYMENT_ZALOPAY_SUM)
    @GetMapping(GET_ADMIN_ZALOPAY_SUB_PATH)
    public SuccessResponse<GetOrderZaloPayResponseDTO> getOrderZalo(@RequestParam String transId) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                zaloPayService.getOrderTransaction(transId)
        );
    }
}
