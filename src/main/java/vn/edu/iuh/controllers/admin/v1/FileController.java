package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.RouterConstant.AdminPaths;
import vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import vn.edu.iuh.dto.admin.v1.req.AdminUploadFileRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.impl.S3Service;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.File.BASE)
@RestController
@Tag(name = "ADMIN V1: File Management", description = "Quản lý file")
public class FileController {
    private final S3Service s3Service;

    @PostMapping
    @Operation(summary = AdminSwagger.File.GET_PRESIGNED_URL_SUM)
    public SuccessResponse<String> getPresignedUrl(
            @RequestBody @Valid AdminUploadFileRequestDTO request,
            @AuthenticationPrincipal UserPrincipal principal
            ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                s3Service.getPresignedUrl(request.getFileName(), request.getType(), principal.getId())
        );
    }
}
