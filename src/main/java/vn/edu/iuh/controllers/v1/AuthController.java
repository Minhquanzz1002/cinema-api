package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.constant.RouterConstant.ClientPaths;
import vn.edu.iuh.constant.SwaggerConstant.ClientSwagger;
import vn.edu.iuh.dto.client.v1.auth.req.RegisterRequest;
import vn.edu.iuh.dto.client.v1.auth.req.VerifyOtpRequest;
import vn.edu.iuh.dto.common.auth.req.ChangePasswordRequest;
import vn.edu.iuh.dto.client.v1.auth.req.ForgotPasswordRequest;
import vn.edu.iuh.dto.common.auth.req.LoginRequest;
import vn.edu.iuh.dto.common.auth.req.UpdateProfileRequest;
import vn.edu.iuh.dto.common.auth.res.UserAuthResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.dto.common.auth.res.UserResponse;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;

@Slf4j
@RestController("authControllerV1")
@RequiredArgsConstructor
@RequestMapping(ClientPaths.Auth.BASE)
@Tag(name = "V1: Authentication", description = "Xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = ClientSwagger.Auth.CONFIRM_REGISTER_SUM)
    @PostMapping(ClientPaths.Auth.CONFIRM_REGISTER)
    public SuccessResponse<?> confirmRegistration(
            @RequestBody VerifyOtpRequest confirmationRequestDTO
    ) {
        return authService.confirmRegister(confirmationRequestDTO);
    }

    @Operation(summary = ClientSwagger.Auth.REGISTER_SUM)
    @PostMapping(ClientPaths.Auth.REGISTER)
    public SuccessResponse<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return new SuccessResponse<>(
                200,
                "success",
                "Tạo tài khoản thành công",
                null
        );
    }

    @Operation(summary = ClientSwagger.Auth.LOGIN_SUM)
    @PostMapping(ClientPaths.Auth.LOGIN)
    public SuccessResponse<UserAuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return new SuccessResponse<>(
                200,
                "success",
                "Đăng nhập thành công.",
                authService.login(request, false)
        );
    }

    @Operation(
            summary = ClientSwagger.Auth.PROFILE_SUM,
            description = "Lấy thông tin người dùng thông qua JWT",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @GetMapping(ClientPaths.Auth.PROFILE)
    public SuccessResponse<UserResponse> profile(@AuthenticationPrincipal UserPrincipal principal) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                authService.getProfile(principal)
        );
    }

    @Operation(
            summary = ClientSwagger.Auth.UPDATE_PROFILE_SUM,
            description = ClientSwagger.Auth.UPDATE_PROFILE_DESC,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping(ClientPaths.Auth.UPDATE_PROFILE)
    public SuccessResponse<UserResponse> changeProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật thông tin thành công",
                authService.updateProfile(principal, request)
        );
    }

    @Operation(
            summary = ClientSwagger.Auth.FORGOT_PASSWORD_SUM,
            description = "Gửi mật khẩu mới (8 ký tự) tới email"
    )
    @PostMapping(ClientPaths.Auth.FORGOT_PASSWORD)
    public SuccessResponse<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return authService.forgotPassword(request.getEmail());
    }

    @Operation(summary = ClientSwagger.Auth.LOGOUT_SUM)
    @PostMapping(ClientPaths.Auth.LOGOUT)
    public SuccessResponse<UserAuthResponse> logout() {
        return null;
    }

    @Operation(
            summary = ClientSwagger.Auth.CHANGE_PASSWORD_SUM,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PostMapping(ClientPaths.Auth.CHANGE_PASSWORD)
    public SuccessResponse<UserAuthResponse> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        return authService.changePassword(principal, request);
    }

}
