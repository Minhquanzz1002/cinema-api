package vn.edu.iuh.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String name;
    private String avatar;
    private String email;
    private String phone;
    private boolean gender;
    private LocalDate birthday;
    private String role;
}
