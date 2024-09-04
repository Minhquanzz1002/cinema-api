package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;

public interface AuthService {
    SuccessResponse<?> register(RegisterRequestDTO registerRequestDTO);
}
