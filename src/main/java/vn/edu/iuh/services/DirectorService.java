package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.director.req.CreateDirectorRequest;
import vn.edu.iuh.dto.admin.v1.director.req.UpdateDirectorRequest;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;

public interface DirectorService {
    /**
     * Create a new director
     *
     * @param request DTO contains director information
     * @return Director
     */
    Director createDirector(CreateDirectorRequest request);

    /**
     * Find a director by id
     *
     * @param id director id
     * @return Director
     */
    Director findById(int id);

    /**
     * Find a director by code
     *
     * @param code director code
     * @return Director
     */
    Director findByCode(String code);

    Page<Director> getAllDirectors(String search, BaseStatus status, String country, Pageable pageable);

    /**
     * Update a director
     *
     * @param id      director id
     * @param request DTO contains director information
     * @return Director
     */
    Director updateDirector(int id, UpdateDirectorRequest request);

    /**
     * Delete a director
     *
     * @param id director id
     */
    void deleteDirector(int id);
}
