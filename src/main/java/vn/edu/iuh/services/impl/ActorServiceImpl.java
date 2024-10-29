package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateActorRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateActorRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ActorRepository;
import vn.edu.iuh.services.ActorService;
import vn.edu.iuh.specifications.ActorSpecification;
import vn.edu.iuh.specifications.GenericSpecifications;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;
    private final ModelMapper modelMapper;

    /**
     * Get actor by id and not deleted
     *
     * @param id actor id
     * @return actor
     */
    @Override
    public Actor getActorById(int id) {
        return actorRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy diễn viên"));
    }

    @Override
    public Actor getActorByCode(String code) {
        return actorRepository.findByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy diễn viên"));
    }

    @Override
    public Page<Actor> getAllActors(String search, BaseStatus status, String country, Pageable pageable) {
        Specification<Actor> specification = Specification.where(null);
        specification = specification.and(ActorSpecification.withNameOrCode(search))
                .and(ActorSpecification.withStatus(status))
                .and(ActorSpecification.withCountry(country))
                .and(GenericSpecifications.withDeleted(false));
        return actorRepository.findAll(specification, pageable);
    }

    @Override
    public Actor createActor(CreateActorRequestDTO createActorRequestDTO) {
        String code = createActorRequestDTO.getCode();
        if (code != null && !code.isEmpty()) {
            Optional<Actor> actorOptional = actorRepository.findByCode(code);
            if (actorOptional.isPresent()) {
                throw new BadRequestException("Mã " + code + " đã tồn tại");
            }
        }

        Actor actor = modelMapper.map(createActorRequestDTO, Actor.class);
        log.info("actor: {}", actor);
        String newCode = generateNextActorCode();
        actor.setCode(newCode);
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(int id) {
        Actor actor = getActorById(id);
        if (actor.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa diễn viên đang hoạt động");
        }
        actor.setDeleted(true);
        actorRepository.save(actor);
    }

    @Override
    public Actor updateActor(int id, UpdateActorRequestDTO updateActorRequestDTO) {
        Actor existingActor = getActorById(id);
        modelMapper.map(updateActorRequestDTO, existingActor);
        return actorRepository.save(existingActor);
    }

    private String generateNextActorCode() {
        Optional<Actor> lastActor = actorRepository.findTopByOrderByCodeDesc();
        int nextNumber = 1;
        if (lastActor.isPresent()) {
            String lastCode = lastActor.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }
        return String.format("DV%06d", nextNumber);
    }
}
