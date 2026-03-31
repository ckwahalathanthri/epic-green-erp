package lk.epicgreen.erp.supplier.repository;

import lk.epicgreen.erp.supplier.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {

    List<SupplierPayment> findAllByPaymentStatus(String status);
}