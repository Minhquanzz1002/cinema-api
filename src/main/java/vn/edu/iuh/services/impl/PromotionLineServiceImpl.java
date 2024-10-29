package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.services.PromotionLineService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionLineServiceImpl implements PromotionLineService {
    private final PromotionLineRepository promotionLineRepository;
    @Override
    public PromotionLine getPromotionLineByCode(String code) {
        return promotionLineRepository.findByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));
    }
}
