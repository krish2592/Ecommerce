package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

public class UserPdfExporter extends ExporterFileType{

public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
	super.setHeader("application/pdf", ".pdf", response);
	
	Document document=new Document(PageSize.A4);
	PdfWriter.getInstance(document,response.getOutputStream());
	document.open();
	
	Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	font.setSize(14);
	font.setColor(Color.blue);
	
	Paragraph paragraph =new Paragraph("List of Users",font);
	paragraph.setAlignment(Element.ALIGN_CENTER);
	document.add(paragraph);

	PdfPTable pdfPTable=new PdfPTable(6);
	pdfPTable.setWidthPercentage(100f);
	pdfPTable.setSpacingBefore(10f);
	pdfPTable.setWidths(new float[] {1.2f,4f,2.9f,2.9f,3.0f,1.8f});
	writeTableHeader(pdfPTable);
	writeTableElement(pdfPTable,listUsers);
	document.add(pdfPTable);
	document.close();
}


private void writeTableHeader(PdfPTable pdfPTable) {
	PdfPCell pdfPCell=new PdfPCell();
	pdfPCell.setBackgroundColor(Color.BLUE);
	pdfPCell.setPadding(6);
	
	Font font=FontFactory.getFont(FontFactory.HELVETICA);
	font.setColor(Color.WHITE);
	font.setSize(12);
	pdfPCell.setPhrase(new Phrase("User Id", font));
	pdfPTable.addCell(pdfPCell);
	pdfPCell.setPhrase(new Phrase("E-mail", font));
	pdfPTable.addCell(pdfPCell);
	pdfPCell.setPhrase(new Phrase("First Name", font));
	pdfPTable.addCell(pdfPCell);
	pdfPCell.setPhrase(new Phrase("Last Name", font));
	pdfPTable.addCell(pdfPCell);
	pdfPCell.setPhrase(new Phrase("Roles", font));
	pdfPTable.addCell(pdfPCell);
	pdfPCell.setPhrase(new Phrase("Enabled", font));
	pdfPTable.addCell(pdfPCell);
}

private void writeTableElement(PdfPTable pdfPTable, List<User> listUsers) {
	for(User user:listUsers) {
		pdfPTable.addCell(String.valueOf(user.getId()));
		pdfPTable.addCell(String.valueOf(user.getEmail()));
		pdfPTable.addCell(String.valueOf(user.getFirstName()));
		pdfPTable.addCell(String.valueOf(user.getLastName()));
		pdfPTable.addCell(String.valueOf(user.getRoles()));
		pdfPTable.addCell(String.valueOf(user.isEnabled()));
	}
	
}

}
