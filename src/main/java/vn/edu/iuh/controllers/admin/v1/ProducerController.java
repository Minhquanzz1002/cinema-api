package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.constant.RouterConstant.AdminPaths;
import vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import vn.edu.iuh.dto.admin.v1.req.CreateProducerRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Producer;
import vn.edu.iuh.services.ProducerService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Producer.BASE)
@RestController("producerControllerAdminV1")
@Tag(name = "ADMIN V1: Producer Management", description = "Quản lý nhà sản xuất")
public class ProducerController {
    private final ProducerService producerService;

    @Operation(summary = AdminSwagger.Producer.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Producer> createProducer(
            @RequestBody @Valid CreateProducerRequestDTO request
            ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm nhà sản xuất thành công",
                producerService.createProducer(request.getName())
        );
    }
}
