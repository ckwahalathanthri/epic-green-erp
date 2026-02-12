package lk.epicgreen.erp.customer.controller;

import lk.epicgreen.erp.accounting.dto.response.StatementDTO;
import lk.epicgreen.erp.accounting.service.impl.CustomerStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer-statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerStatementController {
    private final CustomerStatementService service;

    @PostMapping("/generate")
    public ResponseEntity<StatementDTO> generateStatement(
            @RequestParam Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Authentication auth) {
        String username = auth != null ? auth.getName() : "system";
        return ResponseEntity.ok(service.generateStatement(customerId, fromDate, toDate, username));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<StatementDTO>> getCustomerStatements(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getCustomerStatements(customerId));
    }
}
