package lk.epicgreen.erp.report.service.impl;

import lk.epicgreen.erp.admin.dto.response.UserResponse;
import lk.epicgreen.erp.admin.entity.User;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface UserReportCreationService {

    public Workbook generateWorkbook(List<UserResponse> users);
}
