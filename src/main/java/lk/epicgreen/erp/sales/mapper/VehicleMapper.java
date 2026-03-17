package lk.epicgreen.erp.sales.mapper;


import lk.epicgreen.erp.sales.dto.response.VehicleDTO;
import lk.epicgreen.erp.sales.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {
    
    public VehicleDTO toDTO(Vehicle entity) {
        if (entity == null) return null;
        
        VehicleDTO dto = new VehicleDTO();
        dto.setId(entity.getId());
        dto.setVehicleNumber(entity.getVehicleNumber());
        dto.setVehicleType(entity.getVehicleType());
        dto.setVehicleStatus(entity.getVehicleStatus());
        dto.setCapacity(entity.getCapacity());
        dto.setGpsEnabled(entity.getGpsEnabled());
        return dto;
    }
    
    public Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) return null;
        
        Vehicle entity = new Vehicle();
        entity.setVehicleNumber(dto.getVehicleNumber());
        entity.setVehicleType(dto.getVehicleType());
        entity.setVehicleStatus(dto.getVehicleStatus());
        return entity;
    }
    
    public void updateEntityFromDTO(VehicleDTO dto, Vehicle entity) {
        if (dto == null || entity == null) return;
        entity.setVehicleStatus(dto.getVehicleStatus());
        entity.setCapacity(dto.getCapacity());
    }
}