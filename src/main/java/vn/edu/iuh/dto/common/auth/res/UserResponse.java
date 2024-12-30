package vn.edu.iuh.dto.common.auth.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String code;
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private boolean gender;
    private LocalDate birthday;
    private String role;
}
