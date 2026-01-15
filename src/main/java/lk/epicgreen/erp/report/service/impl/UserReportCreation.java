package lk.epicgreen.erp.report.service.impl;

import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.entity.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReportCreation implements UserReportCreationService {

    public Workbook generateWorkbook(List<UserResponse> users) {
        Workbook workbook =new SXSSFWorkbook();

        Sheet sheet=workbook.createSheet("Users in the System");

        Row headerRow=sheet.createRow(0);
        headerRow.createCell(0).setCellValue("User Id ");
        headerRow.createCell(1).setCellValue("Username ");
        headerRow.createCell(2).setCellValue("Email ");
        headerRow.createCell(3).setCellValue("First Name ");
        headerRow.createCell(4).setCellValue("Last Name ");
        headerRow.createCell(5).setCellValue("Status");

        int rowIdx=1;
        for(UserResponse user:users){
            Row row=sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getFirstName());
            row.createCell(4).setCellValue(user.getLastName());
            row.createCell(5).setCellValue(user.getStatus());
        }

        return workbook;
    }

}
