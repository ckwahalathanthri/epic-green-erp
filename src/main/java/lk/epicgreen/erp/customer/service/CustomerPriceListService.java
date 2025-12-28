package lk.epicgreen.erp.customer.service;

import lk.epicgreen.erp.customer.dto.request.CustomerPriceListRequest;
import lk.epicgreen.erp.customer.dto.response.CustomerPriceListResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for CustomerPriceList entity business logic
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public interface CustomerPriceListService {

    CustomerPriceListResponse createCustomerPriceList(CustomerPriceListRequest request);
    CustomerPriceListResponse updateCustomerPriceList(Long id, CustomerPriceListRequest request);
    void deleteCustomerPriceList(Long id);
    void activateCustomerPriceList(Long id);
    void deactivateCustomerPriceList(Long id);
    CustomerPriceListResponse getCustomerPriceListById(Long id);
    List<CustomerPriceListResponse> getPriceListsByCustomer(Long customerId);
    List<CustomerPriceListResponse> getPriceListsByProduct(Long productId);
    CustomerPriceListResponse getCustomerProductPrice(Long customerId, Long productId);
    List<CustomerPriceListResponse> getActivePriceListsByCustomer(Long customerId);
    List<CustomerPriceListResponse> getValidPriceListsByCustomerAndDate(Long customerId, LocalDate date);
    BigDecimal getSpecialPrice(Long customerId, Long productId, BigDecimal quantity, LocalDate date);
}
