package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.services.CustomerService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;

    @Override
    public List<AdminCustomerWithNameAndPhoneProjection> getCustomersWithPhone(String phone) {
        return userRepository.findProjectionByPhoneContainingAndDeletedAndRole_Name(phone, false, "ROLE_CLIENT", AdminCustomerWithNameAndPhoneProjection.class);
    }
}
