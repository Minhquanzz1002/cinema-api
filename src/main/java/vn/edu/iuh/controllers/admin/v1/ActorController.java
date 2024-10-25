package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateActorRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ActorService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/actors")
@RestController("actorControllerAdminV1")
@Tag(name = "Actor Controller Admin V1", description = "Quản lý thể loại")
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public SuccessResponse<Page<Actor>> getActors(@PageableDefault(sort = "code") Pageable pageable,
                                                  @RequestParam(defaultValue = "", required = false) String search,
                                                  @RequestParam(required = false, defaultValue = "") String country,
                                                  @RequestParam(required = false) BaseStatus status) {
        Page<Actor> actorPage = actorService.getAllActors(search, status, country, pageable);
        return new SuccessResponse<>(200, "success", "Thành công", actorPage);
    }

    @PostMapping
    public SuccessResponse<Actor> createActor(@RequestBody @Valid CreateActorRequestDTO createActorRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", actorService.createActor(createActorRequestDTO));
    }

}
