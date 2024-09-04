package vn.edu.iuh.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.AuthService;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public SuccessResponse<?> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        log.info(registerRequestDTO.toString());
        return authService.register(registerRequestDTO);
    }

    @PostMapping("/login")
    public String login() {
        return "hlo";
    }

}
