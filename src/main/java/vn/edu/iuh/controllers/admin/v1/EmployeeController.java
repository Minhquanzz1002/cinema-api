package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateEmployeeDTO;
import vn.edu.iuh.dto.admin.v1.req.EmployeeResponseDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateEmployeeDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.services.EmployeeService;
import vn.edu.iuh.specifications.UserSpecification;

import java.util.UUID;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_EMPLOYEE_BASE_PATH;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_EMPLOYEE_BASE_PATH)
@RestController
@Tag(name = "Employee Controller Admin V1", description = "Quản lý nhân viên")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = GET_ALL_ADMIN_EMPLOYEE_SUM)
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<Page<EmployeeResponseDTO>> getEmployees(
            @RequestParam(required = false) UserStatus status,
            @Parameter(description = "Tìm kiếm theo ID hoặc tên")
            @RequestParam(required = false) String search,
            @PageableDefault Pageable pageable) {
        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(search, status, pageable);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy danh sách nhân viên thành công",
                employees
        );
    }

    @Operation(summary = GET_ADMIN_EMPLOYEE_SUM)
    @GetMapping("/{code}")
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<EmployeeResponseDTO> getEmployee(
            @Parameter(description = "Mã nhân viên") @PathVariable String code) {
        EmployeeResponseDTO employee = employeeService.getEmployeeByCode(code);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy thông tin nhân viên thành công",
                employee
        );
    }

    @Operation(summary = POST_ADMIN_EMPLOYEE_SUM)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Created successfully")
    public SuccessResponse<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody CreateEmployeeDTO dto) {
        EmployeeResponseDTO newEmployee = employeeService.createEmployee(dto);
        return new SuccessResponse<>(
                HttpStatus.CREATED.value(),
                "success",
                "Thêm nhân viên thành công",
                newEmployee
        );
    }

    @Operation(summary = PUT_ADMIN_EMPLOYEE_SUM)
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Updated successfully")
    public SuccessResponse<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "ID nhân viên") @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployeeDTO dto) {
        EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, dto);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Cập nhật thông tin nhân viên thành công",
                updatedEmployee
        );
    }

    @Operation(summary = DELETE_ADMIN_EMPLOYEE_SUM)
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    public SuccessResponse<Void> deleteEmployee(
            @Parameter(description = "ID nhân viên") @PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Xóa nhân viên thành công",
                null
        );
    }
}
