package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.*;
import lombok.*;
import vn.edu.iuh.models.enums.UserStatus;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private UUID id;
    private String code;
    private String name;
    private boolean gender;
    private String email;
    private String phone;
    private LocalDate birthday;
    private UserStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
