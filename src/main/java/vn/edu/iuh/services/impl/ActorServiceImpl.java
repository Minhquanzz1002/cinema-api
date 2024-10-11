package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ActorRepository;
import vn.edu.iuh.services.ActorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Override
    public Page<Actor> getAllActors(Pageable pageable) {
        return actorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, pageable, Actor.class);
    }
}
