package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMovieReportResponseDTO {
    private String movieCode;
    private String movieTitle;
    private LocalDate date;
    private int totalShows;
    private int totalTickets;
    private float totalPrice;
}
