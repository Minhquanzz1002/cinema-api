package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivateMultipleShowTimeRequestDTO {
    private int cinemaId;
    private List<Integer> roomIds;
    private List<Integer> movieIds;
    private LocalDate startDate;
}
