package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.actor.req.CreateActorRequest;
import vn.edu.iuh.dto.admin.v1.actor.req.UpdateActorRequest;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;

public interface ActorService {
    Actor getActorById(int id);
    Actor getActorByCode(String code);
    Page<Actor> getAllActors(String search, BaseStatus status, String country, Pageable pageable);

    Actor createActor(CreateActorRequest request);
    void deleteActor(int id);
    Actor updateActor(int id, UpdateActorRequest request);
}
