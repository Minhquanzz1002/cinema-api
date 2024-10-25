package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.DirectorRepository;
import vn.edu.iuh.services.DirectorService;
import vn.edu.iuh.specifications.DirectorSpecification;
import vn.edu.iuh.specifications.GenericSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public Page<Director> getAllDirectors(String search, BaseStatus status, String country, Pageable pageable) {
        Specification<Director> specification = Specification.where(null);
        specification = specification.and(DirectorSpecification.withNameOrCode(search))
                .and(DirectorSpecification.withStatus(status))
                .and(DirectorSpecification.withCountry(country))
                .and(GenericSpecifications.withDeleted(false));
        return directorRepository.findAll(specification, pageable);
    }
}
