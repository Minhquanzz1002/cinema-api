package vn.edu.iuh.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
    private int code;
    private String status;
    private String message;
    private T errors;
}
