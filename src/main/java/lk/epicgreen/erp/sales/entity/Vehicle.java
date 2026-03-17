package lk.epicgreen.erp.sales.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicle_number", columnList = "vehicle_number"),
    @Index(name = "idx_vehicle_status", columnList = "vehicle_status")
})
@Data
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vehicle_number", unique = true, nullable = false, length = 50)
    private String vehicleNumber; // License plate
    
    @Column(name = "vehicle_type", length = 50, nullable = false)
    private String vehicleType; // TRUCK, VAN, PICKUP, MOTORCYCLE
    
    @Column(name = "vehicle_model", length = 100)
    private String vehicleModel;
    
    @Column(name = "vehicle_make", length = 100)
    private String vehicleMake;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "capacity", precision = 15, scale = 3)
    private java.math.BigDecimal capacity;
    
    @Column(name = "capacity_unit", length = 20)
    private String capacityUnit; // kg, tons, cubic_meters
    
    @Column(name = "vehicle_status", length = 20, nullable = false)
    private String vehicleStatus; // ACTIVE, MAINTENANCE, INACTIVE
    
    @Column(name = "registration_number", length = 50)
    private String registrationNumber;
    
    @Column(name = "insurance_number", length = 50)
    private String insuranceNumber;
    
    @Column(name = "insurance_expiry")
    private LocalDate insuranceExpiry;
    
    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;
    
    @Column(name = "next_service_date")
    private LocalDate nextServiceDate;
    
    @Column(name = "fuel_type", length = 20)
    private String fuelType; // PETROL, DIESEL, ELECTRIC
    
    @Column(name = "gps_enabled")
    private Boolean gpsEnabled = false;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    @PreUpdate
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
}