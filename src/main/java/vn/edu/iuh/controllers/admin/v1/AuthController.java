package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.ChangePasswordRequestDTO;
import vn.edu.iuh.dto.req.EmailRequestDTO;
import vn.edu.iuh.dto.req.LoginRequestDTO;
import vn.edu.iuh.dto.req.UpdateProfileRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RestController("authControllerAdminV1")
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Auth.BASE)
@Tag(name = "ADMIN V1: Authentication", description = "Xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = AdminSwagger.Auth.LOGIN_SUM, description = POST_LOGIN_DESC)
    @PostMapping(AdminPaths.Auth.LOGIN)
    public SuccessResponse<UserAuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                authService.login(loginRequestDTO, true)
        );
    }

    @Operation(
            summary = AdminSwagger.Auth.PROFILE_SUM,
            description = "Lấy thông tin người dùng thông qua JWT",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @GetMapping(AdminPaths.Auth.PROFILE)
    public SuccessResponse<?> profile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return authService.getProfile(userPrincipal);
    }

    @Operation(
            summary = AdminSwagger.Auth.UPDATE_PROFILE_SUM,
            description = AdminSwagger.Auth.UPDATE_PROFILE_DESC,
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping(AdminPaths.Auth.UPDATE_PROFILE)
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
            summary = AdminSwagger.Auth.FORGOT_PASSWORD_SUM,
            description = "Gửi mật khẩu mới (8 ký tự) tới email")
    @PostMapping(AdminPaths.Auth.FORGOT_PASSWORD)
    public SuccessResponse<?> forgotPassword(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
        return authService.forgotPassword(emailRequestDTO.getEmail());
    }

    @Operation(summary = AdminSwagger.Auth.LOGOUT_SUM)
    @PostMapping(AdminPaths.Auth.LOGOUT)
    public SuccessResponse<UserAuthResponseDTO> logout() {
        return null;
    }

    @Operation(
            summary = AdminSwagger.Auth.CHANGE_PASSWORD_SUM,
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/change-password")
    public SuccessResponse<UserAuthResponseDTO> changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        return authService.changePassword(userPrincipal, changePasswordRequestDTO);
    }

}
