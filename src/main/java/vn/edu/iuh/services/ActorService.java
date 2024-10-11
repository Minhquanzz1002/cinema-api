package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.models.Actor;

import java.util.List;

public interface ActorService {
    Page<Actor> getAllActors(Pageable pageable);
}
