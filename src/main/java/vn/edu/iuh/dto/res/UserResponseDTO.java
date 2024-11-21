package vn.edu.iuh.dto.res;

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
public class UserResponseDTO {
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
