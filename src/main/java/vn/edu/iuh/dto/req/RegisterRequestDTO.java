package vn.edu.iuh.dto.req;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotNull(message = "Tên là bắt buộc")
    @NotBlank(message = "Tên không được để trống")
    private String name;
    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email là bắt buộc")
    private String email;
    @NotNull(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^0(?:3[2-9]|8[12345689]|7[06789]|5[2689]|9[2367890])\\d{7}$", message = "Số điện thoại phải là 10 và các đầu số phải thuộc các nhà mạng Viettel (032, 033, 034, 035, 036, 037, 038, 039, 096, 097, 098, 086), Vinaphone (081, 082, 083, 084, 085, 088), MobiFone (070, 076, 077, 078, 079, 090, 089, 093), Vietnamobile (052, 056, 058, 092) và Gmobile (059, 099)")
    private String phone;
    private boolean gender;
    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate birthday;
    @NotNull(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
}
