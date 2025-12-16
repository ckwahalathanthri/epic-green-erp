# Common DTO Files - Quick Index

## 📦 Complete Package (9 Java Files + 1 README)

### ✅ Core Response DTOs (3 files)

1. **ApiResponse.java** (~200 lines)
   - Generic API response wrapper for all endpoints
   - Success/error indication
   - Factory methods: success(), error()
   - **Use in:** Every controller endpoint

2. **PageResponse.java** (~180 lines)
   - Pagination wrapper for list endpoints
   - Spring Data Page integration
   - Helper methods for navigation
   - **Use in:** All list/search endpoints

3. **ErrorResponse.java** (~230 lines)
   - Standardized error response structure
   - Field validation errors
   - Sub-error support
   - **Use in:** GlobalExceptionHandler

---

### ✅ Base & Search DTOs (3 files)

4. **BaseDTO.java** (~120 lines)
   - Base class for all entity DTOs
   - Audit fields (created/updated)
   - Soft delete support
   - Helper methods
   - **Use in:** All entity DTOs (extend this)

5. **SearchRequest.java** (~200 lines)
   - Advanced search with filters
   - Pagination & sorting
   - Multiple filter operations
   - **Use in:** Search endpoints

6. **IdNameDTO.java** (~100 lines)
   - Simple DTO for dropdowns
   - Minimal fields (id, name, code)
   - Display text generation
   - **Use in:** Dropdown lists, autocomplete

---

### ✅ Utility DTOs (3 files)

7. **KeyValueDTO.java** (~120 lines)
   - Generic key-value pair
   - Configuration settings
   - Metadata support
   - **Use in:** Settings, parameters

8. **ImportResultDTO.java** (~200 lines)
   - File import operation results
   - Success/failure tracking
   - Error details per row
   - **Use in:** Excel/CSV import endpoints

9. **BulkOperationResultDTO.java** (~200 lines)
   - Bulk operation results
   - Success/failure counts
   - Failed item details
   - **Use in:** Bulk delete/update endpoints

---

## 📊 File Statistics

| File | Lines | Primary Purpose | Usage Frequency |
|------|-------|-----------------|-----------------|
| ApiResponse.java | 200 | Response wrapper | 100% (all endpoints) |
| PageResponse.java | 180 | Pagination | 60% (list endpoints) |
| ErrorResponse.java | 230 | Error handling | 100% (exceptions) |
| BaseDTO.java | 120 | Base class | 90% (all DTOs) |
| SearchRequest.java | 200 | Advanced search | 30% (search) |
| IdNameDTO.java | 100 | Dropdowns | 80% (dropdowns) |
| KeyValueDTO.java | 120 | Key-value pairs | 20% (settings) |
| ImportResultDTO.java | 200 | Import tracking | 10% (imports) |
| BulkOperationResultDTO.java | 200 | Bulk operations | 15% (bulk ops) |
| **Total** | **~1,550 lines** | **9 DTOs** | - |

---

## 🎯 Usage Matrix

### Every Endpoint Should Use:
✅ ApiResponse - Wrap all responses

### List Endpoints Should Use:
✅ ApiResponse + PageResponse

### Entity DTOs Should Extend:
✅ BaseDTO (for audit trail)

### Search Endpoints Should Use:
✅ ApiResponse + SearchRequest + PageResponse

### Dropdown Endpoints Should Use:
✅ ApiResponse + List<IdNameDTO>

### Import Endpoints Should Use:
✅ ApiResponse + ImportResultDTO

### Bulk Operations Should Use:
✅ ApiResponse + BulkOperationResultDTO

---

## 🚀 Quick Examples

### 1. Simple CRUD Endpoint
```java
@PostMapping
public ResponseEntity<ApiResponse<ProductDTO>> create(@Valid @RequestBody CreateProductRequest request) {
    ProductDTO product = service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(product, "Created successfully"));
}
```

### 2. List Endpoint with Pagination
```java
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getAll(Pageable pageable) {
    Page<Product> page = service.findAll(pageable);
    PageResponse<ProductDTO> response = PageResponse.from(page);
    return ResponseEntity.ok(ApiResponse.success(response));
}
```

