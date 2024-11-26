package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminCustomerResponseDTO;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<AdminCustomerWithNameAndPhoneProjection> getCustomersWithPhone(String phone);

    /**
     * Retrieve a customer by ID
     *
     * @param id The ID of the customer
     * @return The customer
     */
    User findCustomerById(UUID id);

    /**
     * Retrieve all customers
     *
     * @param search   The ID or name of the customer
     * @param phone    Phone number
     * @param email    Email address
     * @param status   Status of the customer
     * @param pageable Pagination information (page number, size, sort)
     * @return Page of customers
     */
    Page<AdminCustomerResponseDTO> getAllCustomers(
            String search,
            String phone,
            String email,
            UserStatus status,
            Pageable pageable
    );

    /**
     * Update a customer
     *
     * @param id      The ID of the customer
     * @param request The updated information
     * @return The updated customer
     */
    AdminCustomerResponseDTO updateCustomer(UUID id, UpdateCustomerRequestDTO request);

    /**
     * Delete a customer
     *
     * @param id The ID of the customer
     */
    void deleteCustomer(UUID id);
}
