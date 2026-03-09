package lk.epicgreen.erp.warehouse.service.impl;


import lk.epicgreen.erp.warehouse.entity.GoodsIssue;
import lk.epicgreen.erp.warehouse.repository.GoodsIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoodsIssueService {
    private final GoodsIssueRepository repository;
    
    public List<GoodsIssue> findAll() {
        return repository.findAll();
    }
    
    public GoodsIssue findById(Long id) {
        return repository.findById(id).get();
    }
    
    public GoodsIssue create(GoodsIssue issue) {
        return repository.save(issue);
    }
    
    public GoodsIssue post(Long id) {
        GoodsIssue issue = findById(id);
        issue.post();
        return repository.save(issue);
    }
}
