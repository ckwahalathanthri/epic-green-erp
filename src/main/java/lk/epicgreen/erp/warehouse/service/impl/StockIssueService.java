package lk.epicgreen.erp.warehouse.service.impl;

import lk.epicgreen.erp.warehouse.dto.response.StockIssueDTO;
import lk.epicgreen.erp.warehouse.entity.StockIssue;
import lk.epicgreen.erp.warehouse.repository.StockIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockIssueService {
    private final StockIssueRepository issueRepository;
    
    public List<StockIssueDTO> getAll() {
        return issueRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public StockIssueDTO getById(Long id) {
        return issueRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Stock Issue not found"));
    }
    
    public StockIssueDTO create(StockIssueDTO dto) {
        StockIssue issue = toEntity(dto);
        issue.setIssueStatus("DRAFT");
        StockIssue saved = issueRepository.save(issue);
        return toDTO(saved);
    }
    
    public void processIssue(Long id) {
        StockIssue issue = issueRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock Issue not found"));
        issue.setIssueStatus("ISSUED");
        issueRepository.save(issue);
    }
    
    private StockIssueDTO toDTO(StockIssue entity) {
        StockIssueDTO dto = new StockIssueDTO();
        dto.setId(entity.getId());
        dto.setIssueNumber(entity.getIssueNumber());
        dto.setIssueStatus(entity.getIssueStatus());
        dto.setIssueType(entity.getIssueType());
        dto.setIssueDate(entity.getIssueDate());
        dto.setPriority(entity.getPriority());
        return dto;
    }
    
    private StockIssue toEntity(StockIssueDTO dto) {
        StockIssue entity = new StockIssue();
        entity.setIssueNumber(dto.getIssueNumber());
        entity.setIssueDate(dto.getIssueDate());
        entity.setIssueType(dto.getIssueType());
        entity.setPriority(dto.getPriority());
        return entity;
    }
}
