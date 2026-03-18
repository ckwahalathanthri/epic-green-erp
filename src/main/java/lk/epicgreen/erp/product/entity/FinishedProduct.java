package lk.epicgreen.erp.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Finished Product Entity
 */
@Entity
@DiscriminatorValue("FINISHED_PRODUCT")
@Data
@EqualsAndHashCode(callSuper = true)
public class FinishedProduct extends Product {
    
    @Column(name = "batch_size")
    private Integer batchSize;
    
    @Column(name = "production_time_minutes")
    private Integer productionTimeMinutes;
    
    @Column(name = "packaging_type")
    private String packagingType;
    
    @Column(name = "expiry_days")
    private Integer expiryDays;
}
