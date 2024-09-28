package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.OrderDetailType;
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
    public SuccessResponse<?> getProducts() {
        List<ProductProjection> products = productRepository.findAllWithPrice(ProductStatus.ACTIVE, false);
        return new SuccessResponse<>(200, "success", "Thành công", products);
    }
}
