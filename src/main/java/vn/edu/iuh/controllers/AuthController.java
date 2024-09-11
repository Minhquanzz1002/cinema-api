package vn.edu.iuh.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.LoginRequestDTO;
import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.res.LoginResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "Xác thực người dùng")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register/confirm")
    public String confirmRegistration(@RequestBody String token) {
        return null;
    }

    @PostMapping("/register")
    public SuccessResponse<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public SuccessResponse<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return authService.login(loginRequestDTO);
    }

}
