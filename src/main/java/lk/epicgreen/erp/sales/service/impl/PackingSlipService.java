package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.PackingSlipDTO;
import lk.epicgreen.erp.sales.entity.PackingSlip;
import lk.epicgreen.erp.sales.mapper.PackingSlipMapper;
import lk.epicgreen.erp.sales.repository.PackingSlipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PackingSlipService {
    
    private final PackingSlipRepository packingSlipRepository;
    private final PackingSlipMapper packingSlipMapper;
    
    public List<PackingSlipDTO> getAllPackingSlips() {
        return packingSlipRepository.findAll().stream()
            .map(packingSlipMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public PackingSlipDTO getPackingSlipById(Long id) {
        PackingSlip packingSlip = packingSlipRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Packing slip not found with id: " + id));
        return packingSlipMapper.toDTO(packingSlip);
    }
    
    public List<PackingSlipDTO> getPackingSlipsByOrder(Long orderId) {
        return packingSlipRepository.findByOrderId(orderId).stream()
            .map(packingSlipMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public PackingSlipDTO createPackingSlip(PackingSlipDTO packingSlipDTO) {
        if (packingSlipDTO.getPackingNumber() == null || packingSlipDTO.getPackingNumber().isEmpty()) {
            packingSlipDTO.setPackingNumber(generatePackingNumber());
        }
        
        if (packingSlipDTO.getPackingStatus() == null) {
            packingSlipDTO.setPackingStatus("PENDING");
        }
        
        if (packingSlipDTO.getPackingDate() == null) {
            packingSlipDTO.setPackingDate(LocalDate.now());
        }
        
        PackingSlip packingSlip = packingSlipMapper.toEntity(packingSlipDTO);
        PackingSlip savedPackingSlip = packingSlipRepository.save(packingSlip);
        
        return packingSlipMapper.toDTO(savedPackingSlip);
    }
    
    public PackingSlipDTO completePacking(Long id) {
        PackingSlip packingSlip = packingSlipRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Packing slip not found with id: " + id));
        
        packingSlip.completePacking();
        PackingSlip savedPackingSlip = packingSlipRepository.save(packingSlip);
        
        return packingSlipMapper.toDTO(savedPackingSlip);
    }
    
    private String generatePackingNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long count = packingSlipRepository.count() + 1;
        return String.format("PKS-%s-%04d", year, count);
    }
}