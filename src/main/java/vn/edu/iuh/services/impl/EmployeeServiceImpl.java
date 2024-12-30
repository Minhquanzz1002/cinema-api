package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.edu.iuh.dto.admin.v1.employee.req.CreateEmployeeRequest;
import vn.edu.iuh.dto.admin.v1.req.EmployeeResponseDTO;
import vn.edu.iuh.dto.admin.v1.employee.req.UpdateEmployeeRequest;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.RoleRepository;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.services.EmployeeService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.UserSpecification;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static vn.edu.iuh.constant.SecurityConstant.ROLE_EMPLOYEE_SALE;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(String search, UserStatus status, String role, Pageable pageable) {
        Specification<User> specification = Specification.where(UserSpecification.hasRole(role))
                .and(UserSpecification.hasStatus(status))
                .and(UserSpecification.excludeClientRole())
                .and(GenericSpecifications.withDeleted(false))
                .and(UserSpecification.hasSearchKey(search));
        Page<User> employees = userRepository.findAll(specification, pageable);
        return employees.map(emp -> modelMapper.map(emp, EmployeeResponseDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployee(UUID id) {
        return userRepository.findByIdAndDeletedFalseAndRole_Name(id, ROLE_EMPLOYEE_SALE)
                .map(this::mapToEmployeeResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));
    }

    @Override
    @Transactional
    public EmployeeResponseDTO createEmployee(CreateEmployeeRequest request) {
        if (userRepository.existsByEmailAndDeleted(request.getEmail(), false)) {
            throw new BadRequestException("Email đã tồn tại");
        }
        if (userRepository.existsByPhoneAndDeleted(request.getPhone(), false)) {
            throw new BadRequestException("Số điện thoại đã tồn tại");
        }

        Role role = roleRepository.findByIdAndDeleted(request.getRoleId(), false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy vai trò"));

        String prefix = "NV";
        if (Objects.equals(role.getName(), ROLE_EMPLOYEE_SALE)) {
            prefix = "NVBH";
        }

        User employee = User.builder()
                .code(generateEmployeeCode(prefix))
                .name(request.getName())
                .gender(request.isGender())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .birthday(request.getBirthday())
                .status(request.getStatus())
                .role(role)
                .invalidateBefore(LocalDateTime.now())
                .build();

        return mapToEmployeeResponse(userRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID id, UpdateEmployeeRequest dto) {
        User employee = userRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy nhân viên"));

        // Validate phone if changed
        if (!employee.getPhone().equals(dto.getPhone()) && userRepository.existsByPhoneAndDeleted(dto.getPhone(), false)) {
            throw new BadRequestException("Số điện thoại đã tồn tại");
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        employee.setName(dto.getName());
        employee.setGender(dto.isGender());
        employee.setPhone(dto.getPhone());
        employee.setBirthday(dto.getBirthday());
        employee.setStatus(dto.getStatus());

        return mapToEmployeeResponse(userRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        User employee = userRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy nhân viên"));

        if (employee.getStatus() == UserStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa nhân viên đang hoạt động");
        }

        employee.setDeleted(true);
        userRepository.save(employee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeByCode(String code) {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với mã " + code));
        return mapToEmployeeResponse(user);
    }

    private EmployeeResponseDTO mapToEmployeeResponse(User user) {
        return EmployeeResponseDTO.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .gender(user.isGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthday(user.getBirthday())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt().toLocalDate())
                .updatedAt(user.getUpdatedAt().toLocalDate())
                .build();
    }

    private String generateEmployeeCode(String prefix) {
        Optional<User> lastEmployee = userRepository.findTopByCodeStartingWithOrderByCodeDesc(prefix);

        int nextNumber = 1;

        if (lastEmployee.isPresent()) {
            String lastCode = lastEmployee.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(4)) + 1;
        }

        return String.format("%s%08d", prefix, nextNumber);

    }
}