### 3. Search Endpoint
```java
@PostMapping("/search")
public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> search(@RequestBody SearchRequest request) {
    PageResponse<ProductDTO> result = service.search(request);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

### 4. Dropdown Endpoint
```java
@GetMapping("/dropdown")
public ResponseEntity<ApiResponse<List<IdNameDTO>>> getDropdown() {
    List<IdNameDTO> items = service.getAllForDropdown();
    return ResponseEntity.ok(ApiResponse.success(items));
}
```

### 5. Import Endpoint
```java
@PostMapping("/import")
public ResponseEntity<ApiResponse<ImportResultDTO>> importFile(@RequestParam MultipartFile file) {
    ImportResultDTO result = service.importFromExcel(file);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

### 6. Bulk Delete Endpoint
```java
@DeleteMapping("/bulk")
public ResponseEntity<ApiResponse<BulkOperationResultDTO>> bulkDelete(@RequestBody List<Long> ids) {
    BulkOperationResultDTO result = service.bulkDelete(ids);
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

---

## 📁 Directory Structure

```
common/dto/
├── FILE_INDEX.md                    ← You are here
├── README.md                        ← Detailed documentation
│
├── ApiResponse.java                 ← Response wrapper
├── PageResponse.java                ← Pagination
├── ErrorResponse.java               ← Error handling
│
├── BaseDTO.java                     ← Base class
├── SearchRequest.java               ← Advanced search
│
├── IdNameDTO.java                   ← Dropdowns
├── KeyValueDTO.java                 ← Key-value pairs
│
├── ImportResultDTO.java             ← Import results
└── BulkOperationResultDTO.java      ← Bulk operations
```

---

## 🔧 Where to Place These Files

```
src/main/java/lk/epicgreen/erp/
└── common/
    └── dto/
        ├── ApiResponse.java
        ├── PageResponse.java
        ├── ErrorResponse.java
        ├── BaseDTO.java
        ├── SearchRequest.java
        ├── IdNameDTO.java
        ├── KeyValueDTO.java
        ├── ImportResultDTO.java
        └── BulkOperationResultDTO.java
```

---

## ✅ Implementation Checklist

Setup (One-time):
- [ ] Create `common/dto` package
- [ ] Copy all 9 Java files
- [ ] Update imports in existing code

For Each Module:
- [ ] Extend BaseDTO for entity DTOs
- [ ] Use ApiResponse in all endpoints
- [ ] Use PageResponse for list endpoints
- [ ] Use IdNameDTO for dropdown endpoints
- [ ] Implement search with SearchRequest
- [ ] Handle imports with ImportResultDTO
- [ ] Handle bulk ops with BulkOperationResultDTO

---

## 💡 Best Practices

1. **Always use ApiResponse** - Even for simple endpoints
2. **Extend BaseDTO** - Get audit trail for free
3. **Paginate large lists** - Use PageResponse
4. **Minimize dropdown data** - Use IdNameDTO (not full DTOs)
5. **Provide detailed import feedback** - Use ImportResultDTO
6. **Track bulk operation results** - Show users what failed
7. **Use factory methods** - ApiResponse.success(), ErrorResponse.of()

---

## 🎯 Response Structure Examples

### Success Response
```json
{
  "success": true,
  "message": "Operation completed",
  "data": { /* your data */ },
  "timestamp": "2024-12-10 10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "timestamp": "2024-12-10 10:30:00",
  "status": 400,
  "fieldErrors": {
    "productCode": "Required field"
  }
}
```

### Paginated Response
```json
{
  "success": true,
  "data": {
    "content": [ /* items */ ],
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 100,
    "totalPages": 5,
    "last": false
  }
}
```

---

## 📚 Related Documentation

- **README.md** - Detailed documentation with examples
- **SpringBoot_Implementation_Guide.md** - How to use in Spring Boot
- **Product module** - See real implementation examples

---

## 🎓 Learning Path

**Hour 1:** Read README.md  
**Hour 2:** Study ApiResponse and PageResponse  
**Hour 3:** Study BaseDTO and extend it  
**Hour 4:** Implement in one controller  
**Hour 5:** Update all controllers  

---

## 🔗 Dependencies

These DTOs use:
- Lombok (@Data, @Builder, etc.)
- Jackson (JSON serialization)
- Jakarta Validation (for SearchRequest)
- Spring Data (for PageResponse)

Make sure these are in your pom.xml!

---

## 🎯 Summary

**9 production-ready DTOs** providing:
- ✅ Consistent API responses
- ✅ Standardized error handling
- ✅ Built-in pagination
- ✅ Audit trail support
- ✅ Advanced search capabilities
- ✅ Dropdown support
- ✅ Import/export tracking
- ✅ Bulk operation results

**Total Code:** ~1,550 lines of reusable, production-ready code!

**Use them in every module for professional, consistent APIs!**

---

**Last Updated:** December 2025  
**Version:** 1.0  
**Package:** lk.epicgreen.erp.common.dto
