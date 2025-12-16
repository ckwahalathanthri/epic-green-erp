package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.dto.UserRequest;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User Service Implementation
 * Implementation of user service operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public User createUser(UserRequest request) {
        log.info("Creating user: {}", request.getUsername());
        
        // Validate unique fields
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (request.getEmployeeCode() != null && userRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists: " + request.getEmployeeCode());
        }
        
        // Validate password strength
        if (!isPasswordStrong(request.getPassword())) {
            throw new RuntimeException("Password does not meet strength requirements");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmployeeCode(request.getEmployeeCode());
        user.setPhone(request.getPhone());
        user.setMobile(request.getMobile());
        user.setAddress(request.getAddress());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setIsActive(true);
        user.setIsLocked(false);
        user.setEmailVerified(false);
        user.setFailedLoginAttempts(0);
        user.setLoginCount(0);
        
        User saved = userRepository.save(user);
        
        // Assign roles if provided
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            assignRoles(saved.getId(), request.getRoleIds());
        }
        
        // Send verification email
        if (request.getEmail() != null) {
            sendVerificationEmail(saved.getId());
        }
        
        return saved;
    }
    
    @Override
    public User updateUser(Long id, UserRequest request) {
        log.info("Updating user: {}", id);
        User existing = getUserById(id);
        
        // Validate unique fields if changed
        if (!existing.getUsername().equals(request.getUsername()) &&
            userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().equals(existing.getEmail()) &&
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (request.getEmployeeCode() != null && 
            !request.getEmployeeCode().equals(existing.getEmployeeCode()) &&
            userRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new RuntimeException("Employee code already exists: " + request.getEmployeeCode());
        }
        
        existing.setUsername(request.getUsername());
        existing.setEmail(request.getEmail());
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmployeeCode(request.getEmployeeCode());
        existing.setPhone(request.getPhone());
        existing.setMobile(request.getMobile());
        existing.setAddress(request.getAddress());
        existing.setDepartment(request.getDepartment());
        existing.setPosition(request.getPosition());
        existing.setUpdatedAt(LocalDateTime.now());
        
        User updated = userRepository.save(existing);
        
        // Update roles if provided
        if (request.getRoleIds() != null) {
            updateUserRoles(id, request.getRoleIds());
        }
        
        return updated;
    }
    
    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user: {}", id);
        User user = getUserById(id);
        
        if (!canDeleteUser(id)) {
            throw new RuntimeException("Cannot delete user with existing data");
        }
        
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByEmployeeCode(String employeeCode) {
        return userRepository.findByEmployeeCode(employeeCode)
            .orElseThrow(() -> new RuntimeException("User not found with employee code: " + employeeCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword, pageable);
    }
    
    @Override
    public User authenticateUser(String username, String password) {
        log.info("Authenticating user: {}", username);
        User user = getUserByUsername(username);
        
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is not active");
        }
        
        if (user.getIsLocked()) {
            throw new RuntimeException("User account is locked");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            incrementFailedLoginAttempts(user.getId());
            throw new RuntimeException("Invalid credentials");
        }
        
        // Reset failed attempts on successful login
        resetFailedLoginAttempts(user.getId());
        updateLastLogin(user.getId());
        incrementLoginCount(user.getId());
        
        return user;
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("Changing password for user: {}", userId);
        User user = getUserById(userId);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        
        if (!isPasswordStrong(newPassword)) {
            throw new RuntimeException("New password does not meet strength requirements");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void resetPassword(Long userId, String newPassword) {
        log.info("Resetting password for user: {}", userId);
        User user = getUserById(userId);
        
        if (!isPasswordStrong(newPassword)) {
            throw new RuntimeException("New password does not meet strength requirements");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void updateLastLogin(Long userId) {
        User user = getUserById(userId);
        user.setLastLogin(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void incrementLoginCount(Long userId) {
        User user = getUserById(userId);
        user.setLoginCount(user.getLoginCount() + 1);
        userRepository.save(user);
    }
    
    @Override
    public void incrementFailedLoginAttempts(Long userId) {
        User user = getUserById(userId);
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        user.setUpdatedAt(LocalDateTime.now());
        
        // Auto-lock after 5 failed attempts
        if (user.getFailedLoginAttempts() >= 5) {
            user.setIsLocked(true);
            user.setLockedAt(LocalDateTime.now());
            user.setLockReason("Too many failed login attempts");
        }
        
        userRepository.save(user);
    }
    
    @Override
    public void resetFailedLoginAttempts(Long userId) {
        User user = getUserById(userId);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }
    
    @Override
    public void lockUser(Long userId, String lockReason) {
        log.info("Locking user: {}", userId);
        User user = getUserById(userId);
        
        if (!canLockUser(userId)) {
            throw new RuntimeException("Cannot lock this user");
        }
        
        user.setIsLocked(true);
        user.setLockedAt(LocalDateTime.now());
        user.setLockReason(lockReason);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void unlockUser(Long userId) {
        log.info("Unlocking user: {}", userId);
        User user = getUserById(userId);
        
        user.setIsLocked(false);
        user.setLockedAt(null);
        user.setLockReason(null);
        user.setFailedLoginAttempts(0);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean verifyPassword(Long userId, String password) {
        User user = getUserById(userId);
        return passwordEncoder.matches(password, user.getPassword());
    }
    
    @Override
    public void sendVerificationEmail(Long userId) {
        log.info("Sending verification email to user: {}", userId);
        User user = getUserById(userId);
        
        if (user.getEmail() == null) {
            throw new RuntimeException("User does not have an email address");
        }
        
        // Generate verification token
        String token = UUID.randomUUID().toString();
        user.setEmailVerificationToken(token);
        user.setEmailVerificationSentAt(LocalDateTime.now());
        userRepository.save(user);
        
        // TODO: Send actual email with verification link
        log.info("Verification token for user {}: {}", userId, token);
    }
    
    @Override
    public void verifyEmail(Long userId, String verificationToken) {
        log.info("Verifying email for user: {}", userId);
        User user = getUserById(userId);
        
        if (user.getEmailVerificationToken() == null) {
            throw new RuntimeException("No verification token found");
        }
        
        if (!user.getEmailVerificationToken().equals(verificationToken)) {
            throw new RuntimeException("Invalid verification token");
        }
        
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setEmailVerificationToken(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void resendVerificationEmail(Long userId) {
        sendVerificationEmail(userId);
    }
    
    @Override
    public User activateUser(Long userId) {
        log.info("Activating user: {}", userId);
        User user = getUserById(userId);
        user.setIsActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public User deactivateUser(Long userId) {
        log.info("Deactivating user: {}", userId);
        User user = getUserById(userId);
        user.setIsActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public void assignRole(Long userId, Long roleId) {
        User user = getUserById(userId);
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
        
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        
        user.getRoles().add(role);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        User user = getUserById(userId);
        Set<Role> roles = new HashSet<>();
        
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            roles.add(role);
        }
        
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        
        user.getRoles().addAll(roles);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    public void removeRole(Long userId, Long roleId) {
        User user = getUserById(userId);
        
        if (user.getRoles() != null) {
            user.getRoles().removeIf(role -> role.getId().equals(roleId));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    @Override
    public void removeAllRoles(Long userId) {
        User user = getUserById(userId);
        
        if (user.getRoles() != null) {
            user.getRoles().clear();
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
    
    @Override
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        User user = getUserById(userId);
        Set<Role> roles = new HashSet<>();
        
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            roles.add(role);
        }
        
        user.setRoles(roles);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<String> getUserRoles(Long userId) {
        User user = getUserById(userId);
        if (user.getRoles() == null) {
            return new ArrayList<>();
        }
        return user.getRoles().stream()
            .map(Role::getRoleName)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<String> getUserPermissions(Long userId) {
        User user = getUserById(userId);
        if (user.getRoles() == null) {
            return new ArrayList<>();
        }
        return user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(permission -> permission.getPermissionName())
            .distinct()
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(Long userId, String roleName) {
        return getUserRoles(userId).contains(roleName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasPermission(Long userId, String permissionName) {
        return getUserPermissions(userId).contains(permissionName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasAnyRole(Long userId, List<String> roleNames) {
        List<String> userRoles = getUserRoles(userId);
        return roleNames.stream().anyMatch(userRoles::contains);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasAllRoles(Long userId, List<String> roleNames) {
        List<String> userRoles = getUserRoles(userId);
        return userRoles.containsAll(roleNames);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> getActiveUsers(Pageable pageable) {
        return userRepository.findActiveUsers(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getInactiveUsers() {
        return userRepository.findInactiveUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getLockedUsers() {
        return userRepository.findLockedUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithVerifiedEmail() {
        return userRepository.findUsersWithVerifiedEmail();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithUnverifiedEmail() {
        return userRepository.findUsersWithUnverifiedEmail();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWhoNeverLoggedIn() {
        return userRepository.findUsersWhoNeverLoggedIn();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getActiveUsersSince(LocalDateTime afterDate) {
        return userRepository.findActiveUsersSince(afterDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getInactiveUsersSince(LocalDateTime beforeDate) {
        return userRepository.findInactiveUsersSince(beforeDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithFailedLoginAttempts() {
        return userRepository.findUsersWithFailedLoginAttempts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersExceedingFailedLoginThreshold(Integer threshold) {
        return userRepository.findUsersExceedingFailedLoginThreshold(threshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByDepartment(String department) {
        return userRepository.findByDepartment(department);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByPosition(String position) {
        return userRepository.findByPosition(position);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByPermission(String permissionName) {
        return userRepository.findUsersByPermission(permissionName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByDepartmentAndRole(String department, String roleName) {
        return userRepository.findByDepartmentAndRole(department, roleName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithMultipleRoles() {
        return userRepository.findUsersWithMultipleRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithoutRoles() {
        return userRepository.findUsersWithoutRoles();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersRequiringAttention(Integer failedLoginThreshold) {
        return userRepository.findUsersRequiringAttention(failedLoginThreshold);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getRecentUsers(int limit) {
        return userRepository.findRecentUsers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getRecentlyUpdatedUsers(int limit) {
        return userRepository.findRecentlyUpdatedUsers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getRecentlyLoggedInUsers(int limit) {
        return userRepository.findRecentlyLoggedInUsers(PageRequest.of(0, limit));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getMostActiveUsers(int limit) {
        return userRepository.getMostActiveUsers(PageRequest.of(0, limit));
    }
    
    @Override
    public User updateProfile(Long userId, Map<String, Object> profileData) {
        User user = getUserById(userId);
        
        if (profileData.containsKey("firstName")) {
            user.setFirstName((String) profileData.get("firstName"));
        }
        if (profileData.containsKey("lastName")) {
            user.setLastName((String) profileData.get("lastName"));
        }
        if (profileData.containsKey("phone")) {
            user.setPhone((String) profileData.get("phone"));
        }
        if (profileData.containsKey("mobile")) {
            user.setMobile((String) profileData.get("mobile"));
        }
        if (profileData.containsKey("address")) {
            user.setAddress((String) profileData.get("address"));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public User updateContactInfo(Long userId, String phone, String mobile, String address) {
        User user = getUserById(userId);
        user.setPhone(phone);
        user.setMobile(mobile);
        user.setAddress(address);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    public User updateProfilePicture(Long userId, String profilePictureUrl) {
        User user = getUserById(userId);
        user.setProfilePictureUrl(profilePictureUrl);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateUser(User user) {
        return user.getUsername() != null &&
               user.getPassword() != null &&
               user.getFirstName() != null &&
               user.getLastName() != null;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isEmployeeCodeAvailable(String employeeCode) {
        return !userRepository.existsByEmployeeCode(employeeCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canDeleteUser(Long userId) {
        // Can delete if user has no critical data or references
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canLockUser(Long userId) {
        User user = getUserById(userId);
        // Cannot lock if user is admin (has ADMIN role)
        return !hasRole(userId, "ADMIN");
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordStrong(String password) {
        // Password must be at least 8 characters
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Must contain uppercase
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Must contain lowercase
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        // Must contain digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        
        // Must contain special character
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }
    
    @Override
    public List<User> createBulkUsers(List<UserRequest> requests) {
        return requests.stream()
            .map(this::createUser)
            .collect(Collectors.toList());
    }
    
    @Override
    public int activateBulkUsers(List<Long> userIds) {
        int count = 0;
        for (Long id : userIds) {
            try {
                activateUser(id);
                count++;
            } catch (Exception e) {
                log.error("Error activating user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deactivateBulkUsers(List<Long> userIds) {
        int count = 0;
        for (Long id : userIds) {
            try {
                deactivateUser(id);
                count++;
            } catch (Exception e) {
                log.error("Error deactivating user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int lockBulkUsers(List<Long> userIds, String lockReason) {
        int count = 0;
        for (Long id : userIds) {
            try {
                lockUser(id, lockReason);
                count++;
            } catch (Exception e) {
                log.error("Error locking user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int unlockBulkUsers(List<Long> userIds) {
        int count = 0;
        for (Long id : userIds) {
            try {
                unlockUser(id);
                count++;
            } catch (Exception e) {
                log.error("Error unlocking user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int deleteBulkUsers(List<Long> userIds) {
        int count = 0;
        for (Long id : userIds) {
            try {
                deleteUser(id);
                count++;
            } catch (Exception e) {
                log.error("Error deleting user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    public int assignRoleToBulkUsers(List<Long> userIds, Long roleId) {
        int count = 0;
        for (Long id : userIds) {
            try {
                assignRole(id, roleId);
                count++;
            } catch (Exception e) {
                log.error("Error assigning role to user: {}", id, e);
            }
        }
        return count;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", getTotalUsers());
        stats.put("activeUsers", getActiveUsersCount());
        stats.put("inactiveUsers", getInactiveUsersCount());
        stats.put("lockedUsers", getLockedUsersCount());
        stats.put("verifiedUsers", getVerifiedUsersCount());
        stats.put("unverifiedUsers", getUnverifiedUsersCount());
        stats.put("usersWhoNeverLoggedIn", userRepository.countUsersWhoNeverLoggedIn());
        stats.put("usersWithFailedLoginAttempts", userRepository.countUsersWithFailedLoginAttempts());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getDepartmentDistribution() {
        List<Object[]> results = userRepository.getDepartmentDistribution();
        return convertToMapList(results, "department", "userCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPositionDistribution() {
        List<Object[]> results = userRepository.getPositionDistribution();
        return convertToMapList(results, "position", "userCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRoleDistribution() {
        List<Object[]> results = userRepository.getRoleDistribution();
        return convertToMapList(results, "roleName", "userCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUsersByRegistrationMonth() {
        List<Object[]> results = userRepository.getUsersByRegistrationMonth();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put("year", result[0]);
                map.put("month", result[1]);
                map.put("userCount", result[2]);
                return map;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getLoginActivitySummary(int limit) {
        List<Object[]> results = userRepository.getLoginActivitySummary(PageRequest.of(0, limit));
        return convertToMapList(results, "loginDate", "userCount");
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getTotalUsers() {
        return userRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getInactiveUsersCount() {
        return userRepository.countInactiveUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getLockedUsersCount() {
        return userRepository.countLockedUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getVerifiedUsersCount() {
        return userRepository.countUsersWithVerifiedEmail();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getUnverifiedUsersCount() {
        return userRepository.countUsersWithUnverifiedEmail();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> dashboard = new HashMap<>();
        
        dashboard.put("statistics", getUserStatistics());
        dashboard.put("departmentDistribution", getDepartmentDistribution());
        dashboard.put("positionDistribution", getPositionDistribution());
        dashboard.put("roleDistribution", getRoleDistribution());
        dashboard.put("registrationTrend", getUsersByRegistrationMonth());
        dashboard.put("loginActivity", getLoginActivitySummary(30));
        dashboard.put("recentUsers", getRecentUsers(10));
        dashboard.put("mostActiveUsers", getMostActiveUsers(10));
        dashboard.put("usersRequiringAttention", getUsersRequiringAttention(3));
        
        return dashboard;
    }
    
    private List<Map<String, Object>> convertToMapList(List<Object[]> results, String key1, String key2) {
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                map.put(key1, result[0]);
                map.put(key2, result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }
}
