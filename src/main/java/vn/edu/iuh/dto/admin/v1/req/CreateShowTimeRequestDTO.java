package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowTimeRequestDTO {
    private int movieId;
    private int cinemaId;
    private int roomId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;
    private BaseStatus status;
}
