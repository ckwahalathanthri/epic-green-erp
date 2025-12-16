package lk.epicgreen.erp.common.util;

import lk.epicgreen.erp.common.constants.AppConstants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for number operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class NumberUtil {
    
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat(AppConstants.DECIMAL_FORMAT);
    private static final DecimalFormat INTEGER_FORMATTER = new DecimalFormat(AppConstants.INTEGER_FORMAT);
    private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat(AppConstants.PERCENTAGE_FORMAT);
    private static final DecimalFormat QUANTITY_FORMATTER = new DecimalFormat(AppConstants.QUANTITY_FORMAT);
    
    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;
    
    // Prevent instantiation
    private NumberUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Formatting ====================
    
    /**
     * Formats number as decimal with 2 decimal places
     * Example: 1234.56 -> "1,234.56"
     * 
     * @param number Number to format
     * @return Formatted string
     */
    public static String formatDecimal(Number number) {
        return number != null ? DECIMAL_FORMATTER.format(number) : null;
    }
    
    /**
     * Formats number as integer with thousand separators
     * Example: 1234567 -> "1,234,567"
     * 
     * @param number Number to format
     * @return Formatted string
     */
    public static String formatInteger(Number number) {
        return number != null ? INTEGER_FORMATTER.format(number) : null;
    }
    
    /**
     * Formats number as percentage
     * Example: 0.1234 -> "12.34%"
     * 
     * @param number Number to format (0.1234 = 12.34%)
     * @return Formatted string
     */
    public static String formatPercentage(Number number) {
        return number != null ? PERCENTAGE_FORMATTER.format(number) : null;
    }
    
    /**
     * Formats quantity with 3 decimal places
     * Example: 123.456 -> "123.456"
     * 
     * @param quantity Quantity to format
     * @return Formatted string
     */
    public static String formatQuantity(Number quantity) {
        return quantity != null ? QUANTITY_FORMATTER.format(quantity) : null;
    }
    
    /**
     * Formats currency with symbol
     * Example: 1234.56 -> "Rs. 1,234.56"
     * 
     * @param amount Amount to format
     * @return Formatted currency string
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) return null;
        return AppConstants.CURRENCY_SYMBOL + " " + formatDecimal(amount);
    }
    
    /**
     * Formats number with custom pattern
     * 
     * @param number Number to format
     * @param pattern Format pattern
     * @return Formatted string
     */
    public static String format(Number number, String pattern) {
        if (number == null) return null;
        DecimalFormat formatter = new DecimalFormat(pattern);
        return formatter.format(number);
    }
    
    // ==================== Parsing ====================
    
    /**
     * Parses string to Integer
     * 
     * @param value String value
     * @return Integer or null
     */
    public static Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Parses string to Long
     * 
     * @param value String value
     * @return Long or null
     */
    public static Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Parses string to Double
     * 
     * @param value String value
     * @return Double or null
     */
    public static Double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Parses string to BigDecimal
     * 
     * @param value String value
     * @return BigDecimal or null
     */
    public static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Parses string to Integer with default value
     * 
     * @param value String value
     * @param defaultValue Default value
     * @return Integer or default
     */
    public static Integer parseInteger(String value, Integer defaultValue) {
        Integer parsed = parseInteger(value);
        return parsed != null ? parsed : defaultValue;
    }
    
    /**
     * Parses string to BigDecimal with default value
     * 
     * @param value String value
     * @param defaultValue Default value
     * @return BigDecimal or default
     */
    public static BigDecimal parseBigDecimal(String value, BigDecimal defaultValue) {
        BigDecimal parsed = parseBigDecimal(value);
        return parsed != null ? parsed : defaultValue;
    }
    
    // ==================== Conversion ====================
    
    /**
     * Converts Number to BigDecimal
     * 
     * @param number Number to convert
     * @return BigDecimal
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (number == null) return null;
        if (number instanceof BigDecimal) return (BigDecimal) number;
        return BigDecimal.valueOf(number.doubleValue());
    }
    
    /**
     * Converts Number to Integer
     * 
     * @param number Number to convert
     * @return Integer
     */
    public static Integer toInteger(Number number) {
        return number != null ? number.intValue() : null;
    }
    
    /**
     * Converts Number to Long
     * 
     * @param number Number to convert
     * @return Long
     */
    public static Long toLong(Number number) {
        return number != null ? number.longValue() : null;
    }
    
    /**
     * Converts Number to Double
     * 
     * @param number Number to convert
     * @return Double
     */
    public static Double toDouble(Number number) {
        return number != null ? number.doubleValue() : null;
    }
    
    // ==================== Rounding ====================
    
    /**
     * Rounds BigDecimal to specified scale
     * 
     * @param value Value to round
     * @param scale Number of decimal places
     * @return Rounded value
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        return value != null ? value.setScale(scale, DEFAULT_ROUNDING) : null;
    }
    
    /**
     * Rounds BigDecimal to default scale (2 decimal places)
     * 
     * @param value Value to round
     * @return Rounded value
     */
    public static BigDecimal round(BigDecimal value) {
        return round(value, DEFAULT_SCALE);
    }
    
    /**
     * Rounds Double to specified decimal places
     * 
     * @param value Value to round
     * @param scale Number of decimal places
     * @return Rounded value
     */
    public static Double round(Double value, int scale) {
        if (value == null) return null;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, DEFAULT_ROUNDING);
        return bd.doubleValue();
    }
    
    /**
     * Rounds up to nearest integer
     * 
     * @param value Value to round up
     * @return Rounded up value
     */
    public static Integer roundUp(Double value) {
        return value != null ? (int) Math.ceil(value) : null;
    }
    
    /**
     * Rounds down to nearest integer
     * 
     * @param value Value to round down
     * @return Rounded down value
     */
    public static Integer roundDown(Double value) {
        return value != null ? (int) Math.floor(value) : null;
    }
    
    // ==================== Comparison ====================
    
    /**
     * Compares two BigDecimal values
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return 0 if equal, negative if value1 < value2, positive if value1 > value2
     */
    public static int compare(BigDecimal value1, BigDecimal value2) {
        if (value1 == null && value2 == null) return 0;
        if (value1 == null) return -1;
        if (value2 == null) return 1;
        return value1.compareTo(value2);
    }
    
    /**
     * Checks if values are equal
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return true if equal
     */
    public static boolean isEqual(BigDecimal value1, BigDecimal value2) {
        return compare(value1, value2) == 0;
    }
    
    /**
     * Checks if value1 is greater than value2
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return true if value1 > value2
     */
    public static boolean isGreaterThan(BigDecimal value1, BigDecimal value2) {
        return compare(value1, value2) > 0;
    }
    
    /**
     * Checks if value1 is less than value2
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return true if value1 < value2
     */
    public static boolean isLessThan(BigDecimal value1, BigDecimal value2) {
        return compare(value1, value2) < 0;
    }
    
    /**
     * Checks if value is zero
     * 
     * @param value Value to check
     * @return true if zero
     */
    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Checks if value is positive
     * 
     * @param value Value to check
     * @return true if positive (> 0)
     */
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if value is negative
     * 
     * @param value Value to check
     * @return true if negative (< 0)
     */
    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }
    
    // ==================== Arithmetic Operations ====================
    
    /**
     * Adds two BigDecimal values
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return Sum
     */
    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        if (value1 == null && value2 == null) return BigDecimal.ZERO;
        if (value1 == null) return value2;
        if (value2 == null) return value1;
        return value1.add(value2);
    }
    
    /**
     * Subtracts value2 from value1
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return Difference
     */
    public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) value1 = BigDecimal.ZERO;
        if (value2 == null) value2 = BigDecimal.ZERO;
        return value1.subtract(value2);
    }
    
    /**
     * Multiplies two BigDecimal values
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return Product
     */
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) return BigDecimal.ZERO;
        return value1.multiply(value2);
    }
    
    /**
     * Divides value1 by value2
     * 
     * @param value1 Dividend
     * @param value2 Divisor
     * @param scale Decimal places
     * @return Quotient
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2, int scale) {
        if (value1 == null || value2 == null || isZero(value2)) return BigDecimal.ZERO;
        return value1.divide(value2, scale, DEFAULT_ROUNDING);
    }
    
    /**
     * Divides value1 by value2 with default scale (2)
     * 
     * @param value1 Dividend
     * @param value2 Divisor
     * @return Quotient
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        return divide(value1, value2, DEFAULT_SCALE);
    }
    
    // ==================== Percentage Calculations ====================
    
    /**
     * Calculates percentage of a value
     * Example: percentage(100, 20) = 20 (20% of 100)
     * 
     * @param value Base value
     * @param percentage Percentage (0-100)
     * @return Percentage amount
     */
    public static BigDecimal percentage(BigDecimal value, BigDecimal percentage) {
        if (value == null || percentage == null) return BigDecimal.ZERO;
        return multiply(value, divide(percentage, BigDecimal.valueOf(100)));
    }
    
    /**
     * Calculates what percentage value1 is of value2
     * Example: percentageOf(25, 100) = 25 (25 is 25% of 100)
     * 
     * @param value1 Part value
     * @param value2 Total value
     * @return Percentage
     */
    public static BigDecimal percentageOf(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null || isZero(value2)) return BigDecimal.ZERO;
        return multiply(divide(value1, value2), BigDecimal.valueOf(100));
    }
    
    /**
     * Adds percentage to value
     * Example: addPercentage(100, 10) = 110 (100 + 10%)
     * 
     * @param value Base value
     * @param percentage Percentage to add
     * @return Value with percentage added
     */
    public static BigDecimal addPercentage(BigDecimal value, BigDecimal percentage) {
        if (value == null) return BigDecimal.ZERO;
        return add(value, percentage(value, percentage));
    }
    
    /**
     * Subtracts percentage from value
     * Example: subtractPercentage(100, 10) = 90 (100 - 10%)
     * 
     * @param value Base value
     * @param percentage Percentage to subtract
     * @return Value with percentage subtracted
     */
    public static BigDecimal subtractPercentage(BigDecimal value, BigDecimal percentage) {
        if (value == null) return BigDecimal.ZERO;
        return subtract(value, percentage(value, percentage));
    }
    
    // ==================== Min/Max ====================
    
    /**
     * Returns minimum of two values
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return Minimum value
     */
    public static BigDecimal min(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) return value2;
        if (value2 == null) return value1;
        return value1.min(value2);
    }
    
    /**
     * Returns maximum of two values
     * 
     * @param value1 First value
     * @param value2 Second value
     * @return Maximum value
     */
    public static BigDecimal max(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) return value2;
        if (value2 == null) return value1;
        return value1.max(value2);
    }
    
    // ==================== Null Safety ====================
    
    /**
     * Returns value or zero if null
     * 
     * @param value Value to check
     * @return Value or zero
     */
    public static BigDecimal defaultIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
    
    /**
     * Returns value or default if null
     * 
     * @param value Value to check
     * @param defaultValue Default value
     * @return Value or default
     */
    public static BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value != null ? value : defaultValue;
    }
    
    /**
     * Returns value or zero if null
     * 
     * @param value Value to check
     * @return Value or zero
     */
    public static Integer defaultIfNull(Integer value) {
        return value != null ? value : 0;
    }
    
    // ==================== Business Calculations ====================
    
    /**
     * Calculates line total (quantity * price)
     * 
     * @param quantity Quantity
     * @param price Unit price
     * @return Line total
     */
    public static BigDecimal calculateLineTotal(Integer quantity, BigDecimal price) {
        if (quantity == null || price == null) return BigDecimal.ZERO;
        return multiply(BigDecimal.valueOf(quantity), price);
    }
    
    /**
     * Calculates discount amount
     * 
     * @param amount Original amount
     * @param discountPercentage Discount percentage
     * @return Discount amount
     */
    public static BigDecimal calculateDiscount(BigDecimal amount, BigDecimal discountPercentage) {
        return percentage(amount, discountPercentage);
    }
    
    /**
     * Calculates amount after discount
     * 
     * @param amount Original amount
     * @param discountPercentage Discount percentage
     * @return Amount after discount
     */
    public static BigDecimal calculateAmountAfterDiscount(BigDecimal amount, BigDecimal discountPercentage) {
        return subtractPercentage(amount, discountPercentage);
    }
    
    /**
     * Calculates tax amount
     * 
     * @param amount Taxable amount
     * @param taxRate Tax rate percentage
     * @return Tax amount
     */
    public static BigDecimal calculateTax(BigDecimal amount, BigDecimal taxRate) {
        return percentage(amount, taxRate);
    }
    
    /**
     * Calculates total with tax
     * 
     * @param amount Base amount
     * @param taxRate Tax rate percentage
     * @return Total with tax
     */
    public static BigDecimal calculateTotalWithTax(BigDecimal amount, BigDecimal taxRate) {
        return addPercentage(amount, taxRate);
    }
}
