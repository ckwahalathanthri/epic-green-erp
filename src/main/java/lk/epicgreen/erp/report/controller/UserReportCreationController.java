package lk.epicgreen.erp.report.controller;

import org.springframework.http.*;
import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.entity.User;
import lk.epicgreen.erp.admin.service.impl.UserServiceImpl;
import lk.epicgreen.erp.report.service.impl.UserReportCreation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users/export")
@CrossOrigin(origins = "http://localhost:4200")
public class UserReportCreationController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserReportCreation userReportCreationService;

    @GetMapping
    public ResponseEntity<byte[]> exportUsersToExcel(){
        List<UserResponse> users=userService.getAllUsers();
        SXSSFWorkbook workbook=(SXSSFWorkbook) userReportCreationService.generateWorkbook(users);
        try(ByteArrayOutputStream outputStream=new ByteArrayOutputStream()){
            workbook.write(outputStream);
            byte[] content=outputStream.toByteArray();
            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ));
            headers.setContentDisposition(
                    ContentDisposition.attachment().filename("user-data.xlsx").build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(content);

        }catch (IOException exp){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }finally {
            workbook.dispose();
        }


    }
}