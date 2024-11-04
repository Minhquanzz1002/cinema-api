package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminShowTimeForSaleResponseDTO {
    private UUID id;
    private RoomDTO room;
    private CinemaDTO cinema;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BaseStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CinemaDTO {
        private int id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDTO {
        private int id;
        private String name;
    }
}
