package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;

public interface DirectorService {
    Page<Director> getAllDirectors(String search, BaseStatus status, String country, Pageable pageable);
}
