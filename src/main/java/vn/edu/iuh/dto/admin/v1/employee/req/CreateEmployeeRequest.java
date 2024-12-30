package vn.edu.iuh.dto.admin.v1.employee.req;

import jakarta.validation.constraints.*;
import lombok.*;
import vn.edu.iuh.models.enums.UserStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự")
    private String name;

    private boolean gender;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[2689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$",
            message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate birthday;

    @NotNull(message = "Trạng thái không được để trống")
    private UserStatus status;

    @NotNull(message = "Vai trò không được để trống")
    private int roleId;

}
