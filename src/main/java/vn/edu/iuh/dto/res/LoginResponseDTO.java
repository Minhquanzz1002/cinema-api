package vn.edu.iuh.dto.res;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String name;
    private String email;
    private String phone;
    private boolean gender;
    private LocalDate birthday;
    private String role;
}
