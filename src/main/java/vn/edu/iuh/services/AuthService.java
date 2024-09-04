package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.LoginRequestDTO;
import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.res.LoginResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;

public interface AuthService {
    SuccessResponse<?> register(RegisterRequestDTO registerRequestDTO);
    SuccessResponse<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
}
