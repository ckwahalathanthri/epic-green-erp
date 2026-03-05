package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.PickingListDTO;
import lk.epicgreen.erp.sales.entity.PickingList;
import lk.epicgreen.erp.sales.mapper.PickingListMapper;
import lk.epicgreen.erp.sales.repository.PickingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PickingListService {
    
    private final PickingListRepository pickingListRepository;
    private final PickingListMapper pickingListMapper;
    
    public List<PickingListDTO> getAllPickingLists() {
        return pickingListRepository.findAll().stream()
            .map(pickingListMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public PickingListDTO getPickingListById(Long id) {
        PickingList pickingList = pickingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Picking list not found with id: " + id));
        return pickingListMapper.toDTO(pickingList);
    }
    
    public List<PickingListDTO> getPickingListsByOrder(Long orderId) {
        return pickingListRepository.findByOrderId(orderId).stream()
            .map(pickingListMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<PickingListDTO> getPickingListsByStatus(String status) {
        return pickingListRepository.findByPickingStatus(status).stream()
            .map(pickingListMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public PickingListDTO createPickingList(PickingListDTO pickingListDTO) {
        if (pickingListDTO.getPickingNumber() == null || pickingListDTO.getPickingNumber().isEmpty()) {
            pickingListDTO.setPickingNumber(generatePickingNumber());
        }
        
        if (pickingListDTO.getPickingStatus() == null) {
            pickingListDTO.setPickingStatus("PENDING");
        }
        
        if (pickingListDTO.getPickingDate() == null) {
            pickingListDTO.setPickingDate(LocalDate.now());
        }
        
        PickingList pickingList = pickingListMapper.toEntity(pickingListDTO);
        PickingList savedPickingList = pickingListRepository.save(pickingList);
        
        return pickingListMapper.toDTO(savedPickingList);
    }
    
    public PickingListDTO updatePickingList(Long id, PickingListDTO pickingListDTO) {
        PickingList existingPickingList = pickingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Picking list not found with id: " + id));
        
        pickingListMapper.updateEntityFromDTO(pickingListDTO, existingPickingList);
        PickingList updatedPickingList = pickingListRepository.save(existingPickingList);
        
        return pickingListMapper.toDTO(updatedPickingList);
    }
    
    public void deletePickingList(Long id) {
        if (!pickingListRepository.existsById(id)) {
            throw new RuntimeException("Picking list not found with id: " + id);
        }
        pickingListRepository.deleteById(id);
    }
    
    public PickingListDTO startPicking(Long id, String picker) {
        PickingList pickingList = pickingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Picking list not found with id: " + id));
        
        pickingList.startPicking(picker);
        PickingList savedPickingList = pickingListRepository.save(pickingList);
        
        return pickingListMapper.toDTO(savedPickingList);
    }
    
    public PickingListDTO completePicking(Long id) {
        PickingList pickingList = pickingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Picking list not found with id: " + id));
        
        if (!pickingList.isPickingComplete()) {
            throw new RuntimeException("Cannot complete picking - not all items have been picked");
        }
        
        pickingList.completePicking();
        PickingList savedPickingList = pickingListRepository.save(pickingList);
        
        return pickingListMapper.toDTO(savedPickingList);
    }
    
    private String generatePickingNumber() {
        String year = String.valueOf(LocalDate.now().getYear());
        long count = pickingListRepository.count() + 1;
        return String.format("PKL-%s-%04d", year, count);
    }
}
