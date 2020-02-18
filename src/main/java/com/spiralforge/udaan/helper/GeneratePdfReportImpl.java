package com.spiralforge.udaan.helper;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spiralforge.udaan.entity.Donation;
import com.spiralforge.udaan.entity.User;
import com.spiralforge.udaan.util.Utility;

/**
 * @author Sujal
 * @since 2020-02-14.
 */
@Component
public class GeneratePdfReportImpl implements GeneratePdfReport {

	static Logger logger = LoggerFactory.getLogger(GeneratePdfReportImpl.class);

	@Override
	public byte[] generatePdf(User user, Donation donation) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(60);
			table.setWidths(new int[] { 1, 3, 3 });

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Donation Id", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Donation Amount", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Tax Benefit Amount ", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			if (!Objects.isNull(user)) {

				PdfPCell cell;

				cell = new PdfPCell(new Phrase(donation.getDonationId().toString()));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(donation.getScheme().getSchemeAmount().toString()));
				cell.setPaddingLeft(5);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				cell = new PdfPCell(
						new Phrase(String.valueOf(Utility
								.calculateCharges(donation.getScheme().getSchemeAmount(),
										donation.getScheme().getTaxBenefit())
								.toString() + " [ in" + donation.getScheme().getTaxBenefit() + " %]")));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);

				table.addCell(cell);
			}

			PdfWriter.getInstance(document, out);
			document.open();
			String description0 = "Dear User,";
			if (!Objects.isNull(user)) {
				description0 = "Dear " + user.getUserName() + ",";

				document.add(new Paragraph(description0));
				String description = "Thank You for your donation.";
				document.add(new Paragraph(description));
				String description1 = "EMAIL ID: " + user.getEmailId();
				document.add(new Paragraph(description1));
				String description2 = "PAN NUMBER: " + user.getPanNumber();
				document.add(new Paragraph(description2));
				String description3 = "MOBILE NUMBER: " + user.getMobileNumber();
				document.add(new Paragraph(description3));

			}
			String description7 = "";
			document.add(new Paragraph(description7));
			document.add(table);
			String description8 = "";
			document.add(new Paragraph(description8));
			String description4 = "";
			document.add(new Paragraph(description4));
			String description5 = "Sincerely,";
			document.add(new Paragraph(description5));
			String description6 = "Udaan Team";
			document.add(new Paragraph(description6));
			document.close();

		} catch (DocumentException ex) {

			logger.error("Error generating pdf");
		}

		return out.toByteArray();
	}
}
