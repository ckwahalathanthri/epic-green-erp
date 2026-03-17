package lk.epicgreen.erp.supplier.controller;



import lk.epicgreen.erp.supplier.dto.response.AgingReportSupplierDTO;
import lk.epicgreen.erp.supplier.service.impl.AgingReportServiceSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supplier/aging-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgingReportSupplierController {
    private final AgingReportServiceSupplier service;

    @GetMapping
    public ResponseEntity<List<AgingReportSupplierDTO>> generateAgingReport() {
        return ResponseEntity.ok(service.generateAgingReport());
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<AgingReportSupplierDTO> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getBySupplier(supplierId));
    }
}
