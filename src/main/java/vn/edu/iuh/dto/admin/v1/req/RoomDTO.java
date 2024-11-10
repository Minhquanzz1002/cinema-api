package vn.edu.iuh.dto.admin.v1.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {
    private int id;
    private String name;
    private int cinemaId;

}
