package lk.epicgreen.erp.payment.service.impl;

import lk.epicgreen.erp.payment.dto.request.ChequeRequest;
import lk.epicgreen.erp.payment.dto.response.ChequeResponse;
import lk.epicgreen.erp.payment.entity.Cheque;
import lk.epicgreen.erp.payment.entity.Payment;
import lk.epicgreen.erp.payment.mapper.ChequeMapper;
import lk.epicgreen.erp.payment.repository.ChequeRepository;
import lk.epicgreen.erp.payment.repository.PaymentRepository;
import lk.epicgreen.erp.payment.service.ChequeService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ChequeService interface (PDC tracking)
 * 
 * Cheque Status Workflow:
 * RECEIVED → DEPOSITED → CLEARED
 * Can be BOUNCED, RETURNED, or CANCELLED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChequeServiceImpl implements ChequeService {

    private final ChequeRepository chequeRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final ChequeMapper chequeMapper;

    @Override
    @Transactional
    public ChequeResponse createCheque(ChequeRequest request) {
        log.info("Creating new Cheque: {}", request.getChequeNumber());

        // Verify payment exists
        Payment payment = findPaymentById(request.getPaymentId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create cheque entity
        Cheque cheque = chequeMapper.toEntity(request);
        cheque.setPayment(payment);
        cheque.setCustomer(customer);

        Cheque savedCheque = chequeRepository.save(cheque);
        log.info("Cheque created successfully: {}", savedCheque.getChequeNumber());

        return chequeMapper.toResponse(savedCheque);
    }

    @Override
    @Transactional
    public ChequeResponse updateCheque(Long id, ChequeRequest request) {
        log.info("Updating Cheque: {}", id);

        Cheque cheque = findChequeById(id);

        // Can only update RECEIVED cheques
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be updated.");
        }

        // Update basic fields
        chequeMapper.updateEntityFromRequest(request, cheque);

        // Update relationships if changed
        if (!cheque.getPayment().getId().equals(request.getPaymentId())) {
            Payment payment = findPaymentById(request.getPaymentId());
            cheque.setPayment(payment);
        }

        if (!cheque.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            cheque.setCustomer(customer);
        }

        Cheque updatedCheque = chequeRepository.save(cheque);
        log.info("Cheque updated successfully: {}", updatedCheque.getChequeNumber());

        return chequeMapper.toResponse(updatedCheque);
    }

    @Override
    @Transactional
    public void depositCheque(Long id, LocalDate depositDate) {
        log.info("Depositing Cheque: {} on date: {}", id, depositDate);

        Cheque cheque = findChequeById(id);

        if (!"RECEIVED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot deposit Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be deposited.");
        }

        cheque.setStatus("DEPOSITED");
        cheque.setDepositDate(depositDate);
        chequeRepository.save(cheque);

        log.info("Cheque deposited successfully: {}", id);
    }

    @Override
    @Transactional
    public void clearCheque(Long id, LocalDate clearanceDate) {
        log.info("Clearing Cheque: {} on date: {}", id, clearanceDate);

        Cheque cheque = findChequeById(id);

        if (!"DEPOSITED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot clear Cheque. Current status: " + cheque.getStatus() + 
                ". Only DEPOSITED cheques can be cleared.");
        }

        cheque.setStatus("CLEARED");
        cheque.setClearanceDate(clearanceDate);
        chequeRepository.save(cheque);

        log.info("Cheque cleared successfully: {}", id);
    }

    @Override
    @Transactional
    public void bounceCheque(Long id, String bounceReason, BigDecimal bounceCharges) {
        log.info("Bouncing Cheque: {} with reason: {}", id, bounceReason);

        Cheque cheque = findChequeById(id);

        if (!"DEPOSITED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot bounce Cheque. Current status: " + cheque.getStatus() + 
                ". Only DEPOSITED cheques can be bounced.");
        }

        cheque.setStatus("BOUNCED");
        cheque.setBounceReason(bounceReason);
        cheque.setBounceCharges(bounceCharges != null ? bounceCharges : BigDecimal.ZERO);
        chequeRepository.save(cheque);

        log.info("Cheque bounced successfully: {}", id);
    }

    @Override
    @Transactional
    public Cheque returnCheque(Long id, String reason) {
        log.info("Returning Cheque: {} with reason: {}", id, reason);

        Cheque cheque = findChequeById(id);

        if ("CLEARED".equals(cheque.getStatus()) || "RETURNED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot return Cheque. Current status: " + cheque.getStatus());
        }

        cheque.setStatus("RETURNED");
        cheque.setRemarks(cheque.getRemarks() != null ? 
            cheque.getRemarks() + "\nReturned: " + reason : 
            "Returned: " + reason);
        chequeRepository.save(cheque);

        log.info("Cheque returned successfully: {}", id);
        return cheque;
    }

    @Transactional
    public Cheque getChequeByNumber(String chequeNumber) {
        log.info("Fetching Cheque by number: {}", chequeNumber);
        return chequeRepository.findByChequeNumber(chequeNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Cheque not found: " + chequeNumber));
    }

    @Transactional
    public Cheque presentCheque(Long id,LocalDate presentationDate) {
        log.info("Presenting Cheque: {} on date: {}", id, presentationDate);

        Cheque cheque = findChequeById(id);

        if (!"RECEIVED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot present Cheque. Current status: " + cheque.getStatus() +
                ". Only RECEIVED cheques can be presented.");
        }

        cheque.setStatus("PRESENTED");
        cheque.setPresentationDate(presentationDate);
        chequeRepository.save(cheque);

        log.info("Cheque presented successfully: {}", id);
        return cheque;
    }

    @Transactional
    public List<Cheque> getPendingCheques(){
        log.info("Fetching pending Cheques");
        return chequeRepository.findByStatus("RECEIVED");
    }

    @Transactional
    public List<Cheque> getPresentedCheques(){
        log.info("Fetching presented Cheques");
        return chequeRepository.findByStatus("PRESENTED");
    }

    @Transactional
    public List<Cheque> getClearedCheques(){
        log.info("Fetching cleared Cheques");
        return chequeRepository.findByStatus("CLEARED");
    }

    @Transactional
    public List<Cheque> getCancelledCheques(){
        log.info("Fetching cancelled Cheques");
        return chequeRepository.findByStatus("CANCELLED");
    }

    @Transactional
    public List<Cheque> getPostDatedCheques(){
        log.info("Fetching post-dated Cheques");
        return chequeRepository.findPostDatedCheques();
    }

    @Transactional
    public List<Cheque> getChequesDueForPresentation(){
        log.info("Fetching Cheques due for presentation");
        return chequeRepository.findChequesDueForPresentation();
    }

    @Transactional
    public List<Cheque> getOverdueCheques(){
        log.info("Fetching overdue Cheques");
        return chequeRepository.findOverdueCheques();
    }

    @Transactional
    public List<Cheque> getChequesRequiringAction(){
        log.info("Fetching Cheques requiring action");
        return chequeRepository.findChequesRequiringAction();
    }

    @Transactional
    public List<Cheque> getRecentCheques( Pageable pageable){
        log.info("Fetching recent Cheques, limit: {}");
        return chequeRepository.findRecentCheques(pageable);
    }

    @Transactional
    public List<Cheque> getCustomerRecentCheques(Long customerId,Pageable page){
        log.info("Fetching recent Cheques for Customer: {}, limit: {}", customerId);
        return chequeRepository.findRecentChequesByCustomer(customerId,page);
    }

    @Transactional
    public boolean canPresentCheque(Long id){
        Cheque cheque = findChequeById(id);
        return "RECEIVED".equals(cheque.getStatus());
    }

    @Transactional
    public  boolean canClearCheque(Long id){
        Cheque cheque = findChequeById(id);
        return "DEPOSITED".equals(cheque.getStatus());
    }

    @Transactional
    public boolean canBounceCheque(Long id){
        Cheque cheque = findChequeById(id);
        return "DEPOSITED".equals(cheque.getStatus());
    }

    @Transactional
    public  List<Cheque> createBulkCheques(List<ChequeRequest> requests){
        log.info("Creating bulk Cheques, count: {}", requests.size());
        List<Cheque> cheques = requests.stream().map(request -> {

            Payment payment = findPaymentById(request.getPaymentId());


            Customer customer = findCustomerById(request.getCustomerId());


            Cheque cheque = chequeMapper.toEntity(request);
            cheque.setPayment(payment);
            cheque.setCustomer(customer);
            return cheque;
        }).collect(Collectors.toList());

        List<Cheque> savedCheques = chequeRepository.saveAll(cheques);
        log.info("Bulk Cheques created successfully, count: {}", savedCheques.size());
        return savedCheques;
    }
    @Transactional
    public int presentBulkCheques(List<Long> chequeIds,LocalDate presentationDate){
        log.info("Presenting bulk Cheques, count: {}", chequeIds.size());
        List<Cheque> cheques = chequeRepository.findAllById(chequeIds);
        int presentedCount = 0;

        for (Cheque cheque : cheques) {
            if ("RECEIVED".equals(cheque.getStatus())) {
                cheque.setStatus("PRESENTED");
                cheque.setPresentationDate(presentationDate);
                presentedCount++;
            }
        }

        chequeRepository.saveAll(cheques);
        log.info("Bulk Cheques presented successfully, count: {}", presentedCount);
        return presentedCount;
    }

    @Transactional
    public  int clearBulkCheques(List<Long>chequeIds,LocalDate clearanceDate){
        log.info("Clearing bulk Cheques, count: {}", chequeIds.size());
        List<Cheque> cheques = chequeRepository.findAllById(chequeIds);
        int clearedCount = 0;

        for (Cheque cheque : cheques) {
            if ("DEPOSITED".equals(cheque.getStatus())) {
                cheque.setStatus("CLEARED");
                cheque.setClearanceDate(clearanceDate);
                clearedCount++;
            }
        }

        chequeRepository.saveAll(cheques);
        log.info("Bulk Cheques cleared successfully, count: {}", clearedCount);
        return clearedCount;
    }

    @Transactional
    public int deleteBulkCheques(List<Long> chequeIds){
        log.info("Deleting bulk Cheques, count: {}", chequeIds.size());
        List<Cheque> cheques = chequeRepository.findAllById(chequeIds);
        int deletedCount = 0;

        for (Cheque cheque : cheques) {
            if ("RECEIVED".equals(cheque.getStatus())) {
                chequeRepository.delete(cheque);
                deletedCount++;
            }
        }

        log.info("Bulk Cheques deleted successfully, count: {}", deletedCount);
        return deletedCount;
    }

    @Transactional
    public Map<String,Object> getChequeStatistics(){
        log.info("Fetching Cheque statistics");
        Long totalCheques = chequeRepository.count();
        Long receivedCheques = chequeRepository.countByStatus("RECEIVED");
        Long depositedCheques = chequeRepository.countByStatus("DEPOSITED");
        Long clearedCheques = chequeRepository.countByStatus("CLEARED");
        Long bouncedCheques = chequeRepository.countByStatus("BOUNCED");
        Long returnedCheques = chequeRepository.countByStatus("RETURNED");
        Long cancelledCheques = chequeRepository.countByStatus("CANCELLED");

        Map<String,Object> map=new HashMap<>();
        map.put("totalCheques", totalCheques);
        map.put("receivedCheques", receivedCheques);
        map.put("depositedCheques", depositedCheques);
        map.put("clearedCheques", clearedCheques);
        map.put("bouncedCheques", bouncedCheques);
        map.put("returnedCheques", returnedCheques);
        map.put("cancelledCheques", cancelledCheques);

        log.info("Cheque statistics fetched successfully");
        return map;
    }

    @Transactional
    public List<Map<String,Object>> getChequeStatusDistribution(){
        log.info("Fetching Cheque status distribution");
        Long receivedCheques = chequeRepository.countByStatus("RECEIVED");
        Long depositedCheques = chequeRepository.countByStatus("DEPOSITED");
        Long clearedCheques = chequeRepository.countByStatus("CLEARED");
        Long bouncedCheques = chequeRepository.countByStatus("BOUNCED");
        Long returnedCheques = chequeRepository.countByStatus("RETURNED");
        Long cancelledCheques = chequeRepository.countByStatus("CANCELLED");

        Map<String,Object> map=new HashMap<>();
        map.put("RECEIVED", receivedCheques);
        map.put("DEPOSITED", depositedCheques);
        map.put("CLEARED", clearedCheques);
        map.put("BOUNCED", bouncedCheques);
        map.put("RETURNED", returnedCheques);
        map.put("CANCELLED", cancelledCheques);

        log.info("Cheque status distribution fetched successfully");

        return Collections.singletonList(map);
    }

    @Transactional
    public List<Map<String,Object>> getBankDistribution(){
        log.info("Fetching Cheque bank distribution");
        List<Map<String,Object>> bankDistribution = chequeRepository.findBankDistribution();
        log.info("Cheque bank distribution fetched successfully");
        return bankDistribution;
    }

    @Transactional
    public double getTotalChequeAmount(){
        log.info("Calculating total Cheque amount");
        Double totalAmount = chequeRepository.sumTotalChequeAmount();
        log.info("Total Cheque amount calculated: {}", totalAmount);
        return totalAmount;
    }

    @Transactional
    public double getAverageChequeAmount(){
        log.info("Calculating average Cheque amount");
        Double averageAmount = chequeRepository.averageChequeAmount();
        log.info("Average Cheque amount calculated: {}", averageAmount);
        return averageAmount;
    }

    @Transactional
    public double getChequeBounceRate(){
        log.info("Calculating Cheque bounce rate");
        Long totalCheques = chequeRepository.count();
        Long bouncedCheques = chequeRepository.countByStatus("BOUNCED");

        double bounceRate = totalCheques > 0 ? (bouncedCheques.doubleValue() / totalCheques.doubleValue()) * 100 : 0.0;
        log.info("Cheque bounce rate calculated: {}%", bounceRate);
        return bounceRate;
    }

    @Transactional
    public List<Map<String,Object>> getMonthlyChequeCount(LocalDate startDate,LocalDate endDate){
        log.info("Fetching monthly Cheque count from {} to {}", startDate, endDate);
        List<Map<String,Object>> monthlyCounts = chequeRepository.findMonthlyChequeCount(startDate, endDate);
        log.info("Monthly Cheque count fetched successfully");
        return monthlyCounts;
    }

    @Transactional
    public Map<String,Object> getDashboardStatistics(){
        log.info("Fetching Cheque dashboard statistics");
        Long totalCheques = chequeRepository.count();
        Long pendingCheques = chequeRepository.countByStatus("RECEIVED");
        Long clearedCheques = chequeRepository.countByStatus("CLEARED");
        Double totalAmount = chequeRepository.sumTotalChequeAmount();

        Map<String,Object> map=new HashMap<>();
        map.put("totalCheques", totalCheques);
        map.put("pendingCheques", pendingCheques);
        map.put("clearedCheques", clearedCheques);
        map.put("totalAmount", totalAmount);

        log.info("Cheque dashboard statistics fetched successfully");
        return map;
    }
    @Override
    @Transactional
    public Cheque cancelCheque(Long id, String reason) {
        log.info("Cancelling Cheque: {} with reason: {}", id, reason);

        Cheque cheque = findChequeById(id);

        if (!"RECEIVED".equals(cheque.getStatus())) {
            throw new InvalidOperationException(
                "Cannot cancel Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be cancelled.");
        }

        cheque.setStatus("CANCELLED");
        cheque.setRemarks(cheque.getRemarks() != null ? 
            cheque.getRemarks() + "\nCancelled: " + reason : 
            "Cancelled: " + reason);
        chequeRepository.save(cheque);

        log.info("Cheque cancelled successfully: {}", id);
        return cheque;
    }

    @Override
    @Transactional
    public void deleteCheque(Long id) {
        log.info("Deleting Cheque: {}", id);

        if (!canDelete(id)) {
            Cheque cheque = findChequeById(id);
            throw new InvalidOperationException(
                "Cannot delete Cheque. Current status: " + cheque.getStatus() + 
                ". Only RECEIVED cheques can be deleted.");
        }

        chequeRepository.deleteById(id);
        log.info("Cheque deleted successfully: {}", id);
    }

    @Override
    public ChequeResponse getChequeById(Long id) {
        Cheque cheque = findChequeById(id);
        return chequeMapper.toResponse(cheque);
    }

    @Override
    public ChequeResponse getChequeByChequeNumber(String chequeNumber) {
        Cheque cheque = chequeRepository.findByChequeNumber(chequeNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Cheque not found: " + chequeNumber));
        return chequeMapper.toResponse(cheque);
    }

    @Override
    public PageResponse<ChequeResponse> getAllCheques(Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.findAll(pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public PageResponse<ChequeResponse> getChequesByStatus(String status, Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.findByStatus(status, pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public List<ChequeResponse> getChequesByPayment(Long paymentId) {
        List<Cheque> cheques = chequeRepository.findByPaymentId(paymentId);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByCustomer(Long customerId) {
        List<Cheque> cheques = chequeRepository.findByCustomerId(customerId);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Cheque> cheques = chequeRepository.findByChequeDateBetween(startDate, endDate);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getChequesByBank(String bankName) {
        List<Cheque> cheques = chequeRepository.findByBankName(bankName);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getPendingDepositCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("RECEIVED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getPendingClearanceCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("DEPOSITED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getDueCheques(LocalDate asOfDate) {
        List<Cheque> cheques = chequeRepository.findDueCheques(asOfDate);
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<ChequeResponse> getBouncedCheques() {
        List<Cheque> cheques = chequeRepository.findByStatus("BOUNCED");
        return cheques.stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<ChequeResponse> searchCheques(String keyword, Pageable pageable) {
        Page<Cheque> chequePage = chequeRepository.searchCheques(keyword,null,null,null,null,null,pageable);
        return createPageResponse(chequePage);
    }

    @Override
    public BigDecimal getTotalChequeAmountByCustomer(Long customerId) {
        return chequeRepository.sumChequeAmountByCustomer(customerId);
    }

    @Override
    public BigDecimal getTotalPendingClearanceAmount() {
        return chequeRepository.sumPendingClearanceAmount();
    }

    @Override
    public boolean canDelete(Long id) {
        Cheque cheque = findChequeById(id);
        return "RECEIVED".equals(cheque.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        Cheque cheque = findChequeById(id);
        return "RECEIVED".equals(cheque.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Cheque findChequeById(Long id) {
        return chequeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cheque not found: " + id));
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id);
    }

    private PageResponse<ChequeResponse> createPageResponse(Page<Cheque> chequePage) {
        List<ChequeResponse> content = chequePage.getContent().stream()
            .map(chequeMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<ChequeResponse>builder()
            .content(content)
            .pageNumber(chequePage.getNumber())
            .pageSize(chequePage.getSize())
            .totalElements(chequePage.getTotalElements())
            .totalPages(chequePage.getTotalPages())
            .last(chequePage.isLast())
            .first(chequePage.isFirst())
            .empty(chequePage.isEmpty())
            .build();
    }
}
