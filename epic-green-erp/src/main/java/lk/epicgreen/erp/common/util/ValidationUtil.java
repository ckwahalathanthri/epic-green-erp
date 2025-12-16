package lk.epicgreen.erp.common.util;

import lk.epicgreen.erp.common.constants.AppConstants;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(AppConstants.EMAIL_PATTERN);
    private static final Pattern PHONE_PATTERN = Pattern.compile(AppConstants.PHONE_PATTERN);
    private static final Pattern MOBILE_PATTERN = Pattern.compile(AppConstants.MOBILE_PATTERN);
    private static final Pattern CODE_PATTERN = Pattern.compile(AppConstants.CODE_PATTERN);
    private static final Pattern USERNAME_PATTERN = Pattern.compile(AppConstants.USERNAME_PATTERN);
    private static final Pattern DECIMAL_PATTERN = Pattern.compile(AppConstants.DECIMAL_PATTERN);
    
    // Prevent instantiation
    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== String Validation ====================
    
    /**
     * Validates if string is not null and not empty
     * 
     * @param value String to validate
     * @return true if valid
     */
    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validates string length
     * 
     * @param value String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if within range
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) return false;
        int length = value.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validates minimum length
     * 
     * @param value String to validate
     * @param minLength Minimum length
     * @return true if meets minimum
     */
    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.length() >= minLength;
    }
    
    /**
     * Validates maximum length
     * 
     * @param value String to validate
     * @param maxLength Maximum length
     * @return true if within maximum
     */
    public static boolean hasMaxLength(String value, int maxLength) {
        return value == null || value.length() <= maxLength;
    }
    
    // ==================== Email Validation ====================
    
    /**
     * Validates email format
     * 
     * @param email Email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    // ==================== Phone Validation ====================
    
    /**
     * Validates phone number format
     * 
     * @param phone Phone to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String cleaned = phone.replaceAll("[\\s-()]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }
    
    /**
     * Validates mobile number format (10 digits)
     * 
     * @param mobile Mobile to validate
     * @return true if valid mobile format
     */
    public static boolean isValidMobile(String mobile) {
        if (mobile == null) return false;
        String cleaned = mobile.replaceAll("\\D", "");
        return MOBILE_PATTERN.matcher(cleaned).matches();
    }
    
    // ==================== Code Validation ====================
    
    /**
     * Validates code format (uppercase letters, numbers, hyphens)
     * 
     * @param code Code to validate
     * @return true if valid code format
     */
    public static boolean isValidCode(String code) {
        return code != null && CODE_PATTERN.matcher(code).matches();
    }
    
    // ==================== Username Validation ====================
    
    /**
     * Validates username format
     * 
     * @param username Username to validate
     * @return true if valid username format
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    // ==================== Number Validation ====================
    
    /**
     * Validates if value is a number
     * 
     * @param value String value
     * @return true if numeric
     */
    public static boolean isNumeric(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates if value is an integer
     * 
     * @param value String value
     * @return true if integer
     */
    public static boolean isInteger(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates decimal format
     * 
     * @param value Decimal value
     * @return true if valid decimal
     */
    public static boolean isValidDecimal(String value) {
        return value != null && DECIMAL_PATTERN.matcher(value).matches();
    }
    
    /**
     * Validates if number is positive
     * 
     * @param value Number to validate
     * @return true if positive
     */
    public static boolean isPositive(Number value) {
        return value != null && value.doubleValue() > 0;
    }
    
    /**
     * Validates if number is non-negative
     * 
     * @param value Number to validate
     * @return true if non-negative
     */
    public static boolean isNonNegative(Number value) {
        return value != null && value.doubleValue() >= 0;
    }
    
    /**
     * Validates if number is within range
     * 
     * @param value Number to validate
     * @param min Minimum value
     * @param max Maximum value
     * @return true if within range
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null || min == null || max == null) return false;
        double val = value.doubleValue();
        return val >= min.doubleValue() && val <= max.doubleValue();
    }
    
    // ==================== Percentage Validation ====================
    
    /**
     * Validates percentage (0-100)
     * 
     * @param value Percentage value
     * @return true if valid percentage
     */
    public static boolean isValidPercentage(Number value) {
        return isInRange(value, 0, 100);
    }
    
    // ==================== Price Validation ====================
    
    /**
     * Validates price (must be positive)
     * 
     * @param price Price to validate
     * @return true if valid price
     */
    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Validates quantity (must be positive)
     * 
     * @param quantity Quantity to validate
     * @return true if valid quantity
     */
    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity > 0;
    }
    
    // ==================== Date Validation ====================
    
    /**
     * Validates if date is not null
     * 
     * @param date Date to validate
     * @return true if not null
     */
    public static boolean isValidDate(LocalDate date) {
        return date != null;
    }
    
    /**
     * Validates if date is in the past
     * 
     * @param date Date to validate
     * @return true if past date
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }
    
    /**
     * Validates if date is in the future
     * 
     * @param date Date to validate
     * @return true if future date
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * Validates if date is today or in the future
     * 
     * @param date Date to validate
     * @return true if today or future
     */
    public static boolean isTodayOrFuture(LocalDate date) {
        return date != null && !date.isBefore(LocalDate.now());
    }
    
    /**
     * Validates if end date is after start date
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return true if valid date range
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return false;
        return !endDate.isBefore(startDate);
    }
    
    /**
     * Validates if date is within range
     * 
     * @param date Date to validate
     * @param startDate Start of range
     * @param endDate End of range
     * @return true if within range
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) return false;
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    // ==================== Collection Validation ====================
    
    /**
     * Validates if collection is not null and not empty
     * 
     * @param collection Collection to validate
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
    
    /**
     * Validates if map is not null and not empty
     * 
     * @param map Map to validate
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }
    
    /**
     * Validates if array is not null and not empty
     * 
     * @param array Array to validate
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * Validates collection size
     * 
     * @param collection Collection to validate
     * @param minSize Minimum size
     * @param maxSize Maximum size
     * @return true if size within range
     */
    public static boolean isValidSize(Collection<?> collection, int minSize, int maxSize) {
        if (collection == null) return false;
        int size = collection.size();
        return size >= minSize && size <= maxSize;
    }
    
    /**
     * Validates minimum collection size
     * 
     * @param collection Collection to validate
     * @param minSize Minimum size
     * @return true if meets minimum
     */
    public static boolean hasMinSize(Collection<?> collection, int minSize) {
        return collection != null && collection.size() >= minSize;
    }
    
    /**
     * Validates maximum collection size
     * 
     * @param collection Collection to validate
     * @param maxSize Maximum size
     * @return true if within maximum
     */
    public static boolean hasMaxSize(Collection<?> collection, int maxSize) {
        return collection == null || collection.size() <= maxSize;
    }
    
    // ==================== Object Validation ====================
    
    /**
     * Validates if object is not null
     * 
     * @param object Object to validate
     * @return true if not null
     */
    public static boolean isNotNull(Object object) {
        return object != null;
    }
    
    /**
     * Validates if all objects are not null
     * 
     * @param objects Objects to validate
     * @return true if all not null
     */
    public static boolean allNotNull(Object... objects) {
        if (objects == null || objects.length == 0) return false;
        for (Object obj : objects) {
            if (obj == null) return false;
        }
        return true;
    }
    
    /**
     * Validates if any object is not null
     * 
     * @param objects Objects to validate
     * @return true if any not null
     */
    public static boolean anyNotNull(Object... objects) {
        if (objects == null || objects.length == 0) return false;
        for (Object obj : objects) {
            if (obj != null) return true;
        }
        return false;
    }
    
    // ==================== Boolean Validation ====================
    
    /**
     * Validates if boolean is true
     * 
     * @param value Boolean to validate
     * @return true if true
     */
    public static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }
    
    /**
     * Validates if boolean is false
     * 
     * @param value Boolean to validate
     * @return true if false
     */
    public static boolean isFalse(Boolean value) {
        return Boolean.FALSE.equals(value);
    }
    
    // ==================== ID Validation ====================
    
    /**
     * Validates if ID is valid (not null and positive)
     * 
     * @param id ID to validate
     * @return true if valid ID
     */
    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
    
    /**
     * Validates if all IDs are valid
     * 
     * @param ids IDs to validate
     * @return true if all valid
     */
    public static boolean allValidIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return ids.stream().allMatch(ValidationUtil::isValidId);
    }
    
    // ==================== Password Validation ====================
    
    /**
     * Validates password strength
     * - At least 8 characters
     * - Contains uppercase letter
     * - Contains lowercase letter
     * - Contains digit
     * - Contains special character
     * 
     * @param password Password to validate
     * @return true if strong password
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    /**
     * Validates if password meets minimum requirements
     * - At least minimum length
     * - Contains letter
     * - Contains digit
     * 
     * @param password Password to validate
     * @param minLength Minimum length
     * @return true if meets minimum requirements
     */
    public static boolean isValidPassword(String password, int minLength) {
        if (password == null || password.length() < minLength) return false;
        
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasLetter && hasDigit;
    }
    
    // ==================== Credit Card Validation ====================
    
    /**
     * Validates credit card number using Luhn algorithm
     * 
     * @param cardNumber Card number
     * @return true if valid
     */
    public static boolean isValidCreditCard(String cardNumber) {
        if (cardNumber == null) return false;
        
        String cleaned = cardNumber.replaceAll("\\D", "");
        if (cleaned.length() < 13 || cleaned.length() > 19) return false;
        
        return luhnCheck(cleaned);
    }
    
    /**
     * Luhn algorithm for credit card validation
     * 
     * @param cardNumber Card number
     * @return true if valid
     */
    private static boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
    
    // ==================== URL Validation ====================
    
    /**
     * Validates URL format
     * 
     * @param url URL to validate
     * @return true if valid URL
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) return false;
        try {
            URI.create(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== Business Validation ====================
    
    /**
     * Validates stock quantity
     * 
     * @param quantity Quantity
     * @return true if valid (non-negative)
     */
    public static boolean isValidStock(Integer quantity) {
        return quantity != null && quantity >= 0;
    }
    
    /**
     * Validates discount percentage
     * 
     * @param discount Discount value
     * @return true if valid (0-100)
     */
    public static boolean isValidDiscount(BigDecimal discount) {
        if (discount == null) return false;
        return discount.compareTo(BigDecimal.ZERO) >= 0 && 
               discount.compareTo(new BigDecimal("100")) <= 0;
    }
    
    /**
     * Validates credit limit
     * 
     * @param creditLimit Credit limit
     * @return true if valid (positive)
     */
    public static boolean isValidCreditLimit(BigDecimal creditLimit) {
        return creditLimit != null && creditLimit.compareTo(BigDecimal.ZERO) > 0;
    }
}
