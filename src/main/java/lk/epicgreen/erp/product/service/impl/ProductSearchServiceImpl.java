package lk.epicgreen.erp.product.service.impl;

import lk.epicgreen.erp.product.dto.response.ProductListDTO;
import lk.epicgreen.erp.product.dto.response.ProductSearchDTO;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.mapper.ProductMapper;
import lk.epicgreen.erp.product.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSearchServiceImpl implements ProductSearchService {
    
    private final EntityManager entityManager;
    private final ProductMapper productMapper;
    
    @Override
    public Page<ProductListDTO> advancedSearch(ProductSearchDTO searchDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        // Keyword search
        if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().isEmpty()) {
            String keyword = "%" + searchDTO.getKeyword().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(product.get("productName")), keyword),
                cb.like(cb.lower(product.get("productCode")), keyword)
            ));
        }
        
        // Category filter
        if (searchDTO.getCategoryId() != null) {
            predicates.add(cb.equal(product.get("category").get("id"), searchDTO.getCategoryId()));
        }
        
        // Active status
        if (searchDTO.getIsActive() != null) {
            predicates.add(cb.equal(product.get("isActive"), searchDTO.getIsActive()));
        }
        
        // Price range
        if (searchDTO.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("sellingPrice"), searchDTO.getMinPrice()));
        }
        if (searchDTO.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("sellingPrice"), searchDTO.getMaxPrice()));
        }
        
        query.where(predicates.toArray(new Predicate[0]));
        
        // Get results
        List<Product> results = entityManager.createQuery(query)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
        
        List<ProductListDTO> dtos = results.stream()
            .map(productMapper::toListDTO)
            .collect(Collectors.toList());
        
        // Count total
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> countRoot = countQuery.from(Product.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(dtos, pageable, total);
    }
}
