package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.ChangePasswordRequestDTO;
import vn.edu.iuh.dto.req.LoginRequestDTO;
import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.req.RegistrationConfirmationRequestDTO;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.security.UserPrincipal;

public interface AuthService {
    SuccessResponse<?> register(RegisterRequestDTO registerRequestDTO);
    SuccessResponse<?> confirmRegister(RegistrationConfirmationRequestDTO registrationConfirmationRequestDTO);
    SuccessResponse<UserAuthResponseDTO> login(LoginRequestDTO loginRequestDTO);
    SuccessResponse<UserResponseDTO> getProfile(UserPrincipal userPrincipal);
    SuccessResponse<?> forgotPassword(String email);
    SuccessResponse<UserAuthResponseDTO> changePassword(UserPrincipal userPrincipal, ChangePasswordRequestDTO changePasswordRequestDTO);
}
