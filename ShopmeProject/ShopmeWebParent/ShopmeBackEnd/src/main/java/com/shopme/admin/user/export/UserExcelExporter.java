package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;


public class UserExcelExporter extends ExporterFileType {
	 
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setHeader("application/octet-stream", ".xlsx", response);
		XSSFWorkbook workbook= new XSSFWorkbook();
		XSSFSheet sheet=workbook.createSheet("List of Users");
		XSSFCellStyle xssfCellStyle=workbook.createCellStyle();
		XSSFFont xssfFont=workbook.createFont();
		xssfFont.setBold(true);
		xssfFont.setFontHeight(12);
		xssfCellStyle.setFont(xssfFont);
		int rowCount=0;
		XSSFRow firstRow=sheet.createRow(0);
		writeHeader(firstRow,xssfCellStyle);
			for(User user : listUsers) {
				XSSFRow row=sheet.createRow(++rowCount);
				writeUser(user, row);
			}
		
		ServletOutputStream outputStream=response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeHeader(XSSFRow firstRow,XSSFCellStyle xssfCellStyle) {
		XSSFCell cell;
		cell=firstRow.createCell(0);
		cell.setCellValue("User Id");
		cell.setCellStyle(xssfCellStyle);
		cell=firstRow.createCell(1);
		cell.setCellValue("E-mail");
		cell.setCellStyle(xssfCellStyle);
		cell=firstRow.createCell(2);
		cell.setCellValue("First Name");
		cell.setCellStyle(xssfCellStyle);
		cell=firstRow.createCell(3);
		cell.setCellValue("Last Name");
		cell.setCellStyle(xssfCellStyle);
		cell=firstRow.createCell(4);
		cell.setCellValue("Role");
		cell.setCellStyle(xssfCellStyle);
		cell=firstRow.createCell(5);
		cell.setCellValue("Enabled");
		cell.setCellStyle(xssfCellStyle);
		
	}

	private void writeUser(User user, XSSFRow row) {
		XSSFCell cell;
		cell=row.createCell(0);
		cell.setCellValue(user.getId());
		cell=row.createCell(1);
		cell.setCellValue(user.getEmail());
		cell=row.createCell(2);
		cell.setCellValue(user.getFirstName());
		cell=row.createCell(3);
		cell.setCellValue(user.getLastName());
		cell=row.createCell(4);
		String role=user.getRoles().toString();
		cell.setCellValue(role);
		cell=row.createCell(5);
		if(user.isEnabled()) {
		cell.setCellValue("True");
		} else {
			cell.setCellValue("False");
		}
		
	}
}
