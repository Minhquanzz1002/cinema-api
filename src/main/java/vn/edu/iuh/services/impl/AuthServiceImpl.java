package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.client.v1.auth.req.RegisterRequest;
import vn.edu.iuh.dto.client.v1.auth.req.VerifyOtpRequest;
import vn.edu.iuh.dto.common.auth.req.ChangePasswordRequest;
import vn.edu.iuh.dto.common.auth.req.LoginRequest;
import vn.edu.iuh.dto.common.auth.req.UpdateProfileRequest;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.dto.common.auth.res.UserAuthResponse;
import vn.edu.iuh.dto.common.auth.res.UserResponse;
import vn.edu.iuh.exceptions.*;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.RoleRepository;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.AuthService;
import vn.edu.iuh.services.EmailService;
import vn.edu.iuh.services.OTPService;
import vn.edu.iuh.utils.JwtUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static vn.edu.iuh.constant.SecurityConstant.ROLE_ADMIN;
import static vn.edu.iuh.constant.SecurityConstant.ROLE_EMPLOYEE_SALE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OTPService otpService;

    @Override
    public void register(RegisterRequest request) {
        String email = request.getEmail();
        if (userRepository.existsByEmailAndDeleted(email, false)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (userRepository.existsByPhoneAndDeleted(request.getPhone(), false)) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        User user = modelMapper.map(request, User.class);
        Role role = roleRepository.findByName("ROLE_CLIENT").orElseThrow(() -> new InternalServerErrorException("Role ROLE_CLIENT không tồn tại"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.PENDING);
        userRepository.save(user);

        String otp = generateOTP();
        otpService.saveOTP(email, otp);
        emailService.sendEmail(email, "Xác thực tài khoản", "Mã xác thực OTP của bạn là: " + otp);
    }

    @Override
    public SuccessResponse<?> confirmRegister(VerifyOtpRequest request) {
        User user = getUserByEmail(request.getEmail());
        boolean isValid = otpService.validateOTP(user.getEmail(), request.getOtp());
        if (!isValid) {
            throw new OTPMismatchException("OTP không khớp hoặc không tìm thấy");
        } else {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return new SuccessResponse<>(200, "success", "Xác thực thành công. Hãy đăng nhập lại", null);
    }

    @Override
    public UserAuthResponse login(LoginRequest request, boolean isAdminLogin) {
        User user = getUserByEmail(request.getEmail());
        validateUserStatus(user.getStatus());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Tài khoản hoặc mật khẩu không đúng. Hãy nhập lại");
        }

        boolean isAdminUser = List.of(ROLE_ADMIN, ROLE_EMPLOYEE_SALE).contains(user.getRole().getName());
        if (isAdminLogin) {
            if (!isAdminUser) {
                throw new UnauthorizedException("Tài khoản không có quyền truy cập");
            }
        } else {
            if (isAdminUser) {
                throw new UnauthorizedException("Tài khoản không có quyền truy cập");
            }
        }

        return createAuthResponse(user);
    }

    @Override
    public UserResponse getProfile(UserPrincipal userPrincipal) {
        User user = getUserByEmail(userPrincipal.getEmail());
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateProfile(UserPrincipal principal, UpdateProfileRequest request) {
        User user = getUserByEmail(principal.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user = userRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public SuccessResponse<?> forgotPassword(String email) {
        User user = getUserByEmail(email);

        String newPassword = generateRandomPassword();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setInvalidateBefore(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendEmail(email, "Reset Password", "Password: " + newPassword);
        return new SuccessResponse<>(200, "success", "Email khôi phục lại mật khẩu đã được gởi lại thành công tới " + email, null);
    }

    @Override
    public SuccessResponse<UserAuthResponse> changePassword(UserPrincipal userPrincipal, ChangePasswordRequest request) {
        User user = getUserByEmail(userPrincipal.getEmail());
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setInvalidateBefore(LocalDateTime.now());
        user = userRepository.save(user);

        return new SuccessResponse<>(200, "success", "Thay đổi mật khẩu thành công", createAuthResponse(user));
    }

    private void validateUserStatus(UserStatus status) {
        switch (status) {
            case PENDING -> throw new UnauthorizedException("Hãy hoàn thành việc đăng ký trước khi đăng nhập.");
            case BANNED -> throw new UnauthorizedException("Tài khoản bị khóa vĩnh viễn.");
            case SUSPENDED, INACTIVE -> throw new UnauthorizedException("Tài khoản tạm thời bị khóa.");
            case DELETED -> throw new UnauthorizedException("Tài khoản bị xóa vĩnh viễn.");
        }
    }

    private String generateOTP() {
        Random random = new Random();
        return String.valueOf(random.nextInt(10)) +
               random.nextInt(10) +
               random.nextInt(10) +
               random.nextInt(10) +
               random.nextInt(10) +
               random.nextInt(10);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Tài khoản không tồn tại"));
    }

    private String generateRandomPassword() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(1);

        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(1);

        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
        lowerCaseRule.setNumberOfCharacters(1);

        CharacterRule specialCharRule = new CharacterRule(new CharacterData() {
            @Override
            public String getErrorCode() {
                return "ERROR_CODE";
            }

            @Override
            public String getCharacters() {
                return "@#$%^&+=";
            }
        });
        specialCharRule.setNumberOfCharacters(1);
        return passwordGenerator.generatePassword(8, List.of(lowerCaseRule, upperCaseRule, digitRule, specialCharRule, new CharacterRule(EnglishCharacterData.Alphabetical, 4)));
    }

    private UserAuthResponse createAuthResponse(User user) {
        UserAuthResponse userAuthResponseDTO = modelMapper.map(user, UserAuthResponse.class);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userAuthResponseDTO.setAccessToken(jwtUtil.generateAccessToken(userPrincipal));
        userAuthResponseDTO.setRefreshToken(jwtUtil.generateRefreshToken(userPrincipal));
        return userAuthResponseDTO;
    }
}
