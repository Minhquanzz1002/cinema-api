package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.constant.SecurityConstant;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminCustomerResponseDTO;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.services.CustomerService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.UserSpecification;

import java.util.List;
import java.util.UUID;

import static vn.edu.iuh.constant.SecurityConstant.ROLE_CLIENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AdminCustomerWithNameAndPhoneProjection> getCustomersWithPhone(String phone) {
        return userRepository.findProjectionByPhoneContainingAndDeletedAndRole_Name(
                phone,
                false,
                "ROLE_CLIENT",
                AdminCustomerWithNameAndPhoneProjection.class
        );
    }

    @Override
    public User findCustomerById(UUID id) {
        return userRepository.findByIdAndDeletedAndRole_Name(id, false, ROLE_CLIENT)
                             .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
    }

    @Override
    public Page<AdminCustomerResponseDTO> getAllCustomers(
            String search,
            String phone,
            String email,
            UserStatus status,
            Pageable pageable
    ) {
        Specification<User> spec = Specification.where(UserSpecification.hasRole(ROLE_CLIENT))
                                                .and(UserSpecification.hasPhone(phone))
                                                .and(UserSpecification.hasEmail(email))
                                                .and(UserSpecification.hasStatus(status))
                                                .and(UserSpecification.hasSearch(search))
                                                .and(GenericSpecifications.withDeleted(false));
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(user -> modelMapper.map(user, AdminCustomerResponseDTO.class));
    }

    @Override
    public AdminCustomerResponseDTO updateCustomer(UUID id, UpdateCustomerRequestDTO request) {
        User user = findCustomerById(id);
        modelMapper.map(request, user);
        user = userRepository.save(user);
        return modelMapper.map(user, AdminCustomerResponseDTO.class);
    }

    @Override
    public void deleteCustomer(UUID id) {
        User user = findCustomerById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }
}
