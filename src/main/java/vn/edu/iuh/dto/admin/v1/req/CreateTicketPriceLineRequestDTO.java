package vn.edu.iuh.dto.admin.v1.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.DayType;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketPriceLineRequestDTO {
    private List<DayType> applyForDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private BaseStatus status;
    private float normalPrice;
    private float vipPrice;
    private float couplePrice;
}
