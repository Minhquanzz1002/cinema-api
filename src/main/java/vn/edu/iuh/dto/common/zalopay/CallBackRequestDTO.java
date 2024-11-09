package vn.edu.iuh.dto.common.zalopay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallBackRequestDTO {
    private String data;
    private String mac;
}
