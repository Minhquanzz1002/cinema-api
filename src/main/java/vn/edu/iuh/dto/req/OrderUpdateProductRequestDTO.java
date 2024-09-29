package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateProductRequestDTO {
    @NotEmpty(message = "Danh sách sản phẩm không được rỗng")
    private List<OrderProductRequestDTO> products;
}
