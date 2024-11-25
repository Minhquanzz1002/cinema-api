package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromotionResponseDTO {
    private int id;
    private String code;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BaseStatus status;
}
