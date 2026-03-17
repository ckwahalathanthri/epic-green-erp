package lk.epicgreen.erp.product.service;


import lk.epicgreen.erp.product.dto.response.ProductListDTO;
import lk.epicgreen.erp.product.dto.response.ProductSearchDTO;
import lk.epicgreen.erp.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductSearchService {
    Page<ProductListDTO> advancedSearch(ProductSearchDTO searchDTO, Pageable pageable);
}
