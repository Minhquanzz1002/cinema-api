package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.services.PromotionService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/promotions")
@RestController("promotionControllerAdminV1")
@Tag(name = "Promotion Controller Admin V1", description = "Quản lý khuyến mãi")
public class PromotionController {
    private final PromotionService promotionService;


}
