package lk.epicgreen.erp.common.util;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for string operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class StringUtil {
    
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s]");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    
    // Prevent instantiation
    private StringUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Null/Empty Checks ====================
    
    /**
     * Checks if string is null or empty
     * 
     * @param str String to check
     * @return true if null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * Checks if string is not null and not empty
     * 
     * @param str String to check
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Checks if string is null, empty, or whitespace only
     * 
     * @param str String to check
     * @return true if null, empty, or blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Checks if string is not null, not empty, and not blank
     * 
     * @param str String to check
     * @return true if not null, not empty, and not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    // ==================== Default Values ====================
    
    /**
     * Returns string or default value if null
     * 
     * @param str String to check
     * @param defaultValue Default value
     * @return String or default value
     */
    public static String defaultIfNull(String str, String defaultValue) {
        return str != null ? str : defaultValue;
    }
    
    /**
     * Returns string or empty string if null
     * 
     * @param str String to check
     * @return String or empty string
     */
    public static String defaultIfNull(String str) {
        return defaultIfNull(str, "");
    }
    
    /**
     * Returns string or default value if null/empty
     * 
     * @param str String to check
     * @param defaultValue Default value
     * @return String or default value
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isNotEmpty(str) ? str : defaultValue;
    }
    
    /**
     * Returns string or default value if null/blank
     * 
     * @param str String to check
     * @param defaultValue Default value
     * @return String or default value
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isNotBlank(str) ? str : defaultValue;
    }
    
    // ==================== Trimming ====================
    
    /**
     * Trims string (null-safe)
     * 
     * @param str String to trim
     * @return Trimmed string or null
     */
    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }
    
    /**
     * Trims string to null if empty after trimming
     * 
     * @param str String to trim
     * @return Trimmed string or null
     */
    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isNotEmpty(trimmed) ? trimmed : null;
    }
    
    /**
     * Trims string to empty if null
     * 
     * @param str String to trim
     * @return Trimmed string or empty string
     */
    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : "";
    }
    
    // ==================== Case Conversion ====================
    
    /**
     * Converts to uppercase (null-safe)
     * 
     * @param str String to convert
     * @return Uppercase string or null
     */
    public static String toUpperCase(String str) {
        return str != null ? str.toUpperCase() : null;
    }
    
    /**
     * Converts to lowercase (null-safe)
     * 
     * @param str String to convert
     * @return Lowercase string or null
     */
    public static String toLowerCase(String str) {
        return str != null ? str.toLowerCase() : null;
    }
    
    /**
     * Capitalizes first letter of string
     * 
     * @param str String to capitalize
     * @return Capitalized string or null
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Capitalizes first letter of each word
     * 
     * @param str String to convert
     * @return Title case string
     */
    public static String toTitleCase(String str) {
        if (isEmpty(str)) return str;
        return Arrays.stream(str.split("\\s+"))
                .map(StringUtil::capitalize)
                .collect(Collectors.joining(" "));
    }
    
    /**
     * Converts camelCase to snake_case
     * 
     * @param str Camel case string
     * @return Snake case string
     */
    public static String camelToSnake(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
    
    /**
     * Converts snake_case to camelCase
     * 
     * @param str Snake case string
     * @return Camel case string
     */
    public static String snakeToCamel(String str) {
        if (isEmpty(str)) return str;
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        
        for (char c : str.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                result.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }
        return result.toString();
    }
    
    // ==================== Substring Operations ====================
    
    /**
     * Gets substring safely (no IndexOutOfBoundsException)
     * 
     * @param str String
     * @param start Start index
     * @param end End index
     * @return Substring or null
     */
    public static String substring(String str, int start, int end) {
        if (str == null) return null;
        if (start < 0) start = 0;
        if (end > str.length()) end = str.length();
        if (start > end) return "";
        return str.substring(start, end);
    }
    
    /**
     * Gets left substring
     * 
     * @param str String
     * @param length Length
     * @return Left substring
     */
    public static String left(String str, int length) {
        if (str == null) return null;
        if (length < 0) return "";
        if (length >= str.length()) return str;
        return str.substring(0, length);
    }
    
    /**
     * Gets right substring
     * 
     * @param str String
     * @param length Length
     * @return Right substring
     */
    public static String right(String str, int length) {
        if (str == null) return null;
        if (length < 0) return "";
        if (length >= str.length()) return str;
        return str.substring(str.length() - length);
    }
    
    /**
     * Truncates string to specified length and adds ellipsis
     * 
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    // ==================== Padding ====================
    
    /**
     * Pads string on the left
     * 
     * @param str String to pad
     * @param size Target size
     * @param padChar Padding character
     * @return Padded string
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) return null;
        int padSize = size - str.length();
        if (padSize <= 0) return str;
        return repeat(String.valueOf(padChar), padSize) + str;
    }
    
    /**
     * Pads string on the right
     * 
     * @param str String to pad
     * @param size Target size
     * @param padChar Padding character
     * @return Padded string
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) return null;
        int padSize = size - str.length();
        if (padSize <= 0) return str;
        return str + repeat(String.valueOf(padChar), padSize);
    }
    
    // ==================== Repeat ====================
    
    /**
     * Repeats string n times
     * 
     * @param str String to repeat
     * @param count Repeat count
     * @return Repeated string
     */
    public static String repeat(String str, int count) {
        if (str == null || count <= 0) return "";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(str);
        }
        return result.toString();
    }
    
    // ==================== Joining/Splitting ====================
    
    /**
     * Joins array with delimiter
     * 
     * @param array Array to join
     * @param delimiter Delimiter
     * @return Joined string
     */
    public static String join(Object[] array, String delimiter) {
        if (array == null || array.length == 0) return "";
        return Arrays.stream(array)
                .map(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }
    
    /**
     * Joins collection with delimiter
     * 
     * @param collection Collection to join
     * @param delimiter Delimiter
     * @return Joined string
     */
    public static String join(Collection<?> collection, String delimiter) {
        if (collection == null || collection.isEmpty()) return "";
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(delimiter));
    }
    
    /**
     * Splits string by delimiter and trims each part
     * 
     * @param str String to split
     * @param delimiter Delimiter
     * @return List of trimmed parts
     */
    public static List<String> splitAndTrim(String str, String delimiter) {
        if (isEmpty(str)) return Collections.emptyList();
        return Arrays.stream(str.split(delimiter))
                .map(String::trim)
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.toList());
    }
    
    // ==================== Cleaning ====================
    
    /**
     * Removes all whitespace from string
     * 
     * @param str String to clean
     * @return String without whitespace
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("\\s+", "");
    }
    
    /**
     * Normalizes whitespace (multiple spaces to single space)
     * 
     * @param str String to normalize
     * @return Normalized string
     */
    public static String normalizeWhitespace(String str) {
        if (isEmpty(str)) return str;
        return WHITESPACE_PATTERN.matcher(str.trim()).replaceAll(" ");
    }
    
    /**
     * Removes special characters
     * 
     * @param str String to clean
     * @return String without special characters
     */
    public static String removeSpecialCharacters(String str) {
        if (isEmpty(str)) return str;
        return SPECIAL_CHARS_PATTERN.matcher(str).replaceAll("");
    }
    
    /**
     * Removes accents/diacritics
     * 
     * @param str String to clean
     * @return String without accents
     */
    public static String removeAccents(String str) {
        if (isEmpty(str)) return str;
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
    
    // ==================== Validation ====================
    
    /**
     * Validates email format
     * 
     * @param email Email to validate
     * @return true if valid email
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates phone number format
     * 
     * @param phone Phone to validate
     * @return true if valid phone
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        String cleaned = removeWhitespace(phone).replaceAll("[^0-9]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }
    
    /**
     * Checks if string contains only digits
     * 
     * @param str String to check
     * @return true if numeric
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) return false;
        return str.matches("\\d+");
    }
    
    /**
     * Checks if string contains only letters
     * 
     * @param str String to check
     * @return true if alphabetic
     */
    public static boolean isAlphabetic(String str) {
        if (isEmpty(str)) return false;
        return str.matches("[a-zA-Z]+");
    }
    
    /**
     * Checks if string contains only letters and digits
     * 
     * @param str String to check
     * @return true if alphanumeric
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) return false;
        return str.matches("[a-zA-Z0-9]+");
    }
    
    // ==================== Contains/Starts/Ends ====================
    
    /**
     * Checks if string contains substring (null-safe)
     * 
     * @param str String to check
     * @param substring Substring
     * @return true if contains
     */
    public static boolean contains(String str, String substring) {
        return str != null && substring != null && str.contains(substring);
    }
    
    /**
     * Checks if string contains substring (case-insensitive)
     * 
     * @param str String to check
     * @param substring Substring
     * @return true if contains
     */
    public static boolean containsIgnoreCase(String str, String substring) {
        return str != null && substring != null && 
               str.toLowerCase().contains(substring.toLowerCase());
    }
    
    /**
     * Checks if string starts with prefix (null-safe)
     * 
     * @param str String to check
     * @param prefix Prefix
     * @return true if starts with
     */
    public static boolean startsWith(String str, String prefix) {
        return str != null && prefix != null && str.startsWith(prefix);
    }
    
    /**
     * Checks if string ends with suffix (null-safe)
     * 
     * @param str String to check
     * @param suffix Suffix
     * @return true if ends with
     */
    public static boolean endsWith(String str, String suffix) {
        return str != null && suffix != null && str.endsWith(suffix);
    }
    
    // ==================== Comparison ====================
    
    /**
     * Compares two strings (null-safe)
     * 
     * @param str1 First string
     * @param str2 Second string
     * @return true if equal
     */
    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }
    
    /**
     * Compares two strings ignoring case (null-safe)
     * 
     * @param str1 First string
     * @param str2 Second string
     * @return true if equal ignoring case
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equalsIgnoreCase(str2);
    }
    
    // ==================== Masking ====================
    
    /**
     * Masks email (shows first 3 chars and domain)
     * Example: john.doe@example.com -> joh****@example.com
     * 
     * @param email Email to mask
     * @return Masked email
     */
    public static String maskEmail(String email) {
        if (isEmpty(email) || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        String masked = username.length() > 3 ? 
                        username.substring(0, 3) + "****" : "****";
        return masked + "@" + domain;
    }
    
    /**
     * Masks phone number (shows last 4 digits)
     * Example: 1234567890 -> ******7890
     * 
     * @param phone Phone to mask
     * @return Masked phone
     */
    public static String maskPhone(String phone) {
        if (isEmpty(phone) || phone.length() < 4) return "****";
        return repeat("*", phone.length() - 4) + phone.substring(phone.length() - 4);
    }
    
    /**
     * Masks credit card number
     * Example: 1234567890123456 -> ************3456
     * 
     * @param cardNumber Card number
     * @return Masked card number
     */
    public static String maskCreditCard(String cardNumber) {
        if (isEmpty(cardNumber) || cardNumber.length() < 4) return "****";
        return repeat("*", cardNumber.length() - 4) + cardNumber.substring(cardNumber.length() - 4);
    }
    
    // ==================== Generation ====================
    
    /**
     * Generates random alphanumeric string
     * 
     * @param length Length of string
     * @return Random string
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt(random.nextInt(chars.length())));
        }
        return result.toString();
    }
    
    /**
     * Generates random numeric string
     * 
     * @param length Length of string
     * @return Random numeric string
     */
    public static String generateRandomNumeric(int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
