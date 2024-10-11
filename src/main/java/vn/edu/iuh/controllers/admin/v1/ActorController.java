package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.services.ActorService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/actors")
@RestController("actorControllerAdminV1")
@Tag(name = "Actor Controller Admin V1", description = "Quản lý thể loại")
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public SuccessResponse<Page<Actor>> getActors(@PageableDefault(sort = "name") Pageable pageable) {
        Page<Actor> actorPage = actorService.getAllActors(pageable);
        return new SuccessResponse<>(200, "success", "Thành công", actorPage);
    }

}
