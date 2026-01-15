package lk.epicgreen.erp.admin.service.impl;

import lk.epicgreen.erp.admin.dto.request.UserCreateRequest;
import lk.epicgreen.erp.admin.dto.request.UserUpdateRequest;
import lk.epicgreen.erp.admin.dto.request.ChangePasswordRequest;
import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.mapper.UserMapper;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lk.epicgreen.erp.admin.service.UserService;
import lk.epicgreen.erp.common.exception.ResourceNotFoundException;
import lk.epicgreen.erp.common.exception.DuplicateResourceException;
import lk.epicgreen.erp.common.exception.InvalidOperationException;
import lk.epicgreen.erp.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating new user: {}", request.getUsername());

        // Validate unique constraints
        validateUniqueUsername(request.getUsername(), null);
        validateUniqueEmail(request.getEmail(), null);
        
        if (request.getEmployeeCode() != null) {
            validateUniqueEmployeeCode(request.getEmployeeCode(), null);
        }

        // Create user entity
        User user = userMapper.toEntity(request);
        
        // Encode password
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setFailedLoginAttempts(0);

        // Assign roles
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId)))
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getUsername());

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public List<UserResponse> getAllUsers(){
        List<User> users=userRepository.findAllUsers();
        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user: {}", id);

        User user = findUserById(id);

        // Validate unique constraints
        if (request.getUsername() != null) {
            validateUniqueUsername(request.getUsername(), id);
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            validateUniqueEmail(request.getEmail(), id);
            user.setEmail(request.getEmail());
        }

        if (request.getEmployeeCode() != null) {
            validateUniqueEmployeeCode(request.getEmployeeCode(), id);
            user.setEmployeeCode(request.getEmployeeCode());
        }

        // Update fields
        userMapper.updateEntityFromRequest(request, user);

        // Update roles if provided
        if (request.getRoleIds() != null) {
            Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId)))
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUsername());

        return userMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        log.info("Changing password for user: {}", id);

        User user = findUserById(id);

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidOperationException("Current password is incorrect");
        }

        // Validate new password
        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            throw new InvalidOperationException("New password must be different from current password");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);

        userRepository.save(user);
        log.info("Password changed successfully for user: {}", id);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, String newPassword) {
        log.info("Resetting password for user: {}", id);

        User user = findUserById(id);

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);

        userRepository.save(user);
        log.info("Password reset successfully for user: {}", id);
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        log.info("Activating user: {}", id);

        User user = findUserById(id);
        user.setStatus("ACTIVE");
        user.setFailedLoginAttempts(0);

        userRepository.save(user);
        log.info("User activated successfully: {}", id);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        log.info("Deactivating user: {}", id);

        User user = findUserById(id);
        user.setStatus("INACTIVE");

        userRepository.save(user);
        log.info("User deactivated successfully: {}", id);
    }

    @Override
    @Transactional
    public void suspendUser(Long id) {
        log.info("Suspending user: {}", id);

        User user = findUserById(id);
        user.setStatus("SUSPENDED");

        userRepository.save(user);
        log.info("User suspended successfully: {}", id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);

        User user = findUserById(id);
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus("INACTIVE");

        userRepository.save(user);
        log.info("User deleted successfully: {}", id);
    }

    @Override
    @Transactional
    public void recordLoginAttempt(String username, boolean successful) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (successful) {
            user.setLastLoginAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
        } else {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            
            // Auto-suspend after 5 failed attempts
            if (user.getFailedLoginAttempts() >= 5) {
                user.setStatus("SUSPENDED");
                log.warn("User suspended due to failed login attempts: {}", username);
            }
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        log.info("Assigning role {} to user {}", roleId, userId);

        User user = findUserById(userId);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId));

        user.getRoles().add(role);
        userRepository.save(user);

        log.info("Role assigned successfully");
    }

    @Override
    @Transactional
    public void removeRole(Long userId, Long roleId) {
        log.info("Removing role {} from user {}", roleId, userId);

        User user = findUserById(userId);
        user.getRoles().removeIf(role -> role.getId().equals(roleId));
        userRepository.save(user);

        log.info("Role removed successfully");
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllActiveUsers() {
        List<User> users = userRepository.findByStatusAndDeletedAtIsNull("ACTIVE");
        return users.stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findByDeletedAtIsNull(pageable);
        return createPageResponse(userPage);
    }

    @Override
    public PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable) {
        Page<User> userPage = userRepository.searchUsers(keyword,null,null,null,null, pageable);
        return createPageResponse(userPage);
    }

    @Override
    public PageResponse<UserResponse> getUsersByStatus(String status, Pageable pageable) {
        Page<User> userPage = userRepository.findByStatusAndDeletedAtIsNull(status, pageable);
        return createPageResponse(userPage);
    }

    @Override
    public List<UserResponse> getUsersByRole(Long roleId) {
        List<User> users = userRepository.findByRolesId(roleId);
        return users.stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private User findUserById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private void validateUniqueUsername(String username, Long excludeId) {
        if (excludeId == null) {
            if (userRepository.existsByUsername(username)) {
                throw new DuplicateResourceException("Username already exists: " + username);
            }
        } else {
            if (userRepository.existsByUsernameAndIdNot(username, excludeId)) {
                throw new DuplicateResourceException("Username already exists: " + username);
            }
        }
    }

    private void validateUniqueEmail(String email, Long excludeId) {
        if (excludeId == null) {
            if (userRepository.existsByEmail(email)) {
                throw new DuplicateResourceException("Email already exists: " + email);
            }
        } else {
            if (userRepository.existsByEmailAndIdNot(email, excludeId)) {
                throw new DuplicateResourceException("Email already exists: " + email);
            }
        }
    }

    private void validateUniqueEmployeeCode(String employeeCode, Long excludeId) {
        if (excludeId == null) {
            if (userRepository.existsByEmployeeCode(employeeCode)) {
                throw new DuplicateResourceException("Employee code already exists: " + employeeCode);
            }
        } else {
            if (userRepository.existsByEmployeeCodeAndIdNot(employeeCode, excludeId)) {
                throw new DuplicateResourceException("Employee code already exists: " + employeeCode);
            }
        }
    }

    private PageResponse<UserResponse> createPageResponse(Page<User> userPage) {
        List<UserResponse> content = userPage.getContent().stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
            .content(content)
            .pageNumber(userPage.getNumber())
            .pageSize(userPage.getSize())
            .totalElements(userPage.getTotalElements())
            .totalPages(userPage.getTotalPages())
            .last(userPage.isLast())
            .first(userPage.isFirst())
            .empty(userPage.isEmpty())
            .build();
    }
}
