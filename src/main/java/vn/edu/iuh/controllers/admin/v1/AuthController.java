package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_AUTH_BASE_PATH;
import static vn.edu.iuh.constant.RouterConstant.POST_ADMIN_LOGIN_SUB_PATH;
import static vn.edu.iuh.constant.SwaggerConstant.POST_LOGIN_DESC;
import static vn.edu.iuh.constant.SwaggerConstant.POST_LOGIN_SUM;

@Slf4j
@RestController("authControllerAdminV1")
@RequiredArgsConstructor
@RequestMapping(ADMIN_AUTH_BASE_PATH)
@Tag(name = "ADMIN V1: Authentication", description = "Xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = POST_LOGIN_SUM, description = POST_LOGIN_DESC)
    @PostMapping(POST_ADMIN_LOGIN_SUB_PATH)
    public SuccessResponse<UserAuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", authService.login(loginRequestDTO, true));
    }

    @Operation(
            summary = "Thông tin người dùng",
            description = "Lấy thông tin người dùng thông qua JWT",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @GetMapping("/profile")
    public SuccessResponse<?> profile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return authService.getProfile(userPrincipal);
    }

    @Operation(
            summary = "Quên mật khẩu",
            description = "Gửi mật khẩu mới (8 ký tự) tới email"
    )
    @PostMapping("/forgot-password")
    public SuccessResponse<?> forgotPassword(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
        return authService.forgotPassword(emailRequestDTO.getEmail());
    }

    @Operation(
            summary = "Đăng xuất"
    )
    @PostMapping("/logout")
    public SuccessResponse<UserAuthResponseDTO> logout() {
        return null;
    }

    @Operation(
            summary = "Đổi mật khẩu",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/change-password")
    public SuccessResponse<UserAuthResponseDTO> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        return authService.changePassword(userPrincipal, changePasswordRequestDTO);
    }

}
