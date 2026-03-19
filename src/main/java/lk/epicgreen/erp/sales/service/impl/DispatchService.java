package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.DispatchDTO;
import lk.epicgreen.erp.sales.entity.Dispatch;
import lk.epicgreen.erp.sales.mapper.DispatchMapper;
import lk.epicgreen.erp.sales.repository.DispatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DispatchService {
    
    private final DispatchRepository dispatchRepository;
    private final DispatchMapper dispatchMapper;
    
    public List<DispatchDTO> getAllDispatches() {
        return dispatchRepository.findAll().stream()
            .map(dispatchMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public DispatchDTO getDispatchById(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispatch not found with id: " + id));
        return dispatchMapper.toDTO(dispatch);
    }
    
    public List<DispatchDTO> getDispatchesByOrder(Long orderId) {
        return dispatchRepository.findByOrderId(orderId).stream()
            .map(dispatchMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<DispatchDTO> getDispatchesByStatus(String status) {
        return dispatchRepository.findByDispatchStatus(status).stream()
            .map(dispatchMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public DispatchDTO createDispatch(DispatchDTO dispatchDTO) {
        if (dispatchDTO.getDispatchNumber() == null || dispatchDTO.getDispatchNumber().isEmpty()) {
            dispatchDTO.setDispatchNumber(generateDispatchNumber());
        }
        
        if (dispatchDTO.getDispatchStatus() == null) {
            dispatchDTO.setDispatchStatus("PLANNED");
        }
        
        if (dispatchDTO.getDispatchDate() == null) {
            dispatchDTO.setDispatchDate(LocalDate.now());
        }
        System.out.println("The data accesed  is "+dispatchDTO);
        
        Dispatch dispatch = dispatchMapper.toEntity(dispatchDTO);
        Dispatch savedDispatch = dispatchRepository.save(dispatch);
        
        return dispatchMapper.toDTO(savedDispatch);
    }
    
    public DispatchDTO updateDispatch(Long id, DispatchDTO dispatchDTO) {
        Dispatch existingDispatch = dispatchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispatch not found with id: " + id));
        
        dispatchMapper.updateEntityFromDTO(dispatchDTO, existingDispatch);
        Dispatch updatedDispatch = dispatchRepository.save(existingDispatch);
        
        return dispatchMapper.toDTO(updatedDispatch);
    }
    
    public void deleteDispatch(Long id) {
        if (!dispatchRepository.existsById(id)) {
            throw new RuntimeException("Dispatch not found with id: " + id);
        }
        dispatchRepository.deleteById(id);
    }
    
    public DispatchDTO confirmDispatch(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispatch not found with id: " + id));
        
        dispatch.confirm(); // This sets stockDeducted = true
        Dispatch savedDispatch = dispatchRepository.save(dispatch);
        
        // TODO: Integrate with TIER 6 Warehouse to actually deduct stock
        
        return dispatchMapper.toDTO(savedDispatch);
    }
    
    public DispatchDTO markInTransit(Long id) {
        Dispatch dispatch = dispatchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispatch not found with id: " + id));
        
        dispatch.markInTransit();
        Dispatch savedDispatch = dispatchRepository.save(dispatch);
        
        return dispatchMapper.toDTO(savedDispatch);
    }
    
    public DispatchDTO markDelivered(Long id, String deliverer) {
        Dispatch dispatch = dispatchRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dispatch not found with id: " + id));
        
        dispatch.markDelivered(deliverer);
        Dispatch savedDispatch = dispatchRepository.save(dispatch);
        
        return dispatchMapper.toDTO(savedDispatch);
    }
    
    private String generateDispatchNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long count = dispatchRepository.count() + 1;
        return String.format("DSP-%s-%04d", year, count);
    }
}