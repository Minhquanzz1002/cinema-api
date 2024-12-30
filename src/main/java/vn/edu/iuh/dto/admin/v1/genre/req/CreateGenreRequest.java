package vn.edu.iuh.dto.admin.v1.genre.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenreRequest {
    @NotBlank(message = "Tên thể loại không được để trống")
    private String name;
}
