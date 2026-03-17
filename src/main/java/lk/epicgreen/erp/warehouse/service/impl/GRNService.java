package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.GRNDTO;
import lk.epicgreen.erp.warehouse.entity.GoodsReceiptNote;
import lk.epicgreen.erp.warehouse.repository.GoodsReceiptNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GRNService {
    private final GoodsReceiptNoteRepository grnRepository;
    
    public List<GRNDTO> getAll() {
        return grnRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public GRNDTO getById(Long id) {
        return grnRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
    }
    
    public GRNDTO create(GRNDTO dto) {
        GoodsReceiptNote grn = toEntity(dto);
        grn.setGrnStatus("RECEIVED");
        GoodsReceiptNote saved = grnRepository.save(grn);
        return toDTO(saved);
    }
    
    public GRNDTO update(Long id, GRNDTO dto) {
        GoodsReceiptNote grn = grnRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
        // Update fields
        GoodsReceiptNote updated = grnRepository.save(grn);
        return toDTO(updated);
    }
    
    public void approve(Long id) {
        GoodsReceiptNote grn = grnRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("GRN not found"));
        grn.setGrnStatus("APPROVED");
        grnRepository.save(grn);
    }
    
    private GRNDTO toDTO(GoodsReceiptNote entity) {
        GRNDTO dto = new GRNDTO();
        dto.setId(entity.getId());
        dto.setGrnNumber(entity.getGrnNumber());
        dto.setGrnStatus(entity.getGrnStatus());
        dto.setQualityStatus(entity.getQualityStatus());
        dto.setReceivedDate(entity.getReceivedDate());
        dto.setNotes(entity.getNotes());
        return dto;
    }
    
    private GoodsReceiptNote toEntity(GRNDTO dto) {
        GoodsReceiptNote entity = new GoodsReceiptNote();
        entity.setGrnNumber(dto.getGrnNumber());
        entity.setReceivedDate(dto.getReceivedDate());
        entity.setNotes(dto.getNotes());
        return entity;
    }
}
