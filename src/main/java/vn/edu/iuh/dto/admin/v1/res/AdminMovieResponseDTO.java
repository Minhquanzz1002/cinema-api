package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMovieResponseDTO {
    private int id;
    private String code;
    private String title;
    private String imageLandscape;
    private String imagePortrait;
    private String trailer;
    private String slug;
    private int duration;
    private String summary;
    private float rating;
    private String country;
    private int age;
    private AgeRating ageRating;
    private LocalDate releaseDate;
    private MovieStatus status;
}
