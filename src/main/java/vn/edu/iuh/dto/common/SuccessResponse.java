package vn.edu.iuh.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse<T> {
    private int code;
    private String status;
    private String message;
    private T data;
}
