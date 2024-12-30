package vn.edu.iuh.dto.admin.v1.file.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.models.enums.UploadType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUploadFileRequest {
    @NotBlank(message = "Tên file là bắt buộc")
    private String fileName;
    @NotNull(message = "Loại file là bắt buộc")
    private UploadType type;
}
