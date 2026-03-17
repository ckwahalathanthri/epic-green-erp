package lk.epicgreen.erp.product.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Raw Material Entity
 */
@Entity
@DiscriminatorValue("RAW_MATERIAL")
@Data
@EqualsAndHashCode(callSuper = true)
public class RawMaterial extends Product {
    
    @Column(name = "origin_country")
    private String originCountry;
    
    @Column(name = "supplier_code")
    private String supplierCode;
    
    @Column(name = "quality_grade")
    private String qualityGrade;
    
    @Column(name = "shelf_life_days")
    private Integer shelfLifeDays;
}
