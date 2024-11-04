package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminProductPriceOverviewResponseDTO;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ProductPriceRepository;
import vn.edu.iuh.services.ProductPriceService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.ProductPriceSpecifications;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<AdminProductPriceOverviewResponseDTO> getAllProductPrices(LocalDate startDate, LocalDate endDate, BaseStatus status, String search, Pageable pageable) {
        Specification<ProductPrice> spec = Specification.where(null);
        spec = spec.and(GenericSpecifications.withDeleted(false));
        spec = spec.and(GenericSpecifications.betweenDates(startDate, endDate));
        spec = spec.and(GenericSpecifications.withStatus(status));
        spec = spec.and(ProductPriceSpecifications.withProductCodeOrName(search));

        Page<ProductPrice> productPrices = productPriceRepository.findAll(spec, pageable);
        return productPrices.map(productPrice -> modelMapper.map(productPrice, AdminProductPriceOverviewResponseDTO.class));
    }
}
