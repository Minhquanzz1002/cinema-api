package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateProductRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.repositories.ProductPriceRepository;
import vn.edu.iuh.repositories.ProductRepository;
import vn.edu.iuh.services.ProductService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductProjection> getProducts() {
        return productRepository.findAllWithPrice(ProductStatus.ACTIVE, false);
    }

    @Override
    public Page<BaseProductWithPriceProjection> getAllProductsWithCurrentPrice(Pageable pageable, String code, String name, ProductStatus status) {
        if (status == null) {
            return productRepository.findAllWithPriceByCodeContainingAndNameContainingAndDeleted(code, name, false, pageable);
        }
        return productRepository.findAllWithPriceByCodeContainingAndNameContainingAndStatusAndDeleted(code, name, status, false, pageable);
    }

    @Override
    public BaseProductWithPriceProjection getProductWithCurrentPriceByCode(String code) {
        return productRepository.findWithPriceByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));
    }

    @Override
    public Page<ProductPrice> getProductPricesHistory(String code, Pageable pageable) {
        return productPriceRepository.findAllByProduct_CodeAndDeleted(code, false, pageable);
    }

    @Override
    public Product createProduct(CreateProductRequestDTO createProductRequestDTO) {
        String code = createProductRequestDTO.getCode();
        if (code != null && !code.isEmpty()) {
            if (productRepository.existsByCode(code)) {
                throw new BadRequestException("Mã " + code + " đã tồn tại");
            }
        } else {
            code = generateNextProductCode();
        }
        Product product = modelMapper.map(createProductRequestDTO, Product.class);
        product.setCode(code);
        return productRepository.save(product);
    }

    @Override
    public BaseProductProjection updateProduct(String code, UpdateProductRequestDTO updateProductRequestDTO) {
        Product existingProduct = getProductByCode(code);
        if (existingProduct.getStatus() != ProductStatus.ACTIVE) {
            if (updateProductRequestDTO.getName() != null) {
                existingProduct.setName(updateProductRequestDTO.getName());
            }
            if (updateProductRequestDTO.getDescription() != null) {
                existingProduct.setDescription(updateProductRequestDTO.getDescription());
            }
        }
        if (updateProductRequestDTO.getStatus() != null) {
            existingProduct.setStatus(updateProductRequestDTO.getStatus());
        }
        productRepository.save(existingProduct);
        return productRepository.findByCodeAndDeleted(code, false, BaseProductProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));
    }

    @Override
    public Product getProductByCode(String code) {
        return productRepository.findByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));
    }

    @Override
    public void deleteProduct(String code) {
        Product product = getProductByCode(code);
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductPrice createProductPrice(String code, CreateProductPriceRequestDTO createProductPriceRequestDTO) {
        Product product = getProductByCode(code);
        ProductPrice productPrice = modelMapper.map(createProductPriceRequestDTO, ProductPrice.class);
        productPrice.setProduct(product);
        return productPriceRepository.save(productPrice);
    }

    @Override
    public void deleteProductPrice(String code, int id) {
        ProductPrice productPrice = productPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá sản phẩm"));
        if (productPrice.getStatus() != BaseStatus.ACTIVE) {
            productPrice.setDeleted(true);
            productPriceRepository.save(productPrice);
        } else {
            throw new BadRequestException("Không thể xóa giá sản phẩm đang hoạt động");
        }
    }

    @Override
    public ProductPrice updateProductPrice(String code, int id, UpdateProductPriceRequestDTO updateProductPriceRequestDTO) {
        ProductPrice productPrice = productPriceRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy giá sản phẩm"));
        if (productPrice.getStatus() != BaseStatus.ACTIVE) {
            if (updateProductPriceRequestDTO.getPrice() != null) {
                productPrice.setPrice(updateProductPriceRequestDTO.getPrice());
            }
            if (updateProductPriceRequestDTO.getStartDate() != null) {
                productPrice.setStartDate(updateProductPriceRequestDTO.getStartDate());
            }
            if (updateProductPriceRequestDTO.getEndDate() != null) {
                productPrice.setEndDate(updateProductPriceRequestDTO.getEndDate());
            }
            if (updateProductPriceRequestDTO.getStatus() != null) {
                productPrice.setStatus(updateProductPriceRequestDTO.getStatus());
            }
            productPriceRepository.save(productPrice);
            return productPrice;
        } else {
            if (updateProductPriceRequestDTO.getStatus() != null) {
                productPrice.setStatus(updateProductPriceRequestDTO.getStatus());
            }
        }
        return productPriceRepository.save(productPrice);
    }

    private String generateNextProductCode() {
        Optional<Product> lastProduct = productRepository.findTopByOrderByCodeDesc();
        int nextNumber = 1;
        if (lastProduct.isPresent()) {
            String lastCode = lastProduct.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }
        return String.format("CB%06d", nextNumber);
    }
}
