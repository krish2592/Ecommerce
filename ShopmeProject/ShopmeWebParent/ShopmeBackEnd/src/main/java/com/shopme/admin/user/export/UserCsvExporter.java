package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;


public class UserCsvExporter {

	 public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
	    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    String timestamp = dateFormat.format(new Date());
	    String csvFileName="User_"+timestamp+".csv";
	    response.setContentType("text/csv");
	    
	    String headerKey="Content-Disposition";
	    String headerValue= csvFileName;
	    response.setHeader(headerKey, "Inline; filename=" + headerValue); 
	    
	    ICsvBeanWriter csvBeanWriter=new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
	    String[] header= {"UserId", "E-mail","First Name", "Last Name", "Roles","Enabled"};
	    String[] fieldMap = {"id", "email", "firstName", "lastName","roles","enabled"};
	    csvBeanWriter.writeHeader(header);
	    for(User user : listUsers) {
	    	csvBeanWriter.write(user, fieldMap);
	    }
	    csvBeanWriter.close();
	 }
}
