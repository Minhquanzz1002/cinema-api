package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminProductPriceOverviewResponseDTO;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

public interface ProductPriceService {
    Page<AdminProductPriceOverviewResponseDTO> getAllProductPrices(LocalDate startDate, LocalDate endDate, BaseStatus status, String search ,Pageable pageable);

    void deleteProductPrice(int id);

    ProductPrice updateProductPrice(int id, UpdateProductPriceRequestDTO updateProductPriceRequestDTO);
}
