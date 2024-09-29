package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductRequestDTO {
    @NotNull(message = "ID không được rỗng")
    private Integer id;
    @Min(value = 1, message = "Số lượng tối thiểu phải lớn hơn 0")
    private int quantity;
}
