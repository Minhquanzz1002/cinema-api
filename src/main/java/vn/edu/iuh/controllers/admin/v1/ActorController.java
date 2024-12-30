package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.actor.req.CreateActorRequest;
import vn.edu.iuh.dto.admin.v1.actor.req.UpdateActorRequest;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ActorService;

import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import static vn.edu.iuh.constant.RouterConstant.AdminPaths;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Actor.BASE)
@RestController("actorControllerAdminV1")
@Tag(name = "ADMIN V1: Actor Management", description = "Quản lý diễn viên")
public class ActorController {
    private final ActorService actorService;

    @Operation(summary = AdminSwagger.Actor.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<Actor>> getActors(
            @PageableDefault(sort = "code") Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false, defaultValue = "") String country,
            @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                actorService.getAllActors(search, status, country, pageable)
        );
    }

    @Operation(summary = AdminSwagger.Actor.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Actor> createActor(@RequestBody @Valid CreateActorRequest request) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thành công",
                actorService.createActor(request)
        );
    }

    @Operation(summary = AdminSwagger.Actor.GET_SUM)
    @GetMapping(AdminPaths.Actor.DETAIL)
    public SuccessResponse<Actor> getActor(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                actorService.getActorByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Actor.UPDATE_SUM)
    @PutMapping(AdminPaths.Actor.UPDATE)
    public SuccessResponse<Actor> updateActor(
            @PathVariable int id,
            @RequestBody @Valid UpdateActorRequest request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật diễn viên thành công",
                actorService.updateActor(id, request)
        );
    }

    @Operation(summary = AdminSwagger.Actor.DELETE_SUM)
    @DeleteMapping(AdminPaths.Actor.DELETE)
    public SuccessResponse<Void> deleteActor(@PathVariable int id) {
        actorService.deleteActor(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa diễn viên thành công",
                null
        );
    }
}
