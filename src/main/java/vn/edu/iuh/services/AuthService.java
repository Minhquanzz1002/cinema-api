package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.UserAuthResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.dto.res.UserResponseDTO;
import vn.edu.iuh.security.UserPrincipal;

public interface AuthService {
    SuccessResponse<?> register(RegisterRequestDTO registerRequestDTO);

    SuccessResponse<?> confirmRegister(RegistrationConfirmationRequestDTO registrationConfirmationRequestDTO);

    UserAuthResponseDTO login(LoginRequestDTO loginRequestDTO, boolean isAdminLogin);

    SuccessResponse<UserResponseDTO> getProfile(UserPrincipal userPrincipal);

    SuccessResponse<UserResponseDTO> updateProfile(UserPrincipal userPrincipal, UpdateProfileRequestDTO updateProfileRequestDTO);

    SuccessResponse<?> forgotPassword(String email);

    SuccessResponse<UserAuthResponseDTO> changePassword(UserPrincipal userPrincipal, ChangePasswordRequestDTO changePasswordRequestDTO);
}
