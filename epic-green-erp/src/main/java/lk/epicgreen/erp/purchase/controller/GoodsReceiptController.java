package lk.epicgreen.erp.purchase.controller;

import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.purchase.dto.GoodsReceiptRequest;
import lk.epicgreen.erp.purchase.entity.GoodsReceipt;
import lk.epicgreen.erp.purchase.service.GoodsReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GoodsReceipt Controller
 * REST controller for goods receipt operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/purchase/goods-receipts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class GoodsReceiptController {
    
    private final GoodsReceiptService goodsReceiptService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> createGoodsReceipt(
        @Valid @RequestBody GoodsReceiptRequest request
    ) {
        GoodsReceipt created = goodsReceiptService.createGoodsReceipt(request);
        return ResponseEntity.ok(ApiResponse.success(created, "Goods receipt created successfully"));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> updateGoodsReceipt(
        @PathVariable Long id,
        @Valid @RequestBody GoodsReceiptRequest request
    ) {
        GoodsReceipt updated = goodsReceiptService.updateGoodsReceipt(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Goods receipt updated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteGoodsReceipt(@PathVariable Long id) {
        goodsReceiptService.deleteGoodsReceipt(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Goods receipt deleted successfully"));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> getGoodsReceiptById(@PathVariable Long id) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptById(id);
        return ResponseEntity.ok(ApiResponse.success(receipt, "Goods receipt retrieved successfully"));
    }
    
    @GetMapping("/number/{receiptNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> getGoodsReceiptByNumber(
        @PathVariable String receiptNumber
    ) {
        GoodsReceipt receipt = goodsReceiptService.getGoodsReceiptByNumber(receiptNumber);
        return ResponseEntity.ok(ApiResponse.success(receipt, "Goods receipt retrieved successfully"));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<GoodsReceipt>>> getAllGoodsReceipts(Pageable pageable) {
        Page<GoodsReceipt> receipts = goodsReceiptService.getAllGoodsReceipts(pageable);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Goods receipts retrieved successfully"));
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER', 'USER')")
    public ResponseEntity<ApiResponse<Page<GoodsReceipt>>> searchGoodsReceipts(
        @RequestParam String keyword,
        Pageable pageable
    ) {
        Page<GoodsReceipt> receipts = goodsReceiptService.searchGoodsReceipts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Search results retrieved"));
    }
    
    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> verifyGoodsReceipt(
        @PathVariable Long id,
        @RequestParam Long verifiedByUserId,
        @RequestParam(required = false) String verificationNotes
    ) {
        GoodsReceipt verified = goodsReceiptService.verifyGoodsReceipt(id, verifiedByUserId, verificationNotes);
        return ResponseEntity.ok(ApiResponse.success(verified, "Goods receipt verified"));
    }
    
    @PostMapping("/{id}/post-to-inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> postToInventory(@PathVariable Long id) {
        GoodsReceipt posted = goodsReceiptService.postToInventory(id);
        return ResponseEntity.ok(ApiResponse.success(posted, "Goods receipt posted to inventory"));
    }
    
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<GoodsReceipt>> rejectGoodsReceipt(
        @PathVariable Long id,
        @RequestParam String rejectionReason
    ) {
        GoodsReceipt rejected = goodsReceiptService.rejectGoodsReceipt(id, rejectionReason);
        return ResponseEntity.ok(ApiResponse.success(rejected, "Goods receipt rejected"));
    }
    
    @PostMapping("/{id}/record-quality-inspection")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> recordQualityInspection(
        @PathVariable Long id,
        @RequestParam Double acceptedQuantity,
        @RequestParam Double rejectedQuantity
    ) {
        goodsReceiptService.recordQualityInspection(id, acceptedQuantity, rejectedQuantity);
        return ResponseEntity.ok(ApiResponse.success(null, "Quality inspection recorded"));
    }
    
    @PostMapping("/{id}/mark-discrepancy")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> markDiscrepancy(
        @PathVariable Long id,
        @RequestParam String discrepancyNotes
    ) {
        goodsReceiptService.markDiscrepancy(id, discrepancyNotes);
        return ResponseEntity.ok(ApiResponse.success(null, "Discrepancy marked"));
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getPendingGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getPendingGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Pending goods receipts retrieved"));
    }
    
    @GetMapping("/verified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getVerifiedGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getVerifiedGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Verified goods receipts retrieved"));
    }
    
    @GetMapping("/posted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getPostedGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getPostedGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Posted goods receipts retrieved"));
    }
    
    @GetMapping("/rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getRejectedGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getRejectedGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Rejected goods receipts retrieved"));
    }
    
    @GetMapping("/unverified")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getUnverifiedGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getUnverifiedGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Unverified goods receipts retrieved"));
    }
    
    @GetMapping("/unposted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getUnpostedGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getUnpostedGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Unposted goods receipts retrieved"));
    }
    
    @GetMapping("/with-discrepancies")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getGoodsReceiptsWithDiscrepancies() {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsWithDiscrepancies();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Goods receipts with discrepancies retrieved"));
    }
    
    @GetMapping("/today")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getTodaysGoodsReceipts() {
        List<GoodsReceipt> receipts = goodsReceiptService.getTodaysGoodsReceipts();
        return ResponseEntity.ok(ApiResponse.success(receipts, "Today's goods receipts retrieved"));
    }
    
    @GetMapping("/purchase-order/{purchaseOrderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getGoodsReceiptsByPurchaseOrder(
        @PathVariable Long purchaseOrderId
    ) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Purchase order goods receipts retrieved"));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getGoodsReceiptsBySupplier(
        @PathVariable Long supplierId
    ) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Supplier goods receipts retrieved"));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getGoodsReceiptsByWarehouse(
        @PathVariable Long warehouseId
    ) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Warehouse goods receipts retrieved"));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getGoodsReceiptsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<GoodsReceipt> receipts = goodsReceiptService.getGoodsReceiptsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Goods receipts in date range retrieved"));
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<List<GoodsReceipt>>> getRecentGoodsReceipts(
        @RequestParam(defaultValue = "10") int limit
    ) {
        List<GoodsReceipt> receipts = goodsReceiptService.getRecentGoodsReceipts(limit);
        return ResponseEntity.ok(ApiResponse.success(receipts, "Recent goods receipts retrieved"));
    }
    
    @GetMapping("/purchase-order/{purchaseOrderId}/total-received")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalReceivedQuantityByPurchaseOrder(
        @PathVariable Long purchaseOrderId
    ) {
        Double totalReceived = goodsReceiptService.getTotalReceivedQuantityByPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalReceived, "Total received quantity retrieved"));
    }
    
    @GetMapping("/purchase-order/{purchaseOrderId}/total-accepted")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalAcceptedQuantityByPurchaseOrder(
        @PathVariable Long purchaseOrderId
    ) {
        Double totalAccepted = goodsReceiptService.getTotalAcceptedQuantityByPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalAccepted, "Total accepted quantity retrieved"));
    }
    
    @GetMapping("/purchase-order/{purchaseOrderId}/total-rejected")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER', 'WAREHOUSE_MANAGER')")
    public ResponseEntity<ApiResponse<Double>> getTotalRejectedQuantityByPurchaseOrder(
        @PathVariable Long purchaseOrderId
    ) {
        Double totalRejected = goodsReceiptService.getTotalRejectedQuantityByPurchaseOrder(purchaseOrderId);
        return ResponseEntity.ok(ApiResponse.success(totalRejected, "Total rejected quantity retrieved"));
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatistics() {
        Map<String, Object> stats = goodsReceiptService.getGoodsReceiptStatistics();
        return ResponseEntity.ok(ApiResponse.success(stats, "Statistics retrieved"));
    }
    
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'PURCHASE_MANAGER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStatistics() {
        Map<String, Object> dashboard = goodsReceiptService.getDashboardStatistics();
        return ResponseEntity.ok(ApiResponse.success(dashboard, "Dashboard statistics retrieved"));
    }
}
