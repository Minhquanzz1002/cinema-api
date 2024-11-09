package vn.edu.iuh.dto.common.zalopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZaloPayCallBackResponseDTO {
    @JsonProperty("return_code")
    private int returnCode;
    @JsonProperty("return_message")
    private String returnMessage;
}
