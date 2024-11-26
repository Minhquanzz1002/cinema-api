package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.constant.RouterConstant.AdminPaths;
import vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminCustomerResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.projections.admin.v1.AdminCustomerWithNameAndPhoneProjection;
import vn.edu.iuh.services.CustomerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Customer.BASE)
@Tag(name = "ADMIN V1: Customer Management", description = "Quản lý khách hàng")
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/phone/{phone}")
    public SuccessResponse<List<AdminCustomerWithNameAndPhoneProjection>> getCustomersWithPhone(@PathVariable String phone) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                customerService.getCustomersWithPhone(phone)
        );
    }

    @Operation(summary = AdminSwagger.Customer.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<AdminCustomerResponseDTO>> getAllCustomers(
            @PageableDefault Pageable pageable,
            @Parameter(description = "Tên hoặc ID khách hàng")
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String phone,
            @RequestParam(required = false, defaultValue = "") String email,
            @RequestParam(required = false) UserStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                customerService.getAllCustomers(search, phone, email, status, pageable)
        );
    }

    @Operation(summary = AdminSwagger.Customer.UPDATE_SUM)
    @PutMapping(AdminPaths.Customer.UPDATE)
    public SuccessResponse<AdminCustomerResponseDTO> updateCustomer(
            @Parameter(description = "ID khách hàng")
            @PathVariable UUID id,
            @RequestBody @Valid UpdateCustomerRequestDTO request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật khách hàng thành công",
                customerService.updateCustomer(id, request)
        );
    }

    @Operation(summary = AdminSwagger.Customer.DELETE_SUM)
    @DeleteMapping(AdminPaths.Customer.DELETE)
    public SuccessResponse<Void> deleteCustomer(
            @Parameter(description = "ID khách hàng")
            @PathVariable UUID id
    ) {
        customerService.deleteCustomer(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa khách hàng thành công",
                null
        );
    }
}
