package vn.edu.iuh.services;

import vn.edu.iuh.dto.client.v1.auth.req.RegisterRequest;
import vn.edu.iuh.dto.client.v1.auth.req.VerifyOtpRequest;
import vn.edu.iuh.dto.common.auth.req.ChangePasswordRequest;
import vn.edu.iuh.dto.common.auth.req.LoginRequest;
import vn.edu.iuh.dto.common.auth.req.UpdateProfileRequest;
import vn.edu.iuh.dto.common.auth.res.UserAuthResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.dto.common.auth.res.UserResponse;
import vn.edu.iuh.security.UserPrincipal;

public interface AuthService {
    void register(RegisterRequest request);

    SuccessResponse<?> confirmRegister(VerifyOtpRequest request);

    UserAuthResponse login(LoginRequest request, boolean isAdminLogin);

    UserResponse getProfile(UserPrincipal userPrincipal);

    UserResponse updateProfile(
            UserPrincipal principal,
            UpdateProfileRequest request
    );

    SuccessResponse<?> forgotPassword(String email);

    SuccessResponse<UserAuthResponse> changePassword(
            UserPrincipal userPrincipal,
            ChangePasswordRequest request
    );
}
