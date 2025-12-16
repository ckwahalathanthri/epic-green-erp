package lk.epicgreen.erp.common.util;

import lk.epicgreen.erp.common.constants.AppConstants;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Utility class for date and time operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class DateTimeUtil {
    
    private static final ZoneId DEFAULT_ZONE = ZoneId.of(AppConstants.DEFAULT_TIMEZONE);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DATETIME_FORMAT);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.TIMESTAMP_FORMAT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DISPLAY_DATE_FORMAT);
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern(AppConstants.FILE_DATE_FORMAT);
    
    // Prevent instantiation
    private DateTimeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== Current Date/Time ====================
    
    /**
     * Gets current date in default timezone
     * 
     * @return Current LocalDate
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now(DEFAULT_ZONE);
    }
    
    /**
     * Gets current date time in default timezone
     * 
     * @return Current LocalDateTime
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }
    
    /**
     * Gets current timestamp
     * 
     * @return Current timestamp as long
     */
    public static long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }
    
    /**
     * Gets current date as Date object
     * 
     * @return Current Date
     */
    public static Date getCurrentDateAsDate() {
        return Date.from(Instant.now());
    }
    
    // ==================== Formatting ====================
    
    /**
     * Formats LocalDate to string (yyyy-MM-dd)
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    /**
     * Formats LocalDateTime to string (yyyy-MM-dd HH:mm:ss)
     * 
     * @param dateTime DateTime to format
     * @return Formatted datetime string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    /**
     * Formats LocalDateTime to timestamp string (yyyy-MM-dd'T'HH:mm:ss.SSS'Z')
     * 
     * @param dateTime DateTime to format
     * @return Formatted timestamp string
     */
    public static String formatTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(TIMESTAMP_FORMATTER) : null;
    }
    
    /**
     * Formats LocalDate to display format (dd MMM yyyy)
     * 
     * @param date Date to format
     * @return Formatted date string for display
     */
    public static String formatDateForDisplay(LocalDate date) {
        return date != null ? date.format(DISPLAY_DATE_FORMATTER) : null;
    }
    
    /**
     * Formats LocalDate for file names (yyyyMMdd)
     * 
     * @param date Date to format
     * @return Formatted date string for files
     */
    public static String formatDateForFile(LocalDate date) {
        return date != null ? date.format(FILE_DATE_FORMATTER) : null;
    }
    
    /**
     * Formats Date to string
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) return null;
        LocalDate localDate = date.toInstant().atZone(DEFAULT_ZONE).toLocalDate();
        return formatDate(localDate);
    }
    
    // ==================== Parsing ====================
    
    /**
     * Parses date string to LocalDate (yyyy-MM-dd)
     * 
     * @param dateString Date string to parse
     * @return Parsed LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString, DATE_FORMATTER) : null;
    }
    
    /**
     * Parses datetime string to LocalDateTime (yyyy-MM-dd HH:mm:ss)
     * 
     * @param dateTimeString DateTime string to parse
     * @return Parsed LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER) : null;
    }
    
    // ==================== Conversion ====================
    
    /**
     * Converts Date to LocalDate
     * 
     * @param date Date to convert
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return date != null ? date.toInstant().atZone(DEFAULT_ZONE).toLocalDate() : null;
    }
    
    /**
     * Converts Date to LocalDateTime
     * 
     * @param date Date to convert
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date != null ? date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime() : null;
    }
    
    /**
     * Converts LocalDate to Date
     * 
     * @param localDate LocalDate to convert
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        return localDate != null ? Date.from(localDate.atStartOfDay(DEFAULT_ZONE).toInstant()) : null;
    }
    
    /**
     * Converts LocalDateTime to Date
     * 
     * @param localDateTime LocalDateTime to convert
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return localDateTime != null ? Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant()) : null;
    }
    
    /**
     * Converts timestamp to LocalDateTime
     * 
     * @param timestamp Timestamp in milliseconds
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE);
    }
    
    // ==================== Date Manipulation ====================
    
    /**
     * Adds days to a date
     * 
     * @param date Base date
     * @param days Number of days to add
     * @return New date with days added
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }
    
    /**
     * Adds months to a date
     * 
     * @param date Base date
     * @param months Number of months to add
     * @return New date with months added
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        return date != null ? date.plusMonths(months) : null;
    }
    
    /**
     * Adds years to a date
     * 
     * @param date Base date
     * @param years Number of years to add
     * @return New date with years added
     */
    public static LocalDate addYears(LocalDate date, long years) {
        return date != null ? date.plusYears(years) : null;
    }
    
    /**
     * Subtracts days from a date
     * 
     * @param date Base date
     * @param days Number of days to subtract
     * @return New date with days subtracted
     */
    public static LocalDate subtractDays(LocalDate date, long days) {
        return date != null ? date.minusDays(days) : null;
    }
    
    // ==================== Date Comparison ====================
    
    /**
     * Checks if first date is after second date
     * 
     * @param date1 First date
     * @param date2 Second date
     * @return true if date1 is after date2
     */
    public static boolean isAfter(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isAfter(date2);
    }
    
    /**
     * Checks if first date is before second date
     * 
     * @param date1 First date
     * @param date2 Second date
     * @return true if date1 is before date2
     */
    public static boolean isBefore(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isBefore(date2);
    }
    
    /**
     * Checks if two dates are equal
     * 
     * @param date1 First date
     * @param date2 Second date
     * @return true if dates are equal
     */
    public static boolean isEqual(LocalDate date1, LocalDate date2) {
        return date1 != null && date2 != null && date1.isEqual(date2);
    }
    
    /**
     * Checks if date is today
     * 
     * @param date Date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(getCurrentDate());
    }
    
    /**
     * Checks if date is in the past
     * 
     * @param date Date to check
     * @return true if date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(getCurrentDate());
    }
    
    /**
     * Checks if date is in the future
     * 
     * @param date Date to check
     * @return true if date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(getCurrentDate());
    }
    
    // ==================== Date Calculations ====================
    
    /**
     * Calculates number of days between two dates
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of days between dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? 
            ChronoUnit.DAYS.between(startDate, endDate) : 0;
    }
    
    /**
     * Calculates number of months between two dates
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of months between dates
     */
    public static long monthsBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? 
            ChronoUnit.MONTHS.between(startDate, endDate) : 0;
    }
    
    /**
     * Calculates number of years between two dates
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return Number of years between dates
     */
    public static long yearsBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? 
            ChronoUnit.YEARS.between(startDate, endDate) : 0;
    }
    
    /**
     * Calculates age from birth date
     * 
     * @param birthDate Birth date
     * @return Age in years
     */
    public static int calculateAge(LocalDate birthDate) {
        return birthDate != null ? (int) yearsBetween(birthDate, getCurrentDate()) : 0;
    }
    
    // ==================== Start/End of Period ====================
    
    /**
     * Gets start of day
     * 
     * @param date Date
     * @return LocalDateTime at start of day (00:00:00)
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }
    
    /**
     * Gets end of day
     * 
     * @param date Date
     * @return LocalDateTime at end of day (23:59:59)
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        return date != null ? date.atTime(23, 59, 59) : null;
    }
    
    /**
     * Gets first day of month
     * 
     * @param date Date
     * @return First day of the month
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date != null ? date.with(TemporalAdjusters.firstDayOfMonth()) : null;
    }
    
    /**
     * Gets last day of month
     * 
     * @param date Date
     * @return Last day of the month
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date != null ? date.with(TemporalAdjusters.lastDayOfMonth()) : null;
    }
    
    /**
     * Gets first day of year
     * 
     * @param date Date
     * @return First day of the year
     */
    public static LocalDate getFirstDayOfYear(LocalDate date) {
        return date != null ? date.with(TemporalAdjusters.firstDayOfYear()) : null;
    }
    
    /**
     * Gets last day of year
     * 
     * @param date Date
     * @return Last day of the year
     */
    public static LocalDate getLastDayOfYear(LocalDate date) {
        return date != null ? date.with(TemporalAdjusters.lastDayOfYear()) : null;
    }
    
    // ==================== Business Days ====================
    
    /**
     * Checks if date is a weekend (Saturday or Sunday)
     * 
     * @param date Date to check
     * @return true if weekend
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) return false;
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
    
    /**
     * Checks if date is a weekday (Monday to Friday)
     * 
     * @param date Date to check
     * @return true if weekday
     */
    public static boolean isWeekday(LocalDate date) {
        return !isWeekend(date);
    }
    
    /**
     * Gets next business day (skips weekends)
     * 
     * @param date Start date
     * @return Next business day
     */
    public static LocalDate getNextBusinessDay(LocalDate date) {
        if (date == null) return null;
        LocalDate nextDay = date.plusDays(1);
        while (isWeekend(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }
    
    // ==================== Validation ====================
    
    /**
     * Validates if date is within range
     * 
     * @param date Date to validate
     * @param startDate Start of range
     * @param endDate End of range
     * @return true if date is within range
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) return false;
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Validates if end date is after start date
     * 
     * @param startDate Start date
     * @param endDate End date
     * @return true if valid date range
     */
    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !endDate.isBefore(startDate);
    }
}
