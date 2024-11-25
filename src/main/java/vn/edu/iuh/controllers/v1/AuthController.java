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
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;

@Slf4j
@RestController("authControllerV1")
@RequiredArgsConstructor
@RequestMapping(ClientPaths.Auth.BASE)
@Tag(name = "V1: Authentication", description = "Xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Xác thực đăng ký"
    )
    @PostMapping("/register/validate-otp")
    public SuccessResponse<?> confirmRegistration(@RequestBody RegistrationConfirmationRequestDTO confirmationRequestDTO) {
        return authService.confirmRegister(confirmationRequestDTO);
    }

    @Operation(
            summary = "Đăng ký"
    )
    @PostMapping("/register")
    public SuccessResponse<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @Operation(summary = ClientSwagger.Auth.LOGIN_SUM)
    @PostMapping(ClientPaths.Auth.LOGIN)
    public SuccessResponse<UserAuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", authService.login(loginRequestDTO, false));
    }

    @Operation(
            summary = ClientSwagger.Auth.PROFILE_SUM,
            description = "Lấy thông tin người dùng thông qua JWT",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @GetMapping(ClientPaths.Auth.PROFILE)
    public SuccessResponse<?> profile(@AuthenticationPrincipal UserPrincipal principal) {
        return authService.getProfile(principal);
    }

    @Operation(
            summary = ClientSwagger.Auth.UPDATE_PROFILE_SUM,
            description = ClientSwagger.Auth.UPDATE_PROFILE_DESC,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping(ClientPaths.Auth.UPDATE_PROFILE)
    public SuccessResponse<UserResponseDTO> changeProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UpdateProfileRequestDTO request
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
    public SuccessResponse<?> forgotPassword(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
        return authService.forgotPassword(emailRequestDTO.getEmail());
    }

    @Operation(summary = ClientSwagger.Auth.LOGOUT_SUM)
    @PostMapping(ClientPaths.Auth.LOGOUT)
    public SuccessResponse<UserAuthResponseDTO> logout() {
        return null;
    }

    @Operation(
            summary = ClientSwagger.Auth.CHANGE_PASSWORD_SUM,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PostMapping("/change-password")
    public SuccessResponse<UserAuthResponseDTO> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO
    ) {
        return authService.changePassword(userPrincipal, changePasswordRequestDTO);
    }

}
