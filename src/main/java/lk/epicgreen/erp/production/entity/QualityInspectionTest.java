package lk.epicgreen.erp.production.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quality_inspection_tests")
@Data
public class QualityInspectionTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    private QualityInspection qualityInspection;
    
    @Column(name = "test_parameter", nullable = false, length = 200)
    private String testParameter;
    
    @Column(name = "expected_value", precision = 15, scale = 4)
    private BigDecimal expectedValue;
    
    @Column(name = "actual_value", precision = 15, scale = 4)
    private BigDecimal actualValue;
    
    @Column(name = "tolerance", precision = 15, scale = 4)
    private BigDecimal tolerance;
    
    @Column(name = "unit", length = 50)
    private String unit;
    
    @Column(name = "test_result", length = 50)
    private String testResult; // PASS, FAIL, CONDITIONAL
    
    @Column(name = "test_method", columnDefinition = "TEXT")
    private String testMethod;
    
    @Column(name = "tested_by", length = 100)
    private String testedBy;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    @PreUpdate
    public void evaluateResult() {
        if (this.actualValue != null && this.expectedValue != null) {
            BigDecimal variance = this.actualValue.subtract(this.expectedValue).abs();
            if (this.tolerance != null) {
                if (variance.compareTo(this.tolerance) <= 0) {
                    this.testResult = "PASS";
                } else {
                    this.testResult = "FAIL";
                }
            }
        }
    }
}