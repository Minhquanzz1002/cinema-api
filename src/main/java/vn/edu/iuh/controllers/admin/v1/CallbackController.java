package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.services.ZaloPayService;

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_CALLBACK_BASE_PATH)
@RestController
@Tag(name = "Callback Controller Admin V1", description = "Quản lý callback")
public class CallbackController {
    private final ZaloPayService zaloPayService;

    @Operation(summary = POST_ADMIN_CALLBACK_ZALOPAY_SUM)
    @PostMapping(POST_ADMIN_CALLBACK_ZALOPAY_SUB_PATH)
    public ZaloPayCallBackResponseDTO callbackZaloPay(@RequestBody CallBackRequestDTO body) {
        log.info("Nhận được callback");
        return zaloPayService.processCallback(body);
    }
}
