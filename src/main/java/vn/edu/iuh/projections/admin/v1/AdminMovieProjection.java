package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;

public interface AdminMovieProjection {
    Integer getId();
    String getCode();

    String getTitle();

    String getImageLandscape();

    String getImagePortrait();

    String getTrailer();

    String getSlug();

    Integer getDuration();

    String getSummary();

    Float getRating();
    AgeRating getAgeRating();

    String getCountry();

    Integer getAge();

    LocalDate getReleaseDate();

    MovieStatus getStatus();
}
