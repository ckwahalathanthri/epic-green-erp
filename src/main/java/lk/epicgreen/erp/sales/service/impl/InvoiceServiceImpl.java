package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.dto.request.InvoiceRequest;
import lk.epicgreen.erp.sales.dto.request.InvoiceItemRequest;
import lk.epicgreen.erp.sales.dto.response.InvoiceResponse;
import lk.epicgreen.erp.sales.entity.Invoice;
import lk.epicgreen.erp.sales.entity.InvoiceItem;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.entity.SalesOrderItem;
import lk.epicgreen.erp.sales.entity.DispatchNote;
import lk.epicgreen.erp.sales.mapper.InvoiceMapper;
import lk.epicgreen.erp.sales.mapper.InvoiceItemMapper;
import lk.epicgreen.erp.sales.repository.InvoiceRepository;
import lk.epicgreen.erp.sales.repository.InvoiceItemRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderItemRepository;
import lk.epicgreen.erp.sales.repository.DispatchNoteRepository;
import lk.epicgreen.erp.sales.service.InvoiceService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerAddressRepository;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.TaxRate;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.admin.repository.TaxRateRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of InvoiceService interface
 * 
 * Invoice Status Workflow:
 * DRAFT → POSTED
 * Can be CANCELLED from DRAFT only
 * 
 * Payment Status:
 * UNPAID → PARTIAL → PAID
 * OVERDUE (auto-calculated based on due_date)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final DispatchNoteRepository dispatchNoteRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final TaxRateRepository taxRateRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;

    @Override
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        log.info("Creating new Invoice: {}", request.getInvoiceNumber());

        // Validate unique constraint
        validateUniqueInvoiceNumber(request.getInvoiceNumber(), null);

        // Verify sales order exists
        SalesOrder order = findSalesOrderById(request.getOrderId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Create invoice entity
        Invoice invoice = invoiceMapper.toEntity(request);
        invoice.setOrder(order);
        invoice.setCustomer(customer);

        // Set dispatch note if provided
        if (request.getDispatchId() != null) {
            DispatchNote dispatch = findDispatchNoteById(request.getDispatchId());
            invoice.setDispatchNote(dispatch);
        }

        // Set billing address
        if (request.getBillingAddressId() != null) {
            CustomerAddress billingAddress = findCustomerAddressById(request.getBillingAddressId());
            invoice.setBillingAddress(billingAddress);
        }

        // Create invoice items
        List<InvoiceItem> items = new ArrayList<>();
        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = createInvoiceItem(itemRequest);
            item.setInvoice(invoice);
            items.add(item);
        }
        invoice.setItems(items);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice created successfully: {}", savedInvoice.getInvoiceNumber());

        return invoiceMapper.toResponse(savedInvoice);
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoice(Long id, InvoiceRequest request) {
        log.info("Updating Invoice: {}", id);

        Invoice invoice = findInvoiceById(id);

        // Can only update DRAFT invoices
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Invoice. Current status: " + invoice.getStatus() + 
                ". Only DRAFT invoices can be updated.");
        }

        // Validate unique constraint if invoice number changed
        if (!invoice.getInvoiceNumber().equals(request.getInvoiceNumber())) {
            validateUniqueInvoiceNumber(request.getInvoiceNumber(), id);
        }

        // Update basic fields
        invoiceMapper.updateEntityFromRequest(request, invoice);

        // Update relationships if changed
        if (!invoice.getOrder().getId().equals(request.getOrderId())) {
            SalesOrder order = findSalesOrderById(request.getOrderId());
            invoice.setOrder(order);
        }

        if (!invoice.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            invoice.setCustomer(customer);
        }

        // Update dispatch note
        if (request.getDispatchId() != null) {
            DispatchNote dispatch = findDispatchNoteById(request.getDispatchId());
            invoice.setDispatchNote(dispatch);
        } else {
            invoice.setDispatchNote(null);
        }

        // Update billing address
        if (request.getBillingAddressId() != null) {
            CustomerAddress billingAddress = findCustomerAddressById(request.getBillingAddressId());
            invoice.setBillingAddress(billingAddress);
        } else {
            invoice.setBillingAddress(null);
        }

        // Delete existing items and create new ones
        invoiceItemRepository.deleteAll(invoice.getItems());
        invoice.getItems().clear();

        List<InvoiceItem> newItems = new ArrayList<>();
        for (InvoiceItemRequest itemRequest : request.getItems()) {
            InvoiceItem item = createInvoiceItem(itemRequest);
            item.setInvoice(invoice);
            newItems.add(item);
        }
        invoice.setItems(newItems);

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        log.info("Invoice updated successfully: {}", updatedInvoice.getInvoiceNumber());

        return invoiceMapper.toResponse(updatedInvoice);
    }

    @Override
    @Transactional
    public void postInvoice(Long id) {
        log.info("Posting Invoice: {}", id);

        Invoice invoice = findInvoiceById(id);

        if (!"DRAFT".equals(invoice.getStatus())) {
            throw new InvalidOperationException(
                "Cannot post Invoice. Current status: " + invoice.getStatus() + 
                ". Only DRAFT invoices can be posted.");
        }

        invoice.setStatus("POSTED");
        invoiceRepository.save(invoice);

        log.info("Invoice posted successfully: {}", id);
    }

    @Override
    @Transactional
    public void cancelInvoice(Long id, String reason) {
        log.info("Cancelling Invoice: {} with reason: {}", id, reason);

        Invoice invoice = findInvoiceById(id);

        if (!"DRAFT".equals(invoice.getStatus())) {
            throw new InvalidOperationException(
                "Cannot cancel Invoice. Current status: " + invoice.getStatus() + 
                ". Only DRAFT invoices can be cancelled.");
        }

        invoice.setStatus("CANCELLED");
        invoice.setRemarks(invoice.getRemarks() != null ? 
            invoice.getRemarks() + "\nCancelled: " + reason : 
            "Cancelled: " + reason);
        invoiceRepository.save(invoice);

        log.info("Invoice cancelled successfully: {}", id);
    }

    @Override
    @Transactional
    public void recordPayment(Long id, BigDecimal amount) {
        log.info("Recording payment of {} for Invoice: {}", amount, id);

        Invoice invoice = findInvoiceById(id);

        if ("CANCELLED".equals(invoice.getStatus())) {
            throw new InvalidOperationException("Cannot record payment for a cancelled invoice.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException("Payment amount must be greater than zero.");
        }

        BigDecimal currentPaid = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal newPaidAmount = currentPaid.add(amount);

        if (newPaidAmount.compareTo(invoice.getTotalAmount()) > 0) {
            throw new InvalidOperationException(
                "Payment amount exceeds invoice total. Outstanding: " + 
                invoice.getTotalAmount().subtract(currentPaid));
        }

        invoice.setPaidAmount(newPaidAmount);

        // Update payment status automatically
        if (newPaidAmount.compareTo(invoice.getTotalAmount()) == 0) {
            invoice.setPaymentStatus("PAID");
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setPaymentStatus("PARTIAL");
        }

        invoiceRepository.save(invoice);

        log.info("Payment recorded successfully for Invoice: {}", id);
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Long id, String paymentStatus) {
        log.info("Updating payment status for Invoice: {} to {}", id, paymentStatus);

        Invoice invoice = findInvoiceById(id);

        invoice.setPaymentStatus(paymentStatus);
        invoiceRepository.save(invoice);

        log.info("Payment status updated successfully for Invoice: {}", id);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        log.info("Deleting Invoice: {}", id);

        if (!canDelete(id)) {
            Invoice invoice = findInvoiceById(id);
            throw new InvalidOperationException(
                "Cannot delete Invoice. Current status: " + invoice.getStatus() + 
                ". Only DRAFT invoices can be deleted.");
        }

        invoiceRepository.deleteById(id);
        log.info("Invoice deleted successfully: {}", id);
    }

    @Override
    public InvoiceResponse getInvoiceById(Long id) {
        Invoice invoice = findInvoiceById(id);
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + invoiceNumber));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    public PageResponse<InvoiceResponse> getAllInvoices(Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);
        return createPageResponse(invoicePage);
    }

    @Override
    public PageResponse<InvoiceResponse> getInvoicesByStatus(String status, Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepository.findByStatus(status, pageable);
        return createPageResponse(invoicePage);
    }

    @Override
    public PageResponse<InvoiceResponse> getInvoicesByPaymentStatus(String paymentStatus, Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepository.findByPaymentStatus(paymentStatus, pageable);
        return createPageResponse(invoicePage);
    }

    @Override
    public List<InvoiceResponse> getInvoicesByCustomer(Long customerId) {
        List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getInvoicesByOrder(Long orderId) {
        List<Invoice> invoices = invoiceRepository.findByOrderId(orderId);
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getInvoicesByDispatch(Long dispatchId) {
        List<Invoice> invoices = invoiceRepository.findByDispatchNoteId(dispatchId);
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<InvoiceResponse> getInvoicesByType(String invoiceType, Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepository.findByInvoiceType(invoiceType, pageable);
        return createPageResponse(invoicePage);
    }

    @Override
    public List<InvoiceResponse> getUnpaidInvoices() {
        List<Invoice> invoices = invoiceRepository.findByPaymentStatus("UNPAID");
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getOverdueInvoices() {
        List<Invoice> invoices = invoiceRepository.findOverdueInvoices(LocalDate.now());
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getInvoicesDueSoon(Integer daysAhead) {
        LocalDate dueDate = LocalDate.now().plusDays(daysAhead);
        List<Invoice> invoices = invoiceRepository.findInvoicesDueSoon(LocalDate.now(), dueDate);
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceResponse> getPartiallyPaidInvoices() {
        List<Invoice> invoices = invoiceRepository.findByPaymentStatus("PARTIAL");
        return invoices.stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<InvoiceResponse> searchInvoices(String keyword, Pageable pageable) {
        Page<Invoice> invoicePage = invoiceRepository.searchInvoices(keyword, pageable);
        return createPageResponse(invoicePage);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountByCustomer(Long customerId) {
        return invoiceRepository.sumTotalAmountByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalOutstandingByCustomer(Long customerId) {
        return invoiceRepository.sumOutstandingByCustomer(customerId)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalInvoiceAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.sumTotalAmountByDateRange(startDate, endDate)
            .orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean canDelete(Long id) {
        Invoice invoice = findInvoiceById(id);
        return "DRAFT".equals(invoice.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        Invoice invoice = findInvoiceById(id);
        return "DRAFT".equals(invoice.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private InvoiceItem createInvoiceItem(InvoiceItemRequest request) {
        Product product = findProductById(request.getProductId());
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        InvoiceItem item = invoiceItemMapper.toEntity(request);
        item.setProduct(product);
        item.setUom(uom);

        if (request.getOrderItemId() != null) {
            SalesOrderItem orderItem = findSalesOrderItemById(request.getOrderItemId());
            item.setOrderItem(orderItem);
        }

        if (request.getTaxRateId() != null) {
            TaxRate taxRate = findTaxRateById(request.getTaxRateId());
            item.setTaxRate(taxRate);
        }

        return item;
    }

    private void validateUniqueInvoiceNumber(String invoiceNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = invoiceRepository.existsByInvoiceNumberAndIdNot(invoiceNumber, excludeId);
        } else {
            exists = invoiceRepository.existsByInvoiceNumber(invoiceNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Invoice with number '" + invoiceNumber + "' already exists");
        }
    }

    private Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
    }

    private SalesOrder findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found: " + id));
    }

    private SalesOrderItem findSalesOrderItemById(Long id) {
        return salesOrderItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order Item not found: " + id));
    }

    private DispatchNote findDispatchNoteById(Long id) {
        return dispatchNoteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dispatch Note not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private CustomerAddress findCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer Address not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private TaxRate findTaxRateById(Long id) {
        return taxRateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tax Rate not found: " + id));
    }

    private PageResponse<InvoiceResponse> createPageResponse(Page<Invoice> invoicePage) {
        List<InvoiceResponse> content = invoicePage.getContent().stream()
            .map(invoiceMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<InvoiceResponse>builder()
            .content(content)
            .pageNumber(invoicePage.getNumber())
            .pageSize(invoicePage.getSize())
            .totalElements(invoicePage.getTotalElements())
            .totalPages(invoicePage.getTotalPages())
            .last(invoicePage.isLast())
            .first(invoicePage.isFirst())
            .empty(invoicePage.isEmpty())
            .build();
    }
}
