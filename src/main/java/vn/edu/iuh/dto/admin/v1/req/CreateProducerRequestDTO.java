package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProducerRequestDTO {
    @NotBlank(message = "Tên không được để trống")
    private String name;
}
