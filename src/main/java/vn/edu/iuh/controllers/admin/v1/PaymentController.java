package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.common.zalopay.CreateOrderZaloPayResponseDTO;
import vn.edu.iuh.dto.common.zalopay.req.CreateOrderZaloPayRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.ZaloPayService;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_PAYMENT_BASE_PATH;
import static vn.edu.iuh.constant.RouterConstant.POST_ADMIN_ZALOPAY_SUB_PATH;
import static vn.edu.iuh.constant.SwaggerConstant.POST_ADMIN_PAYMENT_ZALOPAY_SUM;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_PAYMENT_BASE_PATH)
@RestController("paymentControllerAdminV1")
@Tag(name = "Payment Controller Admin V1", description = "Quản lý thanh toán")
public class PaymentController {
    private final ZaloPayService zaloPayService;

    @Operation(summary = POST_ADMIN_PAYMENT_ZALOPAY_SUM)
    @PostMapping(POST_ADMIN_ZALOPAY_SUB_PATH)
    public SuccessResponse<CreateOrderZaloPayResponseDTO> createZaloPay(@RequestBody @Valid CreateOrderZaloPayRequestDTO body) {
        return new SuccessResponse<>(200, "success", "Thành công", zaloPayService.createOrderTransaction(body));
    }
}
