package vn.edu.iuh.dto.common.zalopay.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderZaloPayResponseDTO {
    private Status status;
    private String message;
    private String zpTransId;

    public enum Status {
        SUCCESS, PENDING, FAILED
    }
}