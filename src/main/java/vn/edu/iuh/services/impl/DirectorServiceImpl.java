package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.DirectorRepository;
import vn.edu.iuh.services.DirectorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public Page<Director> getAllDirectors(Pageable pageable) {
        return directorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, pageable, Director.class);
    }
}
