package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;




public class UserCsvExporter extends ExporterFileType{

	 public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
	    super.setHeader("text/csv",".csv", response);
	   
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
