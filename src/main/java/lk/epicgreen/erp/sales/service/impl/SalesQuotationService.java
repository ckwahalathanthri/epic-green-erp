package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.SalesQuotationDTO;
import lk.epicgreen.erp.sales.entity.SalesQuotation;
import lk.epicgreen.erp.sales.mapper.SalesQuotationMapper;
import lk.epicgreen.erp.sales.repository.OrderStatusHistoryRepository;
import lk.epicgreen.erp.sales.repository.SalesQuotationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesQuotationService {
    
    private final SalesQuotationRepository quotationRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final SalesQuotationMapper quotationMapper;
    
    /**
     * Get all quotations
     */
    public List<SalesQuotationDTO> getAllQuotations() {
        return quotationRepository.findAll().stream()
            .map(quotationMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get quotation by ID
     */
    public SalesQuotationDTO getQuotationById(Long id) {
        SalesQuotation quotation = quotationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + id));
        return quotationMapper.toDTO(quotation);
    }
    
    /**
     * Get quotation by quotation number
     */
    public SalesQuotationDTO getQuotationByNumber(String quotationNumber) {
        SalesQuotation quotation = quotationRepository.findByQuotationNumber(quotationNumber)
            .orElseThrow(() -> new RuntimeException("Quotation not found with number: " + quotationNumber));
        return quotationMapper.toDTO(quotation);
    }
    
    /**
     * Get quotations by customer
     */
    public List<SalesQuotationDTO> getQuotationsByCustomer(Long customerId) {
        return quotationRepository.findByCustomerId(customerId).stream()
            .map(quotationMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get quotations by status
     */
    public List<SalesQuotationDTO> getQuotationsByStatus(String status) {
        return quotationRepository.findByQuotationStatus(status).stream()
            .map(quotationMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Create new quotation
     */
    public SalesQuotationDTO createQuotation(SalesQuotationDTO quotationDTO) {
        // Generate quotation number if not provided
        if (quotationDTO.getQuotationNumber() == null || quotationDTO.getQuotationNumber().isEmpty()) {
            quotationDTO.setQuotationNumber(generateQuotationNumber());
        }
        
        // Set default values
        if (quotationDTO.getQuotationStatus() == null) {
            quotationDTO.setQuotationStatus("DRAFT");
        }
        
        if (quotationDTO.getQuotationDate() == null) {
            quotationDTO.setQuotationDate(LocalDate.now());
        }
        
        SalesQuotation quotation = quotationMapper.toEntity(quotationDTO);
        SalesQuotation savedQuotation = quotationRepository.save(quotation);
        
        return quotationMapper.toDTO(savedQuotation);
    }
    
    /**
     * Update quotation
     */
    public SalesQuotationDTO updateQuotation(Long id, SalesQuotationDTO quotationDTO) {
        SalesQuotation existingQuotation = quotationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + id));
        
        // Update fields
        quotationMapper.updateEntityFromDTO(quotationDTO, existingQuotation);
        
        SalesQuotation updatedQuotation = quotationRepository.save(existingQuotation);
        return quotationMapper.toDTO(updatedQuotation);
    }
    
    /**
     * Delete quotation
     */
    public void deleteQuotation(Long id) {
        if (!quotationRepository.existsById(id)) {
            throw new RuntimeException("Quotation not found with id: " + id);
        }
        quotationRepository.deleteById(id);
    }
    
    /**
     * Approve quotation
     */
    public SalesQuotationDTO approveQuotation(Long id, String approver) {
        SalesQuotation quotation = quotationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + id));
        
        if (!"PENDING".equals(quotation.getQuotationStatus()) && !"DRAFT".equals(quotation.getQuotationStatus())) {
            throw new RuntimeException("Quotation cannot be approved. Current status: " + quotation.getQuotationStatus());
        }
        
        quotation.approve(approver);
        SalesQuotation savedQuotation = quotationRepository.save(quotation);
        
        return quotationMapper.toDTO(savedQuotation);
    }
    
    /**
     * Reject quotation
     */
    public SalesQuotationDTO rejectQuotation(Long id) {
        SalesQuotation quotation = quotationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + id));
        
        quotation.reject();
        SalesQuotation savedQuotation = quotationRepository.save(quotation);
        
        return quotationMapper.toDTO(savedQuotation);
    }
    
    /**
     * Check if quotation can be converted to order
     */
    public boolean canConvertToOrder(Long id) {
        SalesQuotation quotation = quotationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + id));
        
        return quotation.canConvertToOrder();
    }
    
    /**
     * Mark quotation as converted
     */
    public SalesQuotationDTO markAsConverted(Long quotationId, Long orderId) {
        SalesQuotation quotation = quotationRepository.findById(quotationId)
            .orElseThrow(() -> new RuntimeException("Quotation not found with id: " + quotationId));
        
        quotation.convertToOrder(orderId);
        SalesQuotation savedQuotation = quotationRepository.save(quotation);
        
        return quotationMapper.toDTO(savedQuotation);
    }
    
    /**
     * Get expired quotations
     */
    public List<SalesQuotationDTO> getExpiredQuotations() {
        return quotationRepository.findExpiredQuotations("APPROVED", LocalDate.now()).stream()
            .map(quotationMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Generate unique quotation number
     */
    private String generateQuotationNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long count = quotationRepository.count() + 1;
        return String.format("QT-%s-%04d", year, count);
    }
}
