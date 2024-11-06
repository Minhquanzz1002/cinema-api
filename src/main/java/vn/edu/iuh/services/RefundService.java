package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.res.AdminRefundOverviewResponseDTO;
import vn.edu.iuh.projections.admin.v1.AdminRefundProjection;

import java.time.LocalDate;

public interface RefundService {
    Page<AdminRefundOverviewResponseDTO> getAllRefunds(String refundCode, String orderCode, Pageable pageable);

    AdminRefundProjection getRefundByCode(String code);
}
