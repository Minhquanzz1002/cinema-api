package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.validation.NullOrNotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDTO {
    @NullOrNotBlank(message = "Tên sản phẩm là null hoặc không rỗng")
    private String name;
    @NullOrNotBlank(message = "Mô tả sản phẩm là null hoặc không rỗng")
    private String description;
    private ProductStatus status;
}
