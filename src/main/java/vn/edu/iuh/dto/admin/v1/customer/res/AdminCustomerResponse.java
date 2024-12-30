package vn.edu.iuh.dto.admin.v1.customer.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCustomerResponse {
    private UUID id;
    private String code;
    private String name;
    private boolean gender;
    private String email;
    private String phone;
    private LocalDateTime invalidateBefore = LocalDateTime.now();
    private LocalDate birthday;
    private UserStatus status;
    private String avatar;
}
