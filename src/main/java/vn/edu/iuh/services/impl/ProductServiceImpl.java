package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.repositories.ProductRepository;
import vn.edu.iuh.services.ProductService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductProjection> getProducts() {
        return productRepository.findAllWithPrice(ProductStatus.ACTIVE, false);
    }

    @Override
    public Page<BaseProductWithPriceProjection> getAllProductsWithCurrentPrice(Pageable pageable) {
        return productRepository.findWithPriceByStatusAndDeleted(ProductStatus.ACTIVE, false, pageable);
    }


}
