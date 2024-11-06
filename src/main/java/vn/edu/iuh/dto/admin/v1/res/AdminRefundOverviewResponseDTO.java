package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.RefundMethod;
import vn.edu.iuh.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRefundOverviewResponseDTO {
    private UUID id;
    private String code;
    private String reason;
    private RefundMethod refundMethod;
    private float amount;
    private RefundStatus status;
    private LocalDateTime refundDate;
    private OrderDTO order;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDTO {
        private UUID id;
        private String code;
    }
}
