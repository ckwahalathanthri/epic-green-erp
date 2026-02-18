package lk.epicgreen.erp.warehouse.controller;


import lk.epicgreen.erp.warehouse.dto.response.StockReservationDTO;
import lk.epicgreen.erp.warehouse.service.impl.StockReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockReservationController {
    private final StockReservationService reservationService;
    
    @GetMapping
    public ResponseEntity<List<StockReservationDTO>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }
    
    @PostMapping
    public ResponseEntity<StockReservationDTO> create(@RequestBody StockReservationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(dto));
    }
    
    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseReservation(@PathVariable Long id) {
        reservationService.releaseReservation(id);
        return ResponseEntity.ok().build();
    }
}
