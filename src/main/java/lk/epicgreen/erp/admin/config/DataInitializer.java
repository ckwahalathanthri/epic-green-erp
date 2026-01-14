package lk.epicgreen.erp.admin.config;

import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        List<String> roles = Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE", "HR_MANAGER", "FINANCE_OFFICER");

        for (String roleCode : roles) {
            if (!roleRepository.findByRoleCode(roleCode).isPresent()) {
                Role role = Role.builder()
                        .roleName(formatRoleName(roleCode))
                        .roleCode(roleCode)
                        .description("System generated role for " + roleCode)
                        .isSystemRole(true)
                        .build();
                roleRepository.save(role);
            }
        }
    }

    private String formatRoleName(String roleCode) {
        return roleCode.replace("_", " ").toLowerCase()
                .substring(0, 1).toUpperCase() + 
                roleCode.replace("_", " ").toLowerCase().substring(1);
    }
}