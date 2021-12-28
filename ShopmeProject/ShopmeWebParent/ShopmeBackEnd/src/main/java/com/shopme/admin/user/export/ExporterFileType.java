package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

public abstract class ExporterFileType {
	
	 public void setHeader(String contentType,String fileExtension,HttpServletResponse response) throws IOException {
		    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    String timestamp = dateFormat.format(new Date());
		    String fileName="User_"+timestamp+fileExtension;
		    response.setContentType(contentType);
		    
		    String headerKey="Content-Disposition";
		    String headerValue= fileName;
		    response.setHeader(headerKey, "Inline; filename=" + headerValue); 
	 }
}
