package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;

public interface MovieProjection extends BaseEntityProjection {
    Integer getId();

    String getTitle();

    String getImageLandscape();

    String getImagePortrait();

    String getTrailer();

    String getSlug();

    Integer getDuration();

    String getSummary();

    Float getRating();

    String getCountry();
    AgeRating getAgeRating();
    LocalDate getReleaseDate();

    MovieStatus getStatus();
}
