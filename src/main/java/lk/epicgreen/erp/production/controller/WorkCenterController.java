package lk.epicgreen.erp.production.controller;


import lk.epicgreen.erp.production.service.WorkCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production/work-centers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WorkCenterController {
    private final WorkCenterService service;
    
    @GetMapping
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List> getActive() {
        return ResponseEntity.ok(service.getActive());
    }
}