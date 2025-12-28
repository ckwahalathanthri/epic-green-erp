package lk.epicgreen.erp.sales.mapper;

import lk.epicgreen.erp.sales.dto.request.DispatchNoteRequest;
import lk.epicgreen.erp.sales.dto.response.DispatchNoteResponse;
import lk.epicgreen.erp.sales.entity.DispatchNote;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for DispatchNote entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class DispatchNoteMapper {

    private final DispatchItemMapper dispatchItemMapper;

    public DispatchNoteMapper(DispatchItemMapper dispatchItemMapper) {
        this.dispatchItemMapper = dispatchItemMapper;
    }

    public DispatchNote toEntity(DispatchNoteRequest request) {
        if (request == null) {
            return null;
        }

        return DispatchNote.builder()
            .dispatchNumber(request.getDispatchNumber())
            .dispatchDate(request.getDispatchDate())
            .vehicleNumber(request.getVehicleNumber())
            .driverName(request.getDriverName())
            .driverMobile(request.getDriverMobile())
            .routeCode(request.getRouteCode())
            .status(request.getStatus() != null ? request.getStatus() : "PENDING")
            .receivedByName(request.getReceivedByName())
            .deliveryPhotoUrl(request.getDeliveryPhotoUrl())
            .gpsLatitude(request.getGpsLatitude())
            .gpsLongitude(request.getGpsLongitude())
            .remarks(request.getRemarks())
            .build();
    }

    public void updateEntityFromRequest(DispatchNoteRequest request, DispatchNote dispatch) {
        if (request == null || dispatch == null) {
            return;
        }

        dispatch.setDispatchNumber(request.getDispatchNumber());
        dispatch.setDispatchDate(request.getDispatchDate());
        dispatch.setVehicleNumber(request.getVehicleNumber());
        dispatch.setDriverName(request.getDriverName());
        dispatch.setDriverMobile(request.getDriverMobile());
        dispatch.setRouteCode(request.getRouteCode());
        dispatch.setStatus(request.getStatus());
        dispatch.setReceivedByName(request.getReceivedByName());
        dispatch.setDeliveryPhotoUrl(request.getDeliveryPhotoUrl());
        dispatch.setGpsLatitude(request.getGpsLatitude());
        dispatch.setGpsLongitude(request.getGpsLongitude());
        dispatch.setRemarks(request.getRemarks());
    }

    public DispatchNoteResponse toResponse(DispatchNote dispatch) {
        if (dispatch == null) {
            return null;
        }

        String deliveryAddress = formatAddress(dispatch.getDeliveryAddress());

        return DispatchNoteResponse.builder()
            .id(dispatch.getId())
            .dispatchNumber(dispatch.getDispatchNumber())
            .dispatchDate(dispatch.getDispatchDate())
            .orderId(dispatch.getOrder() != null ? dispatch.getOrder().getId() : null)
            .orderNumber(dispatch.getOrder() != null ? dispatch.getOrder().getOrderNumber() : null)
            .customerId(dispatch.getCustomer() != null ? dispatch.getCustomer().getId() : null)
            .customerCode(dispatch.getCustomer() != null ? dispatch.getCustomer().getCustomerCode() : null)
            .customerName(dispatch.getCustomer() != null ? dispatch.getCustomer().getCustomerName() : null)
            .warehouseId(dispatch.getWarehouse() != null ? dispatch.getWarehouse().getId() : null)
            .warehouseCode(dispatch.getWarehouse() != null ? dispatch.getWarehouse().getWarehouseCode() : null)
            .warehouseName(dispatch.getWarehouse() != null ? dispatch.getWarehouse().getWarehouseName() : null)
            .vehicleNumber(dispatch.getVehicleNumber())
            .driverName(dispatch.getDriverName())
            .driverMobile(dispatch.getDriverMobile())
            .deliveryAddressId(dispatch.getDeliveryAddress() != null ? dispatch.getDeliveryAddress().getId() : null)
            .deliveryAddress(deliveryAddress)
            .routeCode(dispatch.getRouteCode())
            .status(dispatch.getStatus())
            .dispatchTime(dispatch.getDispatchTime())
            .deliveryTime(dispatch.getDeliveryTime())
            .deliveredBy(dispatch.getDeliveredBy())
            .receivedByName(dispatch.getReceivedByName())
            .receivedBySignature(dispatch.getReceivedBySignature())
            .deliveryPhotoUrl(dispatch.getDeliveryPhotoUrl())
            .gpsLatitude(dispatch.getGpsLatitude())
            .gpsLongitude(dispatch.getGpsLongitude())
            .remarks(dispatch.getRemarks())
            .createdAt(dispatch.getCreatedAt())
            .createdBy(dispatch.getCreatedBy())
            .updatedAt(dispatch.getUpdatedAt())
            .updatedBy(dispatch.getUpdatedBy())
            .items(dispatch.getItems() != null ? 
                dispatch.getItems().stream()
                    .map(dispatchItemMapper::toResponse)
                    .collect(Collectors.toList()) : null)
            .build();
    }

    private String formatAddress(lk.epicgreen.erp.customer.entity.CustomerAddress address) {
        if (address == null) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        if (address.getAddressLine1() != null) sb.append(address.getAddressLine1());
        if (address.getAddressLine2() != null) sb.append(", ").append(address.getAddressLine2());
        if (address.getCity() != null) sb.append(", ").append(address.getCity());
        if (address.getState() != null) sb.append(", ").append(address.getState());
        if (address.getCountry() != null) sb.append(", ").append(address.getCountry());
        if (address.getPostalCode() != null) sb.append(" - ").append(address.getPostalCode());
        
        return sb.length() > 0 ? sb.toString() : null;
    }
}
