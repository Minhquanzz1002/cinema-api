package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.employee.req.CreateEmployeeRequest;
import vn.edu.iuh.dto.admin.v1.req.EmployeeResponseDTO;
import vn.edu.iuh.dto.admin.v1.employee.req.UpdateEmployeeRequest;
import vn.edu.iuh.models.enums.UserStatus;


import java.util.UUID;

public interface EmployeeService {
    Page<EmployeeResponseDTO> getEmployees(String search, UserStatus status, String role, Pageable pageable);

    EmployeeResponseDTO getEmployee(UUID id);

    EmployeeResponseDTO createEmployee(CreateEmployeeRequest request);

    EmployeeResponseDTO updateEmployee(UUID id, UpdateEmployeeRequest employeeUpdateDTO);

    void deleteEmployee(UUID id);

    EmployeeResponseDTO getEmployeeByCode(String code);
}