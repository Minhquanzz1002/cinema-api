package vn.edu.iuh.services;

import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Genre;

import java.util.List;

public interface GenreService {
    /**
     * Create a new genre
     *
     * @param name genre name
     * @return genre object
     */
    Genre createGenre(String name);

    List<Genre> getGenres();
}
