package lk.epicgreen.erp.sales.dto.response;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VehicleDTO {
    private Long id;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleModel;
    private String vehicleMake;
    private Integer year;
    private BigDecimal capacity;
    private String capacityUnit;
    private String vehicleStatus;
    private String registrationNumber;
    private String insuranceNumber;
    private LocalDate insuranceExpiry;
    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;
    private String fuelType;
    private Boolean gpsEnabled;
    private String notes;
}