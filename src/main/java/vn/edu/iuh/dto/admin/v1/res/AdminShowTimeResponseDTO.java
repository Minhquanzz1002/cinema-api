package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.admin.v1.AdminRoomNameOnlyProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminShowTimeResponseDTO {
    List<ShowTimeDTO> showTimes;
    List<AdminRoomNameOnlyProjection> rooms;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShowTimeDTO {
        private UUID id;
        private String movieTitle;
        private String roomName;
        private String cinemaName;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private BaseStatus status;
    }
}
