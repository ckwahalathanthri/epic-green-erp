package lk.epicgreen.erp.audit.service.impl;

import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.audit.dto.request.AuditLogRequest;
import lk.epicgreen.erp.audit.dto.response.AuditLogResponse;
import lk.epicgreen.erp.audit.entity.AuditLog;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import lk.epicgreen.erp.audit.mapper.AuditLogMapper;
import lk.epicgreen.erp.audit.repository.AuditLogRepository;
import lk.epicgreen.erp.audit.repository.ErrorLogRepository;
import lk.epicgreen.erp.audit.service.AuditLogService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of AuditLogService interface
 * 
 * Audit logs are immutable - no UPDATE or DELETE operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final ErrorLogRepository errorLogRepository;
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuditLogResponse createAuditLog(AuditLogRequest request) {
        log.debug("Creating audit log for action: {} on entity: {}", request.getAction(), request.getEntityType());

        AuditLog auditLog = auditLogMapper.toEntity(request);
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);

        log.debug("Audit log created successfully with ID: {}", savedAuditLog.getId());

        return auditLogMapper.toResponse(savedAuditLog);
    }

    @Override
    public AuditLogResponse getAuditLogById(Long id) {
        AuditLog auditLog = findAuditLogById(id);
        return auditLogMapper.toResponse(auditLog);
    }

    @Override
    public PageResponse<AuditLogResponse> getAllAuditLogs(Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findAll(pageable);
        return createPageResponse(auditLogPage);
    }

    public Page<AuditLog> getAuditLogsByUserId(Long userId,Pageable pageable){
        return auditLogRepository.findByUsername(String.valueOf(userId),pageable);
    }

    public Page<AuditLog> getAuditLogsByActionType(String actionType,Pageable pageable){
        return auditLogRepository.getAuditLogsByActionType(actionType,pageable);
    }

    public  List<AuditLog> getEntityHistory(String entityType){
        return auditLogRepository.findByEntityType(entityType);
    }

    public ErrorLog getErrorLogById(Long id){
        return errorLogRepository.getById(id);
    }

    public Page<ErrorLog> getAllErrorLogs(Pageable pageable){
        return errorLogRepository.findAll(pageable);
    }
    public List<ErrorLog> getAllErrorLogs(){
        return errorLogRepository.findAll();
    }

    public Page<ErrorLog> getErrorLogsBySeverity(String severityLevel, Pageable pageable){
        return  errorLogRepository.findBySeverity(severityLevel,pageable);
    }

    public List<ErrorLog> getCriticalErrors(Pageable pageable){
        return  errorLogRepository.findCriticalSeverityErrors();
    }

    public List<ErrorLog> getUnresolvedErrors(Pageable pageable){
        return errorLogRepository.findUnresolvedCriticalErrors();
    }

    public ErrorLog updateErrorStatus(Long errorId, String status){
        ErrorLog error=errorLogRepository.findById(errorId)
                .orElseThrow(()-> new EntityNotFoundException("No errors were recorded on that id "+errorId));
        error.setStackTrace(status);
        errorLogRepository.save(error);
        return error;
    }
    public List<AuditLog> getAuditLogsByDate(LocalDate date){
        return auditLogRepository.findByCreatedAt(date.atStartOfDay());
    }

