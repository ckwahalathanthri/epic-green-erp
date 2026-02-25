package lk.epicgreen.erp.production.service;


import lk.epicgreen.erp.production.repository.WorkCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkCenterService {
    private final WorkCenterRepository repository;
    
    public List getAll() {
        return repository.findAll();
    }
    
    public List getActive() {
        return repository.findByIsActiveTrue();
    }
}