package vn.edu.iuh.projections;

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

    Integer getAge();

    LocalDate getReleaseDate();

    MovieStatus getStatus();
}
