package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoomDTO {
    @NotBlank(message = "Room name cannot be blank")
    private String name;

    @NotNull(message = "Cinema ID cannot be null")
    private Integer cinemaId;
}
