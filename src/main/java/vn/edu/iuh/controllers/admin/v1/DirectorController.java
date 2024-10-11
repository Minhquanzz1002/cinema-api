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
import vn.edu.iuh.models.Director;
import vn.edu.iuh.services.DirectorService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/directors")
@RestController("directorControllerAdminV1")
@Tag(name = "Director Controller Admin V1", description = "Quản lý đạo diễn")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public SuccessResponse<Page<Director>> getDirectors(@PageableDefault(sort = "name") Pageable pageable) {
        Page<Director> directorPage = directorService.getAllDirectors(pageable);
        return new SuccessResponse<>(200, "success", "Thành công", directorPage);
    }
}
