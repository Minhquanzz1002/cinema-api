package vn.edu.iuh.dto.common.auth.res;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse extends UserResponse {
    private String accessToken;
    private String refreshToken;
}
