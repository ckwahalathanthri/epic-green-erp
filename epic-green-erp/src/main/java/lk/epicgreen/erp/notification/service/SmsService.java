package lk.epicgreen.erp.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SMS Service
 * Service for sending SMS notifications
 * 
 * Integration with SMS providers (Twilio, Dialog, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${app.sms.provider:TWILIO}")
    private String smsProvider;
    
    @Value("${app.sms.enabled:true}")
    private boolean smsEnabled;
    
    // Twilio configuration
    @Value("${app.sms.twilio.account-sid:}")
    private String twilioAccountSid;
    
    @Value("${app.sms.twilio.auth-token:}")
    private String twilioAuthToken;
    
    @Value("${app.sms.twilio.from-number:}")
    private String twilioFromNumber;
    
    @Value("${app.sms.twilio.api-url:https://api.twilio.com/2010-04-01/Accounts}")
    private String twilioApiUrl;
    
    // Dialog SMS configuration (Sri Lanka)
    @Value("${app.sms.dialog.api-key:}")
    private String dialogApiKey;
    
    @Value("${app.sms.dialog.api-url:https://smsapi.dialog.lk/api/sms/send}")
    private String dialogApiUrl;
    
    @Value("${app.sms.dialog.sender-id:EpicGreen}")
    private String dialogSenderId;
    
    // Generic SMS configuration
    @Value("${app.sms.from-number:}")
    private String defaultFromNumber;
    
    // ===================================================================
    // SIMPLE SMS SENDING
    // ===================================================================
    
    /**
     * Send simple SMS
     */
    public SmsResponse sendSms(String to, String message) {
        if (!smsEnabled) {
            log.warn("SMS sending is disabled. SMS not sent to: {}", to);
            return SmsResponse.builder()
                .success(false)
                .message("SMS sending is disabled")
                .build();
        }
        
        // Route to appropriate provider
        switch (smsProvider.toUpperCase()) {
            case "TWILIO":
                return sendViaTwilio(to, message);
            case "DIALOG":
                return sendViaDialog(to, message);
            default:
                log.error("Unknown SMS provider: {}", smsProvider);
                return SmsResponse.builder()
                    .success(false)
                    .message("Unknown SMS provider: " + smsProvider)
                    .build();
        }
    }
    
    /**
     * Send SMS to multiple recipients
     */
    public List<SmsResponse> sendSms(List<String> toNumbers, String message) {
        if (!smsEnabled) {
            log.warn("SMS sending is disabled. SMS not sent to {} recipients", toNumbers.size());
            return toNumbers.stream()
                .map(to -> SmsResponse.builder()
                    .to(to)
                    .success(false)
                    .message("SMS sending is disabled")
                    .build())
                .toList();
        }
        
        return toNumbers.stream()
            .map(to -> sendSms(to, message))
            .toList();
    }
    
    // ===================================================================
    // TWILIO INTEGRATION
    // ===================================================================
    
    /**
     * Send SMS via Twilio
     */
    private SmsResponse sendViaTwilio(String to, String message) {
        try {
            log.info("Sending SMS via Twilio to: {}", to);
            
            // Validate configuration
            if (twilioAccountSid == null || twilioAccountSid.isEmpty() ||
                twilioAuthToken == null || twilioAuthToken.isEmpty() ||
                twilioFromNumber == null || twilioFromNumber.isEmpty()) {
                log.error("Twilio configuration is incomplete");
                return SmsResponse.builder()
                    .to(to)
                    .success(false)
                    .message("Twilio configuration is incomplete")
                    .build();
            }
            
            // Prepare request
            String url = twilioApiUrl + "/" + twilioAccountSid + "/Messages.json";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(twilioAccountSid, twilioAuthToken);
            
            String body = "To=" + to + "&From=" + twilioFromNumber + "&Body=" + message;
            
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            
            // Send request
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.CREATED || 
                response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                String messageSid = responseBody != null ? 
                    (String) responseBody.get("sid") : null;
                
                log.info("SMS sent successfully via Twilio to: {}, SID: {}", to, messageSid);
                
                return SmsResponse.builder()
                    .to(to)
                    .success(true)
                    .message("SMS sent successfully")
                    .provider("TWILIO")
                    .providerMessageId(messageSid)
                    .providerResponse(responseBody != null ? responseBody.toString() : null)
                    .build();
            } else {
                log.error("Failed to send SMS via Twilio. Status: {}", response.getStatusCode());
                return SmsResponse.builder()
                    .to(to)
                    .success(false)
                    .message("Failed to send SMS. Status: " + response.getStatusCode())
                    .provider("TWILIO")
                    .build();
            }
            
        } catch (Exception e) {
            log.error("Error sending SMS via Twilio to: {}", to, e);
            return SmsResponse.builder()
                .to(to)
                .success(false)
                .message("Error: " + e.getMessage())
                .provider("TWILIO")
                .errorDetails(e.toString())
                .build();
        }
    }
    
    // ===================================================================
    // DIALOG SMS INTEGRATION (Sri Lanka)
    // ===================================================================
    
    /**
     * Send SMS via Dialog (Sri Lanka)
     */
    private SmsResponse sendViaDialog(String to, String message) {
        try {
            log.info("Sending SMS via Dialog to: {}", to);
            
            // Validate configuration
            if (dialogApiKey == null || dialogApiKey.isEmpty()) {
                log.error("Dialog SMS configuration is incomplete");
                return SmsResponse.builder()
                    .to(to)
                    .success(false)
                    .message("Dialog SMS configuration is incomplete")
                    .build();
            }
            
            // Prepare request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + dialogApiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("destination", to);
            requestBody.put("message", message);
            requestBody.put("sender_id", dialogSenderId);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Send request
            ResponseEntity<Map> response = restTemplate.exchange(
                dialogApiUrl, 
                HttpMethod.POST, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK || 
                response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                String messageId = responseBody != null ? 
                    (String) responseBody.get("message_id") : null;
                
                log.info("SMS sent successfully via Dialog to: {}, Message ID: {}", to, messageId);
                
                return SmsResponse.builder()
                    .to(to)
                    .success(true)
                    .message("SMS sent successfully")
                    .provider("DIALOG")
                    .providerMessageId(messageId)
                    .providerResponse(responseBody != null ? responseBody.toString() : null)
                    .build();
            } else {
                log.error("Failed to send SMS via Dialog. Status: {}", response.getStatusCode());
                return SmsResponse.builder()
                    .to(to)
                    .success(false)
                    .message("Failed to send SMS. Status: " + response.getStatusCode())
                    .provider("DIALOG")
                    .build();
            }
            
        } catch (Exception e) {
            log.error("Error sending SMS via Dialog to: {}", to, e);
            return SmsResponse.builder()
                .to(to)
                .success(false)
                .message("Error: " + e.getMessage())
                .provider("DIALOG")
                .errorDetails(e.toString())
                .build();
        }
    }
    
    // ===================================================================
    // ADVANCED SMS SENDING
    // ===================================================================
    
    /**
     * Send SMS with custom from number
     */
    public SmsResponse sendSms(String from, String to, String message) {
        if (!smsEnabled) {
            log.warn("SMS sending is disabled. SMS not sent to: {}", to);
            return SmsResponse.builder()
                .success(false)
                .message("SMS sending is disabled")
                .build();
        }
        
        // For Twilio, temporarily override from number
        String originalFrom = twilioFromNumber;
        twilioFromNumber = from;
        
        SmsResponse response = sendSms(to, message);
        
        // Restore original from number
        twilioFromNumber = originalFrom;
        
        return response;
    }
    
    /**
     * Send SMS with retry
     */
    public SmsResponse sendSmsWithRetry(String to, String message, int maxRetries) {
        SmsResponse response = null;
        
        for (int i = 0; i < maxRetries; i++) {
            response = sendSms(to, message);
            
            if (response.isSuccess()) {
                return response;
            }
            
            log.warn("SMS sending failed. Retry {} of {}", i + 1, maxRetries);
            
            // Wait before retry (exponential backoff)
            if (i < maxRetries - 1) {
                try {
                    Thread.sleep((long) Math.pow(2, i) * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        return response;
    }
    
    /**
     * Validate phone number
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        
        // Remove all non-digit characters
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        
        // Check length (international format: 10-15 digits)
        if (digitsOnly.length() < 10 || digitsOnly.length() > 15) {
            return false;
        }
        
        // Sri Lankan mobile numbers start with 07 or +947
        if (phoneNumber.startsWith("07") || phoneNumber.startsWith("+947")) {
            return digitsOnly.length() >= 10;
        }
        
        return true;
    }
    
    /**
     * Format phone number to international format
     */
    public String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }
        
        // Remove all non-digit characters
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");
        
        // Sri Lankan numbers
        if (phoneNumber.startsWith("07")) {
            return "+94" + digitsOnly.substring(1);
        } else if (phoneNumber.startsWith("0")) {
            return "+94" + digitsOnly.substring(1);
        } else if (!phoneNumber.startsWith("+")) {
            return "+" + digitsOnly;
        }
        
        return phoneNumber;
    }
    
    // ===================================================================
    // SMS RESPONSE CLASS
    // ===================================================================
    
    /**
     * SMS Response DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class SmsResponse {
        private String to;
        private boolean success;
        private String message;
        private String provider;
        private String providerMessageId;
        private String providerResponse;
        private String errorDetails;
    }
}
