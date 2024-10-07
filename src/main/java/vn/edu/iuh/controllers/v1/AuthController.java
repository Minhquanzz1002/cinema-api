package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;

@Slf4j
@RestController("authControllerV1")
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Authentication V1", description = "Xác thực người dùng")
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

    @Operation(
            summary = "Đăng nhập"
    )
    @PostMapping("/login")
    public SuccessResponse<UserAuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
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
            summary = "Cập nhật thông tin người dùng",
            description = """
                    Quy ước: Nam là `true`, Nữ là `false`
                    """,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping("/profile")
    public SuccessResponse<UserResponseDTO> changeProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid UpdateProfileRequestDTO updateProfileRequestDTO) {
        return authService.updateProfile(userPrincipal, updateProfileRequestDTO);
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
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PostMapping("/change-password")
    public SuccessResponse<UserAuthResponseDTO> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        return authService.changePassword(userPrincipal, changePasswordRequestDTO);
    }

}
