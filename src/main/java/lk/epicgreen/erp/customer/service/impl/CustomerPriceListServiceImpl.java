package lk.epicgreen.erp.customer.service.impl;

import lk.epicgreen.erp.customer.dto.request.CustomerPriceListRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerPriceListResponse;
import lk.epicgreen.erp.customer.entity.Customer;
import lk.epicgreen.erp.customer.entity.CustomerPriceList;
import lk.epicgreen.erp.customer.mapper.CustomerPriceListMapper;
import lk.epicgreen.erp.customer.repository.CustomerRepository;
import lk.epicgreen.erp.customer.repository.CustomerPriceListRepository;
import lk.epicgreen.erp.customer.service.CustomerPriceListService;
import lk.epicgreen.erp.product.entity.Product;
import lk.epicgreen.erp.product.repository.ProductRepository;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CustomerPriceListService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CustomerPriceListServiceImpl implements CustomerPriceListService {

    private final CustomerPriceListRepository customerPriceListRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerPriceListMapper customerPriceListMapper;

    @Override
    @Transactional
    public CustomerPriceListResponse createCustomerPriceList(CustomerPriceListRequest request) {
        log.info("Creating new customer price list for customer: {} and product: {}", 
            request.getCustomerId(), request.getProductId());

        // Verify customer exists
        Customer customer = findCustomerById(request.getCustomerId());

        // Verify product exists
        Product product = findProductById(request.getProductId());

        // Check for duplicate active price list
        validateUniqueActivePrice(request.getCustomerId(), request.getProductId(), null);

        // Create price list entity
        CustomerPriceList priceList = customerPriceListMapper.toEntity(request);
        priceList.setCustomer(customer);
        priceList.setProduct(product);

        CustomerPriceList savedPriceList = customerPriceListRepository.save(priceList);
        log.info("Customer price list created successfully: {}", savedPriceList.getId());

        return customerPriceListMapper.toResponse(savedPriceList);
    }

    @Override
    @Transactional
    public CustomerPriceListResponse updateCustomerPriceList(Long id, CustomerPriceListRequest request) {
        log.info("Updating customer price list: {}", id);

        CustomerPriceList priceList = findCustomerPriceListById(id);

        // Check for duplicate active price list
        validateUniqueActivePrice(request.getCustomerId(), request.getProductId(), id);

        // Update fields
        customerPriceListMapper.updateEntityFromRequest(request, priceList);

        // Update customer if changed
        if (!priceList.getCustomer().getId().equals(request.getCustomerId())) {
            Customer customer = findCustomerById(request.getCustomerId());
            priceList.setCustomer(customer);
        }

        // Update product if changed
        if (!priceList.getProduct().getId().equals(request.getProductId())) {
            Product product = findProductById(request.getProductId());
            priceList.setProduct(product);
        }

        CustomerPriceList updatedPriceList = customerPriceListRepository.save(priceList);
        log.info("Customer price list updated successfully: {}", updatedPriceList.getId());

        return customerPriceListMapper.toResponse(updatedPriceList);
    }

    @Override
    @Transactional
    public void deleteCustomerPriceList(Long id) {
        log.info("Deleting customer price list: {}", id);

        CustomerPriceList priceList = findCustomerPriceListById(id);
        customerPriceListRepository.delete(priceList);

        log.info("Customer price list deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void activateCustomerPriceList(Long id) {
        log.info("Activating customer price list: {}", id);

        CustomerPriceList priceList = findCustomerPriceListById(id);
        priceList.setIsActive(true);
        customerPriceListRepository.save(priceList);

        log.info("Customer price list activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateCustomerPriceList(Long id) {
        log.info("Deactivating customer price list: {}", id);

        CustomerPriceList priceList = findCustomerPriceListById(id);
        priceList.setIsActive(false);
        customerPriceListRepository.save(priceList);

        log.info("Customer price list deactivated successfully: {}", id);
    }

    @Override
    public CustomerPriceListResponse getCustomerPriceListById(Long id) {
        CustomerPriceList priceList = findCustomerPriceListById(id);
        return customerPriceListMapper.toResponse(priceList);
    }

    @Override
    public List<CustomerPriceListResponse> getPriceListsByCustomer(Long customerId) {
        List<CustomerPriceList> priceLists = customerPriceListRepository.findByCustomerId(customerId);
        return priceLists.stream()
            .map(customerPriceListMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerPriceListResponse> getPriceListsByProduct(Long productId) {
        List<CustomerPriceList> priceLists = customerPriceListRepository.findByProductId(productId);
        return priceLists.stream()
            .map(customerPriceListMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerPriceListResponse getCustomerProductPrice(Long customerId, Long productId) {
        CustomerPriceList priceList = customerPriceListRepository
            .findByCustomerIdAndProductId(customerId, productId)
            .orElse(null);
        return priceList != null ? customerPriceListMapper.toResponse(priceList) : null;
    }

    @Override
    public List<CustomerPriceListResponse> getActivePriceListsByCustomer(Long customerId) {
        List<CustomerPriceList> priceLists = customerPriceListRepository
            .findByCustomerIdAndIsActiveTrue(customerId);
        return priceLists.stream()
            .map(customerPriceListMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CustomerPriceListResponse> getValidPriceListsByCustomerAndDate(Long customerId, LocalDate date) {
        List<CustomerPriceList> priceLists = customerPriceListRepository
            .findValidPricesByCustomerAndDate(customerId, date);
        return priceLists.stream()
            .map(customerPriceListMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getSpecialPrice(Long customerId, Long productId, BigDecimal quantity, LocalDate date) {
        // Find valid price list for customer and product on given date
        CustomerPriceList priceList = customerPriceListRepository
            .findValidPriceByCustomerProductAndDate(customerId, productId, date)
            .orElse(null);

        if (priceList == null) {
            return null; // No special price configured
        }

        // Check if quantity meets minimum requirement
        if (quantity.compareTo(priceList.getMinQuantity()) < 0) {
            return null; // Quantity doesn't meet minimum
        }

        return priceList.getSpecialPrice();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private CustomerPriceList findCustomerPriceListById(Long id) {
        return customerPriceListRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer price list not found: " + id));
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private void validateUniqueActivePrice(Long customerId, Long productId, Long excludeId) {
        if (excludeId == null) {
            if (customerPriceListRepository.existsByCustomerIdAndProductIdAndIsActiveTrue(customerId, productId)) {
                throw new DuplicateResourceException(
                    "Active price list already exists for customer: " + customerId + " and product: " + productId);
            }
        } else {
            if (customerPriceListRepository.existsByCustomerIdAndProductIdAndIsActiveTrueAndIdNot(
                    customerId, productId, excludeId)) {
                throw new DuplicateResourceException(
                    "Active price list already exists for customer: " + customerId + " and product: " + productId);
            }
        }
    }
}
