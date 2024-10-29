package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;

import java.util.List;

public interface GenreService {
    SuccessResponse<List<?>> getGenres();
}
