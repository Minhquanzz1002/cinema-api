package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.common.zalopay.CallBackRequestDTO;
import vn.edu.iuh.dto.common.zalopay.ZaloPayCallBackResponseDTO;
import vn.edu.iuh.services.ZaloPayService;

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Callback.BASE)
@RestController
@CrossOrigin("*")
@Tag(name = "ADMIN V1: Callback Management", description = "Quản lý callback")
public class CallbackController {
    private final ZaloPayService zaloPayService;

    @Operation(summary = AdminSwagger.Callback.ZALOPAY_SUM)
    @PostMapping(AdminPaths.Callback.ZALOPAY)
    public ZaloPayCallBackResponseDTO callbackZaloPay(@RequestBody CallBackRequestDTO body) {
        log.info("Nhận được callback");
        return zaloPayService.processCallback(body);
    }
}
