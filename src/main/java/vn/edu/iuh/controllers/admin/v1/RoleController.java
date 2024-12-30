package vn.edu.iuh.controllers.admin.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.services.RoleService;

import java.util.List;

import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import static vn.edu.iuh.constant.RouterConstant.AdminPaths;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Role.BASE)
@RestController("roleControllerAdminV1")
@Tag(name = "ADMIN V1: Role Management", description = "Quản lý phân quyền")
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = AdminSwagger.Role.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<List<Role>> getAllRoles() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                roleService.getAllRoles()
        );
    }
}
