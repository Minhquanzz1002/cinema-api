package vn.edu.iuh.services;

import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;

import java.util.List;

public interface CustomerService {
    List<AdminCustomerWithNameAndPhoneProjection> getCustomersWithPhone(String phone);
}
