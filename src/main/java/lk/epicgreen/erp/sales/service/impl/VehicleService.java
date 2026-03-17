package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.sales.dto.response.VehicleDTO;
import lk.epicgreen.erp.sales.entity.Vehicle;
import lk.epicgreen.erp.sales.mapper.VehicleMapper;
import lk.epicgreen.erp.sales.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(vehicleMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        return vehicleMapper.toDTO(vehicle);
    }
    
    public List<VehicleDTO> getVehiclesByStatus(String status) {
        return vehicleRepository.findByVehicleStatus(status).stream()
            .map(vehicleMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<VehicleDTO> getActiveVehicles() {
        return getVehiclesByStatus("ACTIVE");
    }
    
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        if (vehicleDTO.getVehicleStatus() == null) {
            vehicleDTO.setVehicleStatus("ACTIVE");
        }
        
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        return vehicleMapper.toDTO(savedVehicle);
    }
    
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + id));
        
        vehicleMapper.updateEntityFromDTO(vehicleDTO, existingVehicle);
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        
        return vehicleMapper.toDTO(updatedVehicle);
    }
    
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
}