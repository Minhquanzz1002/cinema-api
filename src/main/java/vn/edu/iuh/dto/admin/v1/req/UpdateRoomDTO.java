package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import vn.edu.iuh.models.enums.BaseStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoomDTO {
    @NotBlank(message = "Room name cannot be blank")
    private String name;
    private BaseStatus status;
}
