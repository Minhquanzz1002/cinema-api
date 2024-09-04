package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.req.RegisterRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.InternalServerErrorException;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.RoleRepository;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.services.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public SuccessResponse<?> register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        if (userRepository.existsByPhone(registerRequestDTO.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");
        }

        User user = modelMapper.map(registerRequestDTO, User.class);
        Role role = roleRepository.findByName("ROLE_CLIENT").orElseThrow(() -> new InternalServerErrorException("Role ROLE_CLIENT không tồn tại"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.PENDING);
        userRepository.save(user);
        return new SuccessResponse<>(200, "success", "Tạo tài khoản thành công", null);
    }
}
