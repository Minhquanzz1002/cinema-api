package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductPriceRequestDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private float price;
    private BaseStatus status;
}
