package vn.edu.iuh.dto.res;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDTO extends UserResponseDTO {
    private String accessToken;
    private String refreshToken;
}
