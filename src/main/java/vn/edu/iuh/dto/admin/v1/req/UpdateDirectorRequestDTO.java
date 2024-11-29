package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDirectorRequestDTO {
    @NotBlank(message = "Tên đạo diễn không được để trống")
    private String name;
    private LocalDate birthday;
    private String image;
    private String country;
    private String bio;
    private BaseStatus status;
}
