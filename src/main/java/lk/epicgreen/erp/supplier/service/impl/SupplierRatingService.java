package lk.epicgreen.erp.supplier.service.impl;


import lk.epicgreen.erp.supplier.dto.response.SupplierRatingDTO;
import lk.epicgreen.erp.supplier.entity.Supplier;
import lk.epicgreen.erp.supplier.entity.SupplierRating;
import lk.epicgreen.erp.supplier.repository.SupplierRatingRepository;
import lk.epicgreen.erp.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierRatingService {
    private final SupplierRatingRepository repository;
    private final SupplierRepository supplierRepository;

    public SupplierRatingDTO create(SupplierRatingDTO dto, String username) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
        SupplierRating entity = new SupplierRating();
        entity.setSupplier(supplier);
        entity.setQualityRating(dto.getQualityRating());
        entity.setDeliveryRating(dto.getDeliveryRating());
        entity.setPriceRating(dto.getPriceRating());
        entity.setServiceRating(dto.getServiceRating());
        entity.setOverallRating(dto.getOverallRating());
        entity.setOnTimeDeliveryPercentage(dto.getOnTimeDeliveryPercentage());
        entity.setQualityRejectionPercentage(dto.getQualityRejectionPercentage());
        entity.setAverageLeadTimeDays(dto.getAverageLeadTimeDays());
        entity.setIsPreferredSupplier(dto.getIsPreferredSupplier());
        entity.setComments(dto.getComments());
        entity.setRatingDate(dto.getRatingDate());
        entity.setRatedBy(dto.getRatedBy());
        entity.setCreatedBy(username);
        entity.setUpdatedBy(username);
        return toDTO(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public SupplierRatingDTO getById(Long id) {
        return repository.findById(id).map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Rating not found"));
    }

    @Transactional(readOnly = true)
    public List<SupplierRatingDTO> getBySupplierId(Long supplierId) {
        return repository.findBySupplierId(supplierId).stream()
            .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierRatingDTO getLatestBySupplierId(Long supplierId) {
        return repository.findTopBySupplierIdOrderByRatingDateDesc(supplierId)
            .map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SupplierRatingDTO> getPreferredSuppliers() {
        return repository.findByIsPreferredSupplier(true).stream()
            .map(this::toDTO).collect(Collectors.toList());
    }

    private SupplierRatingDTO toDTO(SupplierRating entity) {
        SupplierRatingDTO dto = new SupplierRatingDTO();
        dto.setId(entity.getId());
        dto.setSupplierId(entity.getSupplier().getId());
        dto.setSupplierName(entity.getSupplier().getSupplierName());
        dto.setQualityRating(entity.getQualityRating());
        dto.setDeliveryRating(entity.getDeliveryRating());
        dto.setPriceRating(entity.getPriceRating());
        dto.setServiceRating(entity.getServiceRating());
        dto.setOverallRating(entity.getOverallRating());
        dto.setOnTimeDeliveryPercentage(entity.getOnTimeDeliveryPercentage());
        dto.setQualityRejectionPercentage(entity.getQualityRejectionPercentage());
        dto.setAverageLeadTimeDays(entity.getAverageLeadTimeDays());
        dto.setIsPreferredSupplier(entity.getIsPreferredSupplier());
        dto.setComments(entity.getComments());
        dto.setRatingDate(entity.getRatingDate());
        dto.setRatedBy(entity.getRatedBy());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
