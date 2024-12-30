package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.director.req.CreateDirectorRequest;
import vn.edu.iuh.dto.admin.v1.director.req.UpdateDirectorRequest;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.DirectorRepository;
import vn.edu.iuh.services.DirectorService;
import vn.edu.iuh.specifications.DirectorSpecification;
import vn.edu.iuh.specifications.GenericSpecifications;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;
    private final ModelMapper modelMapper;

    @Override
    public Director createDirector(CreateDirectorRequest request) {
        Director director = modelMapper.map(request, Director.class);
        director.setCode(generateNextDirectorCode());
        return directorRepository.save(director);
    }

    @Override
    public Director findById(int id) {
        return directorRepository.findByIdAndDeleted(id, false)
                                 .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đạo diễn"));
    }

    @Override
    public Director findByCode(String code) {
        return directorRepository.findByCodeAndDeleted(code, false)
                                 .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đạo diễn"));
    }

    @Override
    public Page<Director> getAllDirectors(String search, BaseStatus status, String country, Pageable pageable) {
        Specification<Director> specification = Specification.where(null);
        specification = specification.and(DirectorSpecification.withNameOrCode(search))
                                     .and(DirectorSpecification.withStatus(status))
                                     .and(DirectorSpecification.withCountry(country))
                                     .and(GenericSpecifications.withDeleted(false));
        return directorRepository.findAll(specification, pageable);
    }

    @Override
    public Director updateDirector(int id, UpdateDirectorRequest request) {
        Director director = findById(id);
        modelMapper.map(request, director);
        return directorRepository.save(director);
    }

    @Override
    public void deleteDirector(int id) {
        Director director = findById(id);
        director.setDeleted(true);
        directorRepository.save(director);
    }

    private String generateNextDirectorCode() {
        Optional<Director> lastDirector = directorRepository.findTopByOrderByCodeDesc();
        int nextNumber = 1;
        if (lastDirector.isPresent()) {
            String lastCode = lastDirector.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }

        String candidateCode;
        do {
            candidateCode = String.format("DD%06d", nextNumber);
            if (!directorRepository.existsByCodeAndDeleted(candidateCode, false)) {
                return candidateCode;
            }
            nextNumber++;
        } while (nextNumber <= 999999);

        throw new BadRequestException("Không thể tạo mã mới");
    }
}
