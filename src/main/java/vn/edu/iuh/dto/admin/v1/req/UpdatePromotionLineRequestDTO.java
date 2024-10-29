package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePromotionLineRequestDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private BaseStatus status;
    private List<PromotionDetailDTO> promotionDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionDetailDTO {
        private Float discountValue;
        private Float maxDiscountValue;
        private Float minOrderValue;
        private int usageLimit;

    }
}
