package lk.epicgreen.erp.sales.service.impl;


import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.payment.entity.SalesReturn;
import lk.epicgreen.erp.payment.repository.SalesReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesReturnService {
    
    private final SalesReturnRepository returnRepository;
    private final UserRepository userRepository;
    
    public List<SalesReturn> getAllReturns() {
        return returnRepository.findAll();
    }
    
    public SalesReturn getReturnById(Long id) {
        return returnRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Return not found: " + id));
    }
    
    public SalesReturn createReturn(SalesReturn salesReturn) {
        if (salesReturn.getReturnNumber() == null) {
            salesReturn.setReturnNumber(generateReturnNumber());
        }
        return returnRepository.save(salesReturn);
    }
    
    public SalesReturn approveReturn(Long id, String approver) {
        SalesReturn salesReturn = getReturnById(id);
        User user=userRepository.findByUsername(approver)
                        .orElseThrow(()->new RuntimeException("User not found: " + approver));
        salesReturn.approve(user);
        return returnRepository.save(salesReturn);
    }
    
    private String generateReturnNumber() {
        long count = returnRepository.count() + 1;
        return String.format("RET-%s-%04d", LocalDate.now().getYear(), count);
    }
}