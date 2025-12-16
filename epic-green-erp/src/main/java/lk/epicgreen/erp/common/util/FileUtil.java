package lk.epicgreen.erp.common.util;

import lk.epicgreen.erp.common.constants.AppConstants;
import lk.epicgreen.erp.common.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Utility class for file operations
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
public final class FileUtil {
    
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList(AppConstants.ALLOWED_IMAGE_EXTENSIONS);
    private static final List<String> ALLOWED_DOCUMENT_EXTENSIONS = Arrays.asList(AppConstants.ALLOWED_DOCUMENT_EXTENSIONS);
    private static final List<String> ALLOWED_IMPORT_EXTENSIONS = Arrays.asList(AppConstants.ALLOWED_IMPORT_EXTENSIONS);
    
    // Prevent instantiation
    private FileUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ==================== File Name Operations ====================
    
    /**
     * Gets file extension
     * 
     * @param fileName File name
     * @return Extension (e.g., ".jpg")
     */
    public static String getFileExtension(String fileName) {
        if (StringUtil.isEmpty(fileName)) return "";
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex > 0 ? fileName.substring(lastIndex) : "";
    }
    
    /**
     * Gets file name without extension
     * 
     * @param fileName File name
     * @return Name without extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (StringUtil.isEmpty(fileName)) return "";
        int lastIndex = fileName.lastIndexOf('.');
        return lastIndex > 0 ? fileName.substring(0, lastIndex) : fileName;
    }
    
    /**
     * Generates unique file name
     * 
     * @param originalFileName Original file name
     * @return Unique file name
     */
    public static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(System.currentTimeMillis());
        return uuid + "_" + timestamp + extension;
    }
    
    /**
     * Sanitizes file name (removes special characters)
     * 
     * @param fileName File name
     * @return Sanitized file name
     */
    public static String sanitizeFileName(String fileName) {
        if (StringUtil.isEmpty(fileName)) return "";
        String extension = getFileExtension(fileName);
        String nameWithoutExt = getFileNameWithoutExtension(fileName);
        String sanitized = nameWithoutExt.replaceAll("[^a-zA-Z0-9.-]", "_");
        return sanitized + extension;
    }
    
    // ==================== File Validation ====================
    
    /**
     * Validates if file is empty
     * 
     * @param file Multipart file
     * @return true if not empty
     */
    public static boolean isNotEmpty(MultipartFile file) {
        return file != null && !file.isEmpty();
    }
    
    /**
     * Validates file size
     * 
     * @param file Multipart file
     * @param maxSizeInBytes Maximum size in bytes
     * @return true if within limit
     */
    public static boolean isValidSize(MultipartFile file, long maxSizeInBytes) {
        return file != null && file.getSize() <= maxSizeInBytes;
    }
    
    /**
     * Validates if file is an image
     * 
     * @param file Multipart file
     * @return true if image
     */
    public static boolean isImage(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) return false;
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
    }
    
    /**
     * Validates if file is a document
     * 
     * @param file Multipart file
     * @return true if document
     */
    public static boolean isDocument(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) return false;
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        return ALLOWED_DOCUMENT_EXTENSIONS.contains(extension);
    }
    
    /**
     * Validates if file extension is allowed for import
     * 
     * @param file Multipart file
     * @return true if valid import file
     */
    public static boolean isValidImportFile(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) return false;
        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        return ALLOWED_IMPORT_EXTENSIONS.contains(extension);
    }
    
    /**
     * Validates file extension against allowed list
     * 
     * @param fileName File name
     * @param allowedExtensions List of allowed extensions
     * @return true if extension allowed
     */
    public static boolean hasAllowedExtension(String fileName, List<String> allowedExtensions) {
        if (StringUtil.isEmpty(fileName) || allowedExtensions == null) return false;
        String extension = getFileExtension(fileName).toLowerCase();
        return allowedExtensions.contains(extension);
    }
    
    // ==================== File Upload ====================
    
    /**
     * Saves uploaded file to specified directory
     * 
     * @param file Multipart file
     * @param uploadDir Upload directory path
     * @return Saved file path
     * @throws FileStorageException if save fails
     */
    public static String saveFile(MultipartFile file, String uploadDir) throws FileStorageException {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }
        
        try {
            // Create directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique file name
            String originalFileName = file.getOriginalFilename();
            String uniqueFileName = generateUniqueFileName(originalFileName);
            
            // Save file
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return uniqueFileName;
            
        } catch (IOException e) {
            throw new FileStorageException("Failed to save file: " + file.getOriginalFilename(), e);
        }
    }
    
    /**
     * Saves file with original name
     * 
     * @param file Multipart file
     * @param uploadDir Upload directory
     * @param fileName Desired file name
     * @return Saved file path
     * @throws FileStorageException if save fails
     */
    public static String saveFileWithName(MultipartFile file, String uploadDir, String fileName) 
            throws FileStorageException {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }
        
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
            
        } catch (IOException e) {
            throw new FileStorageException("Failed to save file: " + fileName, e);
        }
    }
    
    // ==================== File Download ====================
    
    /**
     * Loads file as resource
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return Path to file
     * @throws FileStorageException if file not found
     */
    public static Path loadFile(String fileName, String uploadDir) throws FileStorageException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                return filePath;
            } else {
                throw new FileStorageException("File not found: " + fileName);
            }
        } catch (Exception e) {
            throw new FileStorageException("Failed to load file: " + fileName, e);
        }
    }
    
    /**
     * Reads file content as bytes
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return File content as bytes
     * @throws FileStorageException if read fails
     */
    public static byte[] readFileAsBytes(String fileName, String uploadDir) throws FileStorageException {
        try {
            Path filePath = loadFile(fileName, uploadDir);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new FileStorageException("Failed to read file: " + fileName, e);
        }
    }
    
    /**
     * Reads file content as string
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return File content as string
     * @throws FileStorageException if read fails
     */
    public static String readFileAsString(String fileName, String uploadDir) throws FileStorageException {
        try {
            Path filePath = loadFile(fileName, uploadDir);
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new FileStorageException("Failed to read file: " + fileName, e);
        }
    }
    
    // ==================== File Deletion ====================
    
    /**
     * Deletes file
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return true if deleted successfully
     */
    public static boolean deleteFile(String fileName, String uploadDir) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Deletes multiple files
     * 
     * @param fileNames List of file names
     * @param uploadDir Upload directory
     * @return Number of files deleted
     */
    public static int deleteFiles(List<String> fileNames, String uploadDir) {
        if (fileNames == null || fileNames.isEmpty()) return 0;
        int deletedCount = 0;
        for (String fileName : fileNames) {
            if (deleteFile(fileName, uploadDir)) {
                deletedCount++;
            }
        }
        return deletedCount;
    }
    
    // ==================== Directory Operations ====================
    
    /**
     * Creates directory if not exists
     * 
     * @param directoryPath Directory path
     * @return true if created or already exists
     */
    public static boolean createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Lists files in directory
     * 
     * @param directoryPath Directory path
     * @return List of file names
     */
    public static List<String> listFiles(String directoryPath) {
        try {
            return Files.list(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
    
    /**
     * Deletes directory and all contents
     * 
     * @param directoryPath Directory path
     * @return true if deleted
     */
    public static boolean deleteDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted((p1, p2) -> p2.compareTo(p1)) // Delete files before directories
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                // Ignore
                            }
                        });
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    // ==================== File Info ====================
    
    /**
     * Gets file size
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return File size in bytes
     */
    public static long getFileSize(String fileName, String uploadDir) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            return Files.size(filePath);
        } catch (IOException e) {
            return 0;
        }
    }
    
    /**
     * Gets human-readable file size
     * 
     * @param bytes Size in bytes
     * @return Human-readable size (e.g., "1.5 MB")
     */
    public static String getHumanReadableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Checks if file exists
     * 
     * @param fileName File name
     * @param uploadDir Upload directory
     * @return true if exists
     */
    public static boolean fileExists(String fileName, String uploadDir) {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        return Files.exists(filePath);
    }
    
    // ==================== Content Type ====================
    
    /**
     * Gets content type from file extension
     * 
     * @param fileName File name
     * @return Content type
     */
    public static String getContentType(String fileName) {
        if (StringUtil.isEmpty(fileName)) return "application/octet-stream";
        
        String extension = getFileExtension(fileName).toLowerCase();
        
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".pdf":
                return "application/pdf";
            case ".doc":
                return "application/msword";
            case ".docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".xls":
                return "application/vnd.ms-excel";
            case ".xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case ".csv":
                return "text/csv";
            case ".txt":
                return "text/plain";
            case ".html":
                return "text/html";
            case ".json":
                return "application/json";
            case ".xml":
                return "application/xml";
            case ".zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }
    
    // ==================== File Copy/Move ====================
    
    /**
     * Copies file
     * 
     * @param sourceFileName Source file name
     * @param sourceDir Source directory
     * @param targetFileName Target file name
     * @param targetDir Target directory
     * @return true if copied successfully
     */
    public static boolean copyFile(String sourceFileName, String sourceDir, 
                                   String targetFileName, String targetDir) {
        try {
            Path sourcePath = Paths.get(sourceDir).resolve(sourceFileName);
            Path targetPath = Paths.get(targetDir).resolve(targetFileName);
            
            createDirectoryIfNotExists(targetDir);
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Moves file
     * 
     * @param sourceFileName Source file name
     * @param sourceDir Source directory
     * @param targetFileName Target file name
     * @param targetDir Target directory
     * @return true if moved successfully
     */
    public static boolean moveFile(String sourceFileName, String sourceDir,
                                   String targetFileName, String targetDir) {
        try {
            Path sourcePath = Paths.get(sourceDir).resolve(sourceFileName);
            Path targetPath = Paths.get(targetDir).resolve(targetFileName);
            
            createDirectoryIfNotExists(targetDir);
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
