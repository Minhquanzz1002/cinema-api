package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Genre;

import java.util.List;

public interface GenreService {
    SuccessResponse<List<?>> getGenres();
}