//    public ErrorLog assignError(Long errorId, Long userId){
//        ErrorLog error=errorLogRepository.findById(errorId)
//                .orElseThrow(()->
//                        new EntityNotFoundException(
//                                "Error not fount in id "+errorId
//                        ));
//        User user=userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(
//                "The specified user was not found"
//        ));
//
//
//    }

    public List<AuditLog> getRecentAuditLogs(Pageable limit){
        return auditLogRepository.findByOrderByCreatedAtDesc(limit);
    }

    public Page<AuditLog> getAuditLogsByUsername(String username,Pageable pageable){
        return auditLogRepository.findByUsername(username,pageable);
    }

    public Page<AuditLog> getAuditLogsByModule(String moduleName,Pageable pageable){
        return auditLogRepository.findByModel(moduleName,pageable);
    }

    public  Page<AuditLog> getFailedAuditLogs(Pageable pageable){
        return auditLogRepository.findByStatus("FAILED",pageable);
    }

    public List<AuditLog> getTodayAuditLogs(){
        LocalDateTime time=LocalDateTime.now();
        return  auditLogRepository.findByCreatedAt(time);
    }
    @Override
    public List<AuditLogResponse> getAuditLogsByUser(Long userId) {
        List<AuditLog> auditLogs = auditLogRepository.findByUserId(userId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    public void deleteOldAuditLogs(int daysToKeep){
        LocalDateTime cutoffDate=LocalDateTime.now().minusDays(daysToKeep);
        auditLogRepository.deleteAuditLogByCreatedAtBefore(cutoffDate);
        log.info("Deleted succesfully");

    }

public Map<String,Object> getActivityStatisticsCount(){
        Map<String,Object> map=new HashMap<>();
        map.put("SUCCESS",auditLogRepository.countByStatus("SUCCESS"));
        map.put("FAILED",auditLogRepository.countByStatus("FAILED"));
        return map;
}

    public  Map<String,Object> getActivityStatistics(LocalDateTime startDate,LocalDateTime endDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("ActivityStatistics", auditLogRepository.countByCreatedAtBetween(startDate, endDate));
        return map;
    }

    public Map<String,Object> getUserActivitySummary(Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("UserActivitySummary",auditLogRepository.countByUserId(userId));
        return map;
    }

    public  Map<String,Object> getUserActivitySummary(Long userId, LocalDateTime startDate,LocalDateTime endDate){
        Map<String,Object> map=new HashMap<>();
        map.put("UserAcctivitySummary",auditLogRepository.countByCreatedAtBetweenAndUser_Id(startDate,endDate,userId));
        return map;
    }

    @Override
    public List<Map<String, Object>> getActionTypeDistribution() {
        return null;
    }

    public List<Map<String,Object>> getActionTypeDistribution(String actionType){
        Map<String,Object> map=new HashMap<>();
        map.put("ActionTypeDistribution",auditLogRepository.countByAction(actionType));
        return (List<Map<String, Object>>) map;
    }

    public List<Map<String,Object>> getMostViewedPages(){
        List<Map<String,Object>> viewedPages=new ArrayList<>();
        return  viewedPages;
    }

    public   List<Map<String,Object>> getMostViewedPages(LocalDateTime start,LocalDateTime stop){
        List<Map<String,Object>> Pages=new ArrayList<>();
        return Pages;
    }

    public List<Map<String, Object>> getHourlyActivity(LocalDate date){
        List<Map<String,Object>> HourleyActivity=new ArrayList<>();
        return HourleyActivity;
    }

    public Map<String, Object> getAuditStatistics(){
        Map<String,Object> map=new HashMap<>();
        map.put("totalAudits",auditLogRepository.count());
        return  map;
    }


    @Override
    public List<AuditLogResponse> getAuditLogsByEntity(String entityType, Long entityId) {
        List<AuditLog> auditLogs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AuditLogResponse> getAuditLogsByOperationType(String operationType, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findByOperationType(operationType, pageable);
        return createPageResponse(auditLogPage);
    }



    @Override
    public PageResponse<AuditLogResponse> getAuditLogsByStatus(String status, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.findByStatus(status, pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startDate, endDate);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByAction(String action) {
        List<AuditLog> auditLogs = auditLogRepository.findByAction(action);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getFailedOperations() {
        List<AuditLog> auditLogs = auditLogRepository.findByStatus("FAILED");
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<AuditLogResponse> searchAuditLogs(String keyword, Pageable pageable) {
        Page<AuditLog> auditLogPage = auditLogRepository.searchAuditLogs(null,null,keyword,null,null,null,null,null,pageable);
        return createPageResponse(auditLogPage);
    }

    @Override
    public List<AuditLogResponse> getAuditLogsBySession(String sessionId) {
        List<AuditLog> auditLogs = auditLogRepository.findBySessionId(sessionId);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogResponse> getAuditLogsByIpAddress(String ipAddress) {
        List<AuditLog> auditLogs = auditLogRepository.findByIpAddress(ipAddress);
        return auditLogs.stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private AuditLog findAuditLogById(Long id) {
        return auditLogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Audit Log not found: " + id));
    }

    private PageResponse<AuditLogResponse> createPageResponse(Page<AuditLog> auditLogPage) {
        List<AuditLogResponse> content = auditLogPage.getContent().stream()
            .map(auditLogMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<AuditLogResponse>builder()
            .content(content)
            .pageNumber(auditLogPage.getNumber())
            .pageSize(auditLogPage.getSize())
            .totalElements(auditLogPage.getTotalElements())
            .totalPages(auditLogPage.getTotalPages())
            .last(auditLogPage.isLast())
            .first(auditLogPage.isFirst())
            .empty(auditLogPage.isEmpty())
            .build();
    }
}
