package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.services.EmployeeService;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_EMPLOYEE_BASE_PATH;

/**
 * TODO: ROLE_EMPLOYEE_SALE, code: NVBH + 8 số
 * API lấy danh sách nhân viên có phân trang và lọc theo ID và name (dùng chung ?search)
 * API lấy thông tin chi tiết nhân viên theo ID
 * API thêm mới nhân viên
 * API cập nhật thông tin nhân viên
 * API xóa nhân viên
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_EMPLOYEE_BASE_PATH)
@RestController
@Tag(name = "Employee Controller Admin V1", description = "Quản lý nhân viên")
public class EmployeeController {
    private final EmployeeService employeeService;

}
