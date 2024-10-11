package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.models.Director;

public interface DirectorService {
    Page<Director> getAllDirectors(Pageable pageable);
}
