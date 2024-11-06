package vn.edu.iuh.dto.admin.v1.req;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequestDTO {
    private String code;
    @NotBlank(message = "Tên phim không được để trống")
    private String title;
    private String imageLandscape;
    @URL(message = "Ảnh chính không đúng định dạng URL")
    private String imagePortrait;
    @URL(message = "Trailer không đúng định dạng URL")
    private String trailer;
    @Min(value = 1, message = "Thời lượng phim phải lớn hơn 0")
    private int duration;
    private String summary;
    private String country;
    @NotNull(message = "Độ tuổi không được để trống")
    private AgeRating ageRating;
    private LocalDate releaseDate;
    private MovieStatus status;
    private List<Integer> producers;
    @NotEmpty(message = "Danh sách thể loại không được để trống")
    @Size(min = 1, message = "Phải có ít nhất 1 thể loại")
    private List<Integer> genres;
    private List<Integer> directors;
    private List<Integer> actors;
}
