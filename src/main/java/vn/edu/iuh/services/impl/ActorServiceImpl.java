package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateActorRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ActorRepository;
import vn.edu.iuh.services.ActorService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Actor> getAllActors(Pageable pageable, String code, String name) {
        return actorRepository.findAllByStatusAndDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(BaseStatus.ACTIVE, false, code, name, pageable, Actor.class);
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
