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
import vn.edu.iuh.dto.admin.v1.req.CreateEmployeeDTO;
import vn.edu.iuh.dto.admin.v1.req.EmployeeResponseDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateEmployeeDTO;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.RoleRepository;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.services.EmployeeService;
import vn.edu.iuh.specifications.UserSpecification;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private static final String ROLE_EMPLOYEE_SALE = "ROLE_EMPLOYEE_SALE";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getEmployees(String search, UserStatus status, Pageable pageable) {
        Specification<User> specification = Specification.where(UserSpecification.hasRole(ROLE_EMPLOYEE_SALE))
                .and(UserSpecification.hasStatus(status))
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
    public EmployeeResponseDTO createEmployee(CreateEmployeeDTO dto) {
        // Validate unique constraints
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại");
        }

        Role role = roleRepository.findByName(ROLE_EMPLOYEE_SALE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy vai trò nhân viên bán hàng"));

        User employee = User.builder()
                .code(generateEmployeeCode())
                .name(dto.getName())
                .gender(dto.isGender())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .birthday(dto.getBirthday())
                .status(dto.getStatus())
                .role(role)
                .invalidateBefore(LocalDateTime.now())
                .build();

        return mapToEmployeeResponse(userRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponseDTO updateEmployee(UUID id, UpdateEmployeeDTO dto) {
        User employee = userRepository.findByIdAndDeletedFalseAndRole_Name(id, ROLE_EMPLOYEE_SALE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));

        // Validate phone if changed
        if (!employee.getPhone().equals(dto.getPhone()) && userRepository.existsByPhone(dto.getPhone())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại");
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
        User employee = userRepository.findByIdAndDeletedFalseAndRole_Name(id, ROLE_EMPLOYEE_SALE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"));

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

    private String generateEmployeeCode() {
        // Implement employee code generation logic
        // For example: EMP-[random UUID first 8 chars]
//        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Optional<User> lastEmployee = userRepository.findTopByOrderByCodeDesc();

        int nextNumber = 1;

        if (lastEmployee.isPresent()) {
            String lastCode = lastEmployee.get().getCode();
            try {
                String numericPart = lastCode.substring(4);
                nextNumber = Integer.parseInt(numericPart) + 1;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                nextNumber = 1;
            }
        }

        return String.format("NVBH%08d", nextNumber);

    }
}
