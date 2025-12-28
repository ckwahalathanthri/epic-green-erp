package lk.epicgreen.erp.audit.mapper;

import lk.epicgreen.erp.audit.dto.request.ErrorLogRequest;
import lk.epicgreen.erp.audit.dto.response.ErrorLogResponse;
import lk.epicgreen.erp.audit.entity.ErrorLog;
import org.springframework.stereotype.Component;

/**
 * Mapper for ErrorLog entity and DTOs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class ErrorLogMapper {

    public ErrorLog toEntity(ErrorLogRequest request) {
        if (request == null) {
            return null;
        }

        return ErrorLog.builder()
            .errorType(request.getErrorType())
            .errorCode(request.getErrorCode())
            .errorMessage(request.getErrorMessage())
            .stackTrace(request.getStackTrace())
            .requestUrl(request.getRequestUrl())
            .requestMethod(request.getRequestMethod())
            .requestBody(request.getRequestBody())
            .userId(request.getUserId())
            .ipAddress(request.getIpAddress())
            .userAgent(request.getUserAgent())
            .severity(request.getSeverity() != null ? request.getSeverity() : "MEDIUM")
            .isResolved(request.getIsResolved() != null ? request.getIsResolved() : false)
            .resolvedBy(request.getResolvedBy())
            .build();
    }

    public void updateEntityFromRequest(ErrorLogRequest request, ErrorLog errorLog) {
        if (request == null || errorLog == null) {
            return;
        }

        errorLog.setErrorType(request.getErrorType());
        errorLog.setErrorCode(request.getErrorCode());
        errorLog.setErrorMessage(request.getErrorMessage());
        errorLog.setStackTrace(request.getStackTrace());
        errorLog.setRequestUrl(request.getRequestUrl());
        errorLog.setRequestMethod(request.getRequestMethod());
        errorLog.setRequestBody(request.getRequestBody());
        errorLog.setUserId(request.getUserId());
        errorLog.setIpAddress(request.getIpAddress());
        errorLog.setUserAgent(request.getUserAgent());
        errorLog.setSeverity(request.getSeverity());
        errorLog.setIsResolved(request.getIsResolved());
        errorLog.setResolvedBy(request.getResolvedBy());
    }

    public ErrorLogResponse toResponse(ErrorLog errorLog) {
        if (errorLog == null) {
            return null;
        }

        return ErrorLogResponse.builder()
            .id(errorLog.getId())
            .errorType(errorLog.getErrorType())
            .errorCode(errorLog.getErrorCode())
            .errorMessage(errorLog.getErrorMessage())
            .stackTrace(errorLog.getStackTrace())
            .requestUrl(errorLog.getRequestUrl())
            .requestMethod(errorLog.getRequestMethod())
            .requestBody(errorLog.getRequestBody())
            .userId(errorLog.getUserId())
            .ipAddress(errorLog.getIpAddress())
            .userAgent(errorLog.getUserAgent())
            .severity(errorLog.getSeverity())
            .isResolved(errorLog.getIsResolved())
            .resolvedBy(errorLog.getResolvedBy())
            .resolvedAt(errorLog.getResolvedAt())
            .createdAt(errorLog.getCreatedAt())
            .build();
    }
}
