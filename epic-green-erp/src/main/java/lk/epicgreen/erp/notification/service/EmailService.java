package lk.epicgreen.erp.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Email Service
 * Service for sending email notifications
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Value("${app.mail.from:noreply@epicgreen.lk}")
    private String defaultFromEmail;
    
    @Value("${app.mail.from-name:Epic Green}")
    private String defaultFromName;
    
    @Value("${app.mail.enabled:true}")
    private boolean emailEnabled;
    
    // ===================================================================
    // SIMPLE EMAIL SENDING
    // ===================================================================
    
    /**
     * Send simple text email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email not sent to: {}", to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(defaultFromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Simple email sent successfully to: {}", to);
            
        } catch (MailException e) {
            log.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send simple email to multiple recipients
     */
    public void sendSimpleEmail(List<String> to, String subject, String text) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email not sent to: {}", to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(defaultFromEmail);
            message.setTo(to.toArray(new String[0]));
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            log.info("Simple email sent successfully to {} recipients", to.size());
            
        } catch (MailException e) {
            log.error("Failed to send simple email to multiple recipients", e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    // ===================================================================
    // HTML EMAIL SENDING
    // ===================================================================
    
    /**
     * Send HTML email
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. HTML email not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            
            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send HTML email with plain text fallback
     */
    public void sendHtmlEmail(String to, String subject, String text, String htmlContent) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. HTML email not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, htmlContent); // text, html
            
            mailSender.send(mimeMessage);
            log.info("HTML email with fallback sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }
    
    // ===================================================================
    // EMAIL WITH ATTACHMENTS
    // ===================================================================
    
    /**
     * Send email with attachment
     */
    public void sendEmailWithAttachment(String to, String subject, String text, 
                                       String attachmentPath) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email with attachment not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            
            // Add attachment
            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
            helper.addAttachment(file.getFilename(), file);
            
            mailSender.send(mimeMessage);
            log.info("Email with attachment sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send email with attachment to: {}", to, e);
            throw new RuntimeException("Failed to send email with attachment: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send email with multiple attachments
     */
    public void sendEmailWithAttachments(String to, String subject, String text, 
                                        List<String> attachmentPaths) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email with attachments not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            
            // Add attachments
            for (String attachmentPath : attachmentPaths) {
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                helper.addAttachment(file.getFilename(), file);
            }
            
            mailSender.send(mimeMessage);
            log.info("Email with {} attachments sent successfully to: {}", 
                attachmentPaths.size(), to);
            
        } catch (Exception e) {
            log.error("Failed to send email with attachments to: {}", to, e);
            throw new RuntimeException("Failed to send email with attachments: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send HTML email with attachments
     */
    public void sendHtmlEmailWithAttachments(String to, String subject, String htmlContent, 
                                            List<String> attachmentPaths) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. HTML email with attachments not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            
            // Add attachments
            if (attachmentPaths != null && !attachmentPaths.isEmpty()) {
                for (String attachmentPath : attachmentPaths) {
                    FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                    helper.addAttachment(file.getFilename(), file);
                }
            }
            
            mailSender.send(mimeMessage);
            log.info("HTML email with attachments sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send HTML email with attachments to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email with attachments: " + e.getMessage(), e);
        }
    }
    
    // ===================================================================
    // ADVANCED EMAIL SENDING
    // ===================================================================
    
    /**
     * Send email with CC and BCC
     */
    public void sendEmailWithCcBcc(String to, String subject, String text, 
                                   String[] cc, String[] bcc) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email with CC/BCC not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }
            
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }
            
            helper.setSubject(subject);
            helper.setText(text);
            
            mailSender.send(mimeMessage);
            log.info("Email with CC/BCC sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send email with CC/BCC to: {}", to, e);
            throw new RuntimeException("Failed to send email with CC/BCC: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send email with custom from address
     */
    public void sendEmailWithCustomFrom(String from, String fromName, String to, 
                                       String subject, String text) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(from, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            
            mailSender.send(mimeMessage);
            log.info("Email sent successfully from {} to: {}", from, to);
            
        } catch (Exception e) {
            log.error("Failed to send email from {} to: {}", from, to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send email with reply-to
     */
    public void sendEmailWithReplyTo(String to, String subject, String text, String replyTo) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email not sent to: {}", to);
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, 
                StandardCharsets.UTF_8.name());
            
            helper.setFrom(defaultFromEmail, defaultFromName);
            helper.setTo(to);
            helper.setReplyTo(replyTo);
            helper.setSubject(subject);
            helper.setText(text);
            
            mailSender.send(mimeMessage);
            log.info("Email with reply-to sent successfully to: {}", to);
            
        } catch (Exception e) {
            log.error("Failed to send email with reply-to to: {}", to, e);
            throw new RuntimeException("Failed to send email with reply-to: " + e.getMessage(), e);
        }
    }
    
    // ===================================================================
    // COMPREHENSIVE EMAIL SENDING
    // ===================================================================
    
    /**
     * Send comprehensive email with all options
     */
    public void sendEmail(EmailRequest emailRequest) {
        if (!emailEnabled) {
            log.warn("Email sending is disabled. Email not sent to: {}", emailRequest.getTo());
            return;
        }
        
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, 
                StandardCharsets.UTF_8.name());
            
            // From
            if (emailRequest.getFrom() != null) {
                helper.setFrom(emailRequest.getFrom(), 
                    emailRequest.getFromName() != null ? emailRequest.getFromName() : defaultFromName);
            } else {
                helper.setFrom(defaultFromEmail, defaultFromName);
            }
            
            // To
            helper.setTo(emailRequest.getTo());
            
            // CC
            if (emailRequest.getCc() != null && emailRequest.getCc().length > 0) {
                helper.setCc(emailRequest.getCc());
            }
            
            // BCC
            if (emailRequest.getBcc() != null && emailRequest.getBcc().length > 0) {
                helper.setBcc(emailRequest.getBcc());
            }
            
            // Reply-To
            if (emailRequest.getReplyTo() != null) {
                helper.setReplyTo(emailRequest.getReplyTo());
            }
            
            // Subject
            helper.setSubject(emailRequest.getSubject());
            
            // Body
            if (emailRequest.getHtmlBody() != null) {
                if (emailRequest.getTextBody() != null) {
                    helper.setText(emailRequest.getTextBody(), emailRequest.getHtmlBody());
                } else {
                    helper.setText(emailRequest.getHtmlBody(), true);
                }
            } else {
                helper.setText(emailRequest.getTextBody());
            }
            
            // Attachments
            if (emailRequest.getAttachments() != null && !emailRequest.getAttachments().isEmpty()) {
                for (String attachmentPath : emailRequest.getAttachments()) {
                    FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                    helper.addAttachment(file.getFilename(), file);
                }
            }
            
            mailSender.send(mimeMessage);
            log.info("Comprehensive email sent successfully to: {}", emailRequest.getTo());
            
        } catch (Exception e) {
            log.error("Failed to send comprehensive email to: {}", emailRequest.getTo(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
    
    // ===================================================================
    // EMAIL REQUEST CLASS
    // ===================================================================
    
    /**
     * Email Request DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class EmailRequest {
        private String from;
        private String fromName;
        private String to;
        private String[] cc;
        private String[] bcc;
        private String replyTo;
        private String subject;
        private String textBody;
        private String htmlBody;
        private List<String> attachments;
    }
}
