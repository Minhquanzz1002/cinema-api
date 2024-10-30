package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderBeforeShowTimeRequestDTO {
    @NotBlank(message = "Lý do hủy không được để trống")
    private String reason;
}
