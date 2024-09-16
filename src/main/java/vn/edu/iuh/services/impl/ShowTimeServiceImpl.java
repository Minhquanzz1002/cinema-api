package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.ShowTimeProjection;
import vn.edu.iuh.repositories.ShowTimeRepository;
import vn.edu.iuh.services.ShowTimeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;

    @Override
    public SuccessResponse<List<ShowTimeProjection>> getShowTimes() {
        List<ShowTimeProjection> showTimes = showTimeRepository.findAllProjectionBy(ShowTimeProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", showTimes);
    }
}
