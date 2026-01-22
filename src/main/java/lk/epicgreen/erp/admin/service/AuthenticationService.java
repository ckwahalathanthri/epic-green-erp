package lk.epicgreen.erp.admin.service;

import lk.epicgreen.erp.admin.config.JwtService;
import lk.epicgreen.erp.admin.dto.request.ChangePasswordRequest;
import lk.epicgreen.erp.admin.dto.request.LoginRequest;
import lk.epicgreen.erp.admin.dto.request.RegistrationRequest;
import lk.epicgreen.erp.admin.dto.request.ResetPasswordRequest;
import lk.epicgreen.erp.admin.dto.response.AuthenticationResponse;
import lk.epicgreen.erp.admin.entity.Role;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.entity.UserRole;
import lk.epicgreen.erp.admin.repository.RoleRepository;
import lk.epicgreen.erp.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
   @Autowired

   private final AuthenticationManager authenticationManager;
   private final JwtService jwtService;
   private final UserDetailService userService;
   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final PasswordEncoder passwordEncoder;

   public AuthenticationResponse login(LoginRequest request) {
       authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
       );

       UserDetails user = userService.loadUserByUsername(request.getUsername());

       String jwtToken = jwtService.generateToken(user);
       String refreshToken = jwtService.generateRefreshToken(user);

       return AuthenticationResponse.builder()
           .accessToken(jwtToken)
           .refreshToken(refreshToken)
           .build();
   }

   public void register(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword())) 
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .employeeCode(request.getEmployeeCode())
                .status("ACTIVE")
                .build();

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleCode("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("Error: Role 'EMPLOYEE' is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(roleCode -> {
                Role role = roleRepository.findByRoleCode(roleCode)
                    .orElseThrow(() -> new RuntimeException("Error: Role '" + roleCode + "' is not found."));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

   public void logout(String token) {
       // TODO: Implement logout logic
   }

   public AuthenticationResponse refreshToken(String refreshToken) {
       // TODO: Implement refresh token logic
       return null;
   }

   public boolean validateToken(String token) {
       // TODO: Implement token validation logic
       return false;
   }

   public Map<String, Object> getCurrentUser() {
       // TODO: Implement get current user logic
       return Collections.emptyMap();
   }

   public void changePassword(ChangePasswordRequest request) {
       // TODO: Implement change password logic
   }

   public void resetPassword(ResetPasswordRequest request) {
       // TODO: Implement reset password logic
   }

   public void forgotPassword(String email) {
       // TODO: Implement forgot password logic
   }

   public void resetPasswordWithToken(String token, String newPassword) {
       // TODO: Implement reset password with token logic
   }

   public boolean hasPermission(String permissionName) {
       // TODO: Implement permission check
       return false;
   }

   public boolean hasRole(String roleName) {
       // TODO: Implement role check
       return false;
   }

   public List<String> getCurrentUserRoles() {
       // TODO: Implement get user roles
       return Collections.emptyList();
   }

   public List<String> getCurrentUserPermissions() {
       // TODO: Implement get user permissions
       return Collections.emptyList();
   }

   public List<Map<String, Object>> getActiveSessions() {
       // TODO: Implement get active sessions
       return Collections.emptyList();
   }

   public void invalidateSession(String sessionId) {
       // TODO: Implement session invalidation
   }

   public void invalidateAllOtherSessions() {
       // TODO: Implement invalidate all other sessions
   }
}