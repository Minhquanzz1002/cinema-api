package vn.edu.iuh.dto.admin.v1.showtime.req;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateShowTimeRequest {
    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @FutureOrPresent(message = "Thời gian bắt đầu phải lớn hơn hoặc bằng thời gian hiện tại")
    private LocalDate startDate;
    @NotNull(message = "Thời gian kết thúc không được để trống")
    @FutureOrPresent(message = "Thời gian kết thúc phải lớn hơn hoặc bằng thời gian hiện tại")
    private LocalDate endDate;
    @NotNull(message = "ID rạp chiếu không được để trống")
    private int cinemaId;
    private List<MovieDTO> movies;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieDTO {
        int id;
        @Min(value = 1, message = "Số suất chiếu không được nhỏ hơn 1")
        int totalShowTimes;
    }
}


