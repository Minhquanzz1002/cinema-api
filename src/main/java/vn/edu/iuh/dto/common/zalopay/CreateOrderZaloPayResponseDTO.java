package vn.edu.iuh.dto.common.zalopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderZaloPayResponseDTO {
    private String qrUrl;
    private String transId;
    private String orderUrl;
}
