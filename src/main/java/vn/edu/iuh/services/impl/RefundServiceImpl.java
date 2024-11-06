package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminRefundOverviewResponseDTO;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Refund;
import vn.edu.iuh.projections.admin.v1.AdminRefundProjection;
import vn.edu.iuh.repositories.RefundRepository;
import vn.edu.iuh.services.RefundService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.RefundSpecifications;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<AdminRefundOverviewResponseDTO> getAllRefunds(String refundCode, String orderCode, Pageable pageable) {
        Specification<Refund> spec = Specification.where(null);
        spec = spec.and(GenericSpecifications.withDeleted(false));
        spec = spec.and(RefundSpecifications.withRefundCode(refundCode));
        spec = spec.and(RefundSpecifications.withOrderCode(orderCode));
        Page<Refund> refunds = refundRepository.findAll(spec, pageable);
        return refunds.map(refund -> modelMapper.map(refund, AdminRefundOverviewResponseDTO.class));
    }

    @Override
    public AdminRefundProjection getRefundByCode(String code) {
        return refundRepository.findProjectionByCodeAndDeleted(code, false, AdminRefundProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy hoàn đơn"));
    }
}
