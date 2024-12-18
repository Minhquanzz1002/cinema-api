package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Genre;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.GenreRepository;
import vn.edu.iuh.services.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre createGenre(String name) {
        Genre genre = Genre.builder()
                           .name(name)
                           .status(BaseStatus.ACTIVE)
                           .build();
        return genreRepository.save(genre);
    }

    @Override
    public SuccessResponse<List<?>> getGenres() {
        List<Genre> genres = genreRepository.findAll();
        return new SuccessResponse<>(200, "success", "Thành công", genres);
    }
}
