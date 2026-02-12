package lk.epicgreen.erp.accounting.controller;

import lk.epicgreen.erp.accounting.dto.response.AgingReportDTO;
import lk.epicgreen.erp.accounting.service.impl.AgingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/aging-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgingReportController {
    private final AgingReportService service;

    @GetMapping
    public ResponseEntity<List<AgingReportDTO>> generateAgingReport() {
        return ResponseEntity.ok(service.generateAgingReport());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<AgingReportDTO> getCustomerAging(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getCustomerAging(customerId));
    }
}
