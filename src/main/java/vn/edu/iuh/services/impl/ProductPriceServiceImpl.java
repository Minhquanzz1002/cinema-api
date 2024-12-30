package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.product.price.req.CreateProductPriceRequest;
import vn.edu.iuh.dto.admin.v1.product.price.req.UpdateProductPriceRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminProductPriceOverviewResponseDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.ProductPriceRepository;
import vn.edu.iuh.repositories.ProductRepository;
import vn.edu.iuh.services.ProductPriceService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.ProductPriceSpecifications;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
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

    @Override
    public void deleteProductPrice(int id) {
        ProductPrice productPrice = productPriceRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá sản phẩm"));
        if (productPrice.getStatus() == BaseStatus.ACTIVE) {
            throw new RuntimeException("Không thể xóa bảng giá đang hoạt động");
        }
        productPrice.setDeleted(true);
        productPriceRepository.save(productPrice);
    }

    @Override
    public ProductPrice updateProductPrice(int id, UpdateProductPriceRequest request) {
        ProductPrice productPrice = productPriceRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá sản phẩm"));
        if (productPrice.getStatus() == BaseStatus.ACTIVE) {
            LocalDate newEndDate = request.getEndDate();
            if (newEndDate.isBefore(productPrice.getStartDate())) {
                throw new BadRequestException("Ngày kết thúc phải sau ngày bắt đầu");
            }
            productPrice.setEndDate(newEndDate);
        } else {
            BaseStatus newStatus = request.getStatus();
            if (newStatus == BaseStatus.ACTIVE) {
                boolean hasOverlap = productPriceRepository.existsByProductAndDeletedAndStatusAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        productPrice.getProduct(),
                        false,
                        BaseStatus.ACTIVE,
                        id,
                        request.getEndDate(),
                        request.getStartDate()
                );

                if (hasOverlap) {
                    throw new BadRequestException("Đã tồn tại bảng giá trong khoảng thời gian này");
                }

            }
            modelMapper.map(request, productPrice);
        }
        return productPriceRepository.save(productPrice);
    }

    @Transactional
    @Override
    public void createProductPrice(CreateProductPriceRequest request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        request.getProducts().stream().map(p -> {
            Product product = productRepository.findByIdAndDeleted(p.getId(), false)
                    .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));
            return ProductPrice.builder()
                    .product(product)
                    .price(p.getPrice())
                    .startDate(startDate)
                    .endDate(endDate)
                    .status(BaseStatus.INACTIVE)
                    .build();
        }).forEach(productPriceRepository::save);
    }
}
