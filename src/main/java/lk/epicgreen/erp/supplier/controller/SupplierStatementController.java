package lk.epicgreen.erp.supplier.controller;


import lk.epicgreen.erp.supplier.dto.response.StatementDTO;
import lk.epicgreen.erp.supplier.service.impl.SupplierStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierStatementController {
    private final SupplierStatementService service;

    @PostMapping("/generate")
    public ResponseEntity<StatementDTO> generateStatement(
        @RequestParam Long supplierId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
        Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return new ResponseEntity<>(service.generateStatement(supplierId, fromDate, toDate, username), HttpStatus.CREATED);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<StatementDTO>> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getBySupplier(supplierId));
    }
}
