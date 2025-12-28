package lk.epicgreen.erp.admin.mapper;

import lk.epicgreen.erp.admin.dto.request.UserCreateRequest;
import lk.epicgreen.erp.admin.dto.request.UserUpdateRequest;
import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper for User entity and DTOs
 */
@Component
public class UserMapper {

    private final RoleMapper roleMapper;

    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public User toEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

        return User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .mobileNumber(request.getMobileNumber())
            .employeeCode(request.getEmployeeCode())
            .build();
    }

    public void updateEntityFromRequest(UserUpdateRequest request, User user) {
        if (request == null || user == null) {
            return;
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getMobileNumber() != null) {
            user.setMobileNumber(request.getMobileNumber());
        }
    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .mobileNumber(user.getMobileNumber())
            .employeeCode(user.getEmployeeCode())
            .status(user.getStatus())
            .lastLoginAt(user.getLastLoginAt())
            .failedLoginAttempts(user.getFailedLoginAttempts())
            .roles(user.getRoles() != null ? 
                user.getRoles().stream()
                    .map(roleMapper::toResponse)
                    .collect(Collectors.toSet()) : 
                null)
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
