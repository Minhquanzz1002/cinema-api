package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;
import vn.edu.iuh.services.CustomerService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/customers")
@Tag(name = "Movie Controller Admin V1", description = "Quản lý phim")
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/phone/{phone}")
    public SuccessResponse<List<AdminCustomerWithNameAndPhoneProjection>> getCustomersWithPhone(@PathVariable String phone) {
        return new SuccessResponse<>(200, "success", "Thành công", customerService.getCustomersWithPhone(phone));
    }
}
