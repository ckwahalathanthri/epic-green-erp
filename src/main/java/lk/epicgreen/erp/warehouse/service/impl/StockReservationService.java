package lk.epicgreen.erp.warehouse.service.impl;


import lk.epicgreen.erp.warehouse.dto.response.StockReservationDTO;
import lk.epicgreen.erp.warehouse.entity.StockReservation;
import lk.epicgreen.erp.warehouse.repository.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockReservationService {
    private final StockReservationRepository reservationRepository;
    
    public List<StockReservationDTO> getAll() {
        return reservationRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public StockReservationDTO create(StockReservationDTO dto) {
        StockReservation reservation = toEntity(dto);
        reservation.setReservationStatus("ACTIVE");
        StockReservation saved = reservationRepository.save(reservation);
        return toDTO(saved);
    }
    
    public void releaseReservation(Long id) {
        StockReservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setReservationStatus("CANCELLED");
        reservationRepository.save(reservation);
    }
    
    private StockReservationDTO toDTO(StockReservation entity) {
        StockReservationDTO dto = new StockReservationDTO();
        dto.setId(entity.getId());
        dto.setReservationNumber(entity.getReservationNumber());
        dto.setReservationStatus(entity.getReservationStatus());
        dto.setReservedQuantity(entity.getReservedQuantity());
        dto.setRemainingQuantity(entity.getRemainingQuantity());
        return dto;
    }
    
    private StockReservation toEntity(StockReservationDTO dto) {
        StockReservation entity = new StockReservation();
        entity.setReservationNumber(dto.getReservationNumber());
        entity.setReservedQuantity(dto.getReservedQuantity());
        entity.setReservationDate(dto.getReservationDate());
        return entity;
    }
}
