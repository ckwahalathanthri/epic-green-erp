package lk.epicgreen.erp.sales.service.impl;

import lk.epicgreen.erp.sales.dto.request.DispatchNoteRequest;
import lk.epicgreen.erp.sales.dto.request.DispatchItemRequest;
import lk.epicgreen.erp.sales.dto.response.DispatchNoteResponse;
import lk.epicgreen.erp.sales.entity.DispatchNote;
import lk.epicgreen.erp.sales.entity.DispatchItem;
import lk.epicgreen.erp.sales.entity.SalesOrder;
import lk.epicgreen.erp.sales.entity.SalesOrderItem;
import lk.epicgreen.erp.sales.mapper.DispatchNoteMapper;
import lk.epicgreen.erp.sales.mapper.DispatchItemMapper;
import lk.epicgreen.erp.sales.repository.DispatchNoteRepository;
import lk.epicgreen.erp.sales.repository.DispatchItemRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderRepository;
import lk.epicgreen.erp.sales.repository.SalesOrderItemRepository;
import lk.epicgreen.erp.sales.service.DispatchNoteService;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerAddress;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerAddressRepository;
import lk.epicgreen.erp.warehouse.entity.Warehouse;
import lk.epicgreen.erp.warehouse.entity.WarehouseLocation;
import lk.epicgreen.erp.warehouse.repository.WarehouseRepository;
import lk.epicgreen.erp.warehouse.repository.WarehouseLocationRepository;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.admin.entity.UnitOfMeasure;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UnitOfMeasureRepository;
import lk.epicgreen.erp.admin.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DispatchNoteService interface
 * 
 * Dispatch Status Workflow:
 * PENDING → LOADING → DISPATCHED → IN_TRANSIT → DELIVERED
 * Can be RETURNED from IN_TRANSIT/DELIVERED
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DispatchNoteServiceImpl implements DispatchNoteService {

    private final DispatchNoteRepository dispatchNoteRepository;
    private final DispatchItemRepository dispatchItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UserRepository userRepository;
    private final DispatchNoteMapper dispatchNoteMapper;
    private final DispatchItemMapper dispatchItemMapper;

    @Override
    @Transactional
    public DispatchNoteResponse createDispatchNote(DispatchNoteRequest request) {
        log.info("Creating new Dispatch Note: {}", request.getDispatchNumber());

        // Validate unique constraint
        validateUniqueDispatchNumber(request.getDispatchNumber(), null);

        // Verify sales order exists
        SalesOrder order = findSalesOrderById(request.getOrderId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Verify warehouse exists
        Warehouse warehouse = findWarehouseById(request.getWarehouseId());

        // Create dispatch note entity
        DispatchNote dispatch = dispatchNoteMapper.toEntity(request);
        dispatch.setOrder(order);
        dispatch.setCustomer(customer);
        dispatch.setWarehouse(warehouse);

        // Set delivery address
        if (request.getDeliveryAddressId() != null) {
            CustomerAddress deliveryAddress = findCustomerAddressById(request.getDeliveryAddressId());
            dispatch.setDeliveryAddress(deliveryAddress);
        }

        // Create dispatch items
        List<DispatchItem> items = new ArrayList<>();
        for (DispatchItemRequest itemRequest : request.getItems()) {
            DispatchItem item = createDispatchItem(itemRequest);
            item.setDispatchNote(dispatch);
            items.add(item);
        }
        dispatch.setItems(items);

        DispatchNote savedDispatch = dispatchNoteRepository.save(dispatch);
        log.info("Dispatch Note created successfully: {}", savedDispatch.getDispatchNumber());

        return dispatchNoteMapper.toResponse(savedDispatch);
    }

    @Override
    @Transactional
    public DispatchNoteResponse updateDispatchNote(Long id, DispatchNoteRequest request) {
        log.info("Updating Dispatch Note: {}", id);

        DispatchNote dispatch = findDispatchNoteById(id);

        // Can only update PENDING dispatches
        if (!canUpdate(id)) {
            throw new InvalidOperationException(
                "Cannot update Dispatch Note. Current status: " + dispatch.getStatus() + 
                ". Only PENDING dispatches can be updated.");
        }

        // Validate unique constraint if dispatch number changed
        if (!dispatch.getDispatchNumber().equals(request.getDispatchNumber())) {
            validateUniqueDispatchNumber(request.getDispatchNumber(), id);
        }

        // Update basic fields
        dispatchNoteMapper.updateEntityFromRequest(request, dispatch);

        // Update relationships if changed
        if (!dispatch.getOrder().getId().equals(request.getOrderId())) {
            SalesOrder order = findSalesOrderById(request.getOrderId());
            dispatch.setOrder(order);
        }

        if (!dispatch.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            dispatch.setCustomer(customer);
        }

        if (!dispatch.getWarehouse().getId().equals(request.getWarehouseId())) {
            Warehouse warehouse = findWarehouseById(request.getWarehouseId());
            dispatch.setWarehouse(warehouse);
        }

        // Update delivery address
        if (request.getDeliveryAddressId() != null) {
            CustomerAddress deliveryAddress = findCustomerAddressById(request.getDeliveryAddressId());
            dispatch.setDeliveryAddress(deliveryAddress);
        } else {
            dispatch.setDeliveryAddress(null);
        }

        // Delete existing items and create new ones
        dispatchItemRepository.deleteAll(dispatch.getItems());
        dispatch.getItems().clear();

        List<DispatchItem> newItems = new ArrayList<>();
        for (DispatchItemRequest itemRequest : request.getItems()) {
            DispatchItem item = createDispatchItem(itemRequest);
            item.setDispatchNote(dispatch);
            newItems.add(item);
        }
        dispatch.setItems(newItems);

        DispatchNote updatedDispatch = dispatchNoteRepository.save(dispatch);
        log.info("Dispatch Note updated successfully: {}", updatedDispatch.getDispatchNumber());

        return dispatchNoteMapper.toResponse(updatedDispatch);
    }

    @Override
    @Transactional
    public void startLoading(Long id) {
        log.info("Starting loading for Dispatch Note: {}", id);

        DispatchNote dispatch = findDispatchNoteById(id);

        if (!"PENDING".equals(dispatch.getStatus())) {
            throw new InvalidOperationException(
                "Cannot start loading. Current status: " + dispatch.getStatus() + 
                ". Only PENDING dispatches can start loading.");
        }

        dispatch.setStatus("LOADING");
        dispatchNoteRepository.save(dispatch);

        log.info("Dispatch Note loading started successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsDispatched(Long id, Long deliveredBy) {
        log.info("Marking Dispatch Note as DISPATCHED: {} by user: {}", id, deliveredBy);

        DispatchNote dispatch = findDispatchNoteById(id);

        if (!"LOADING".equals(dispatch.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as dispatched. Current status: " + dispatch.getStatus() + 
                ". Only LOADING dispatches can be marked as dispatched.");
        }

        dispatch.setStatus("DISPATCHED");
        dispatch.setDispatchTime(LocalDateTime.now());
        dispatch.setDeliveredBy(userRepository.findById(deliveredBy).get());
        dispatchNoteRepository.save(dispatch);

        log.info("Dispatch Note marked as DISPATCHED successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsInTransit(Long id) {
        log.info("Marking Dispatch Note as IN_TRANSIT: {}", id);

        DispatchNote dispatch = findDispatchNoteById(id);

        if (!"DISPATCHED".equals(dispatch.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as in transit. Current status: " + dispatch.getStatus() + 
                ". Only DISPATCHED items can be marked as in transit.");
        }

        dispatch.setStatus("IN_TRANSIT");
        dispatchNoteRepository.save(dispatch);

        log.info("Dispatch Note marked as IN_TRANSIT successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsDelivered(Long id, String receivedByName, String receivedBySignature) {
        log.info("Marking Dispatch Note as DELIVERED: {}", id);

        DispatchNote dispatch = findDispatchNoteById(id);

        if (!"IN_TRANSIT".equals(dispatch.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as delivered. Current status: " + dispatch.getStatus() + 
                ". Only IN_TRANSIT dispatches can be marked as delivered.");
        }

        dispatch.setStatus("DELIVERED");
        dispatch.setDeliveryTime(LocalDateTime.now());
        dispatch.setReceivedByName(receivedByName);
        dispatch.setReceivedBySignature(receivedBySignature);

        // Update sales order item quantities
        for (DispatchItem item : dispatch.getItems()) {
            SalesOrderItem orderItem = item.getOrderItem();
            BigDecimal currentDelivered = orderItem.getQuantityDelivered() != null ? 
                orderItem.getQuantityDelivered() : BigDecimal.ZERO;
            orderItem.setQuantityDelivered(currentDelivered.add(item.getQuantityDispatched()));
            salesOrderItemRepository.save(orderItem);
        }

        dispatchNoteRepository.save(dispatch);

        log.info("Dispatch Note marked as DELIVERED successfully: {}", id);
    }

    @Override
    @Transactional
    public void markAsReturned(Long id, String reason) {
        log.info("Marking Dispatch Note as RETURNED: {} with reason: {}", id, reason);

        DispatchNote dispatch = findDispatchNoteById(id);

        if (!"IN_TRANSIT".equals(dispatch.getStatus()) && !"DELIVERED".equals(dispatch.getStatus())) {
            throw new InvalidOperationException(
                "Cannot mark as returned. Current status: " + dispatch.getStatus() + 
                ". Only IN_TRANSIT or DELIVERED dispatches can be returned.");
        }

        dispatch.setStatus("RETURNED");
        dispatch.setRemarks(dispatch.getRemarks() != null ? 
            dispatch.getRemarks() + "\nReturned: " + reason : 
            "Returned: " + reason);
        dispatchNoteRepository.save(dispatch);

        log.info("Dispatch Note marked as RETURNED successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteDispatchNote(Long id) {
        log.info("Deleting Dispatch Note: {}", id);

        if (!canDelete(id)) {
            DispatchNote dispatch = findDispatchNoteById(id);
            throw new InvalidOperationException(
                "Cannot delete Dispatch Note. Current status: " + dispatch.getStatus() + 
                ". Only PENDING dispatches can be deleted.");
        }

        dispatchNoteRepository.deleteById(id);
        log.info("Dispatch Note deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void updateGPSLocation(Long id, BigDecimal latitude, BigDecimal longitude) {
        log.info("Updating GPS location for Dispatch Note: {} to ({}, {})", id, latitude, longitude);

        DispatchNote dispatch = findDispatchNoteById(id);

        dispatch.setGpsLatitude(latitude);
        dispatch.setGpsLongitude(longitude);
        dispatchNoteRepository.save(dispatch);

        log.info("GPS location updated successfully for Dispatch Note: {}", id);
    }

    @Override
    @Transactional
    public void uploadDeliveryPhoto(Long id, String photoUrl) {
        log.info("Uploading delivery photo for Dispatch Note: {}", id);

        DispatchNote dispatch = findDispatchNoteById(id);

        dispatch.setDeliveryPhotoUrl(photoUrl);
        dispatchNoteRepository.save(dispatch);

        log.info("Delivery photo uploaded successfully for Dispatch Note: {}", id);
    }

    @Override
    public DispatchNoteResponse getDispatchNoteById(Long id) {
        DispatchNote dispatch = findDispatchNoteById(id);
        return dispatchNoteMapper.toResponse(dispatch);
    }

    @Override
    public DispatchNoteResponse getDispatchNoteByNumber(String dispatchNumber) {
        DispatchNote dispatch = dispatchNoteRepository.findByDispatchNumber(dispatchNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Dispatch Note not found: " + dispatchNumber));
        return dispatchNoteMapper.toResponse(dispatch);
    }

    @Override
    public PageResponse<DispatchNoteResponse> getAllDispatchNotes(Pageable pageable) {
        Page<DispatchNote> dispatchPage = dispatchNoteRepository.findAll(pageable);
        return createPageResponse(dispatchPage);
    }

    @Override
    public PageResponse<DispatchNoteResponse> getDispatchNotesByStatus(String status, Pageable pageable) {
        Page<DispatchNote> dispatchPage = dispatchNoteRepository.findByStatus(status, pageable);
        return createPageResponse(dispatchPage);
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByOrder(Long orderId) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByOrderId(orderId);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByCustomer(Long customerId) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByCustomerId(customerId);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByWarehouse(Long warehouseId) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByWarehouseId(warehouseId);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByDispatchDateBetween(startDate, endDate);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByRoute(String routeCode) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByRouteCode(routeCode);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getDispatchNotesByDeliveredBy(Long deliveredBy) {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByDeliveredBy(deliveredBy);
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getInTransitDispatches() {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByStatus("IN_TRANSIT");
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DispatchNoteResponse> getPendingDispatches() {
        List<DispatchNote> dispatches = dispatchNoteRepository.findByStatus("PENDING");
        return dispatches.stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * Search Dispatch Notes
     *
     * @param keyword
     * @param pageable
     */
    @Override
    public PageResponse<DispatchNoteResponse> searchDispatchNotes(String keyword, Pageable pageable) {
        return null;
    }

//    @Override
//    public PageResponse<DispatchNoteResponse> searchDispatchNotes(String keyword, Pageable pageable) {
//        Page<DispatchNote> dispatchPage = dispatchNoteRepository.searchDispatches(keyword, pageable);
//        return createPageResponse(dispatchPage);
//    }

    @Override
    public boolean canDelete(Long id) {
        DispatchNote dispatch = findDispatchNoteById(id);
        return "PENDING".equals(dispatch.getStatus());
    }

    @Override
    public boolean canUpdate(Long id) {
        DispatchNote dispatch = findDispatchNoteById(id);
        return "PENDING".equals(dispatch.getStatus());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private DispatchItem createDispatchItem(DispatchItemRequest request) {
        SalesOrderItem orderItem = findSalesOrderItemById(request.getOrderItemId());
        Product product = findProductById(request.getProductId());
        UnitOfMeasure uom = findUnitOfMeasureById(request.getUomId());

        DispatchItem item = dispatchItemMapper.toEntity(request);
        item.setOrderItem(orderItem);
        item.setProduct(product);
        item.setUom(uom);

        if (request.getLocationId() != null) {
            WarehouseLocation location = findWarehouseLocationById(request.getLocationId());
            item.setLocation(location);
        }

        return item;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
    }

    private void validateUniqueDispatchNumber(String dispatchNumber, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = dispatchNoteRepository.existsByDispatchNumberAndIdNot(dispatchNumber, excludeId);
        } else {
            exists = dispatchNoteRepository.existsByDispatchNumber(dispatchNumber);
        }

        if (exists) {
            throw new DuplicateResourceException("Dispatch Note with number '" + dispatchNumber + "' already exists");
        }
    }

    private DispatchNote findDispatchNoteById(Long id) {
        return dispatchNoteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dispatch Note not found: " + id));
    }

    private SalesOrder findSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found: " + id));
    }

    private SalesOrderItem findSalesOrderItemById(Long id) {
        return salesOrderItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sales Order Item not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id).get();
    }

    private CustomerAddress findCustomerAddressById(Long id) {
        return customerAddressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer Address not found: " + id));
    }

    private Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }

    private WarehouseLocation findWarehouseLocationById(Long id) {
        return warehouseLocationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse Location not found: " + id));
    }

//    private Product findProductById(Long id) {
//        return productRepository.findByIdAndDeletedAtIsNull(id)
//            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
//    }

    private UnitOfMeasure findUnitOfMeasureById(Long id) {
        return unitOfMeasureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Unit of Measure not found: " + id));
    }

    private PageResponse<DispatchNoteResponse> createPageResponse(Page<DispatchNote> dispatchPage) {
        List<DispatchNoteResponse> content = dispatchPage.getContent().stream()
            .map(dispatchNoteMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<DispatchNoteResponse>builder()
            .content(content)
            .pageNumber(dispatchPage.getNumber())
            .pageSize(dispatchPage.getSize())
            .totalElements(dispatchPage.getTotalElements())
            .totalPages(dispatchPage.getTotalPages())
            .last(dispatchPage.isLast())
            .first(dispatchPage.isFirst())
            .empty(dispatchPage.isEmpty())
            .build();
    }
}
