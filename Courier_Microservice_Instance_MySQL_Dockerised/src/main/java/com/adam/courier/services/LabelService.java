package com.adam.courier.services;

import java.awt.Color;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.stereotype.Service;

import com.adam.courier.dto.DeliveryAddressDTO;
import com.adam.courier.dto.LabelDTO;
import com.adam.courier.dto.ProductDTO;
import com.adam.courier.dto.ShippingDTO;

import java.text.DecimalFormat;

@Service
public class LabelService {

	// Global Variables

	// Starting Y Value for the delivery address section
	// Will change with each new line
	private float deliveryAddressCurrentYValue = 600;

	// X position for delivery address section
	// Will remain the same
	private float deliveryAddressCurrentXValue = 70;

	// Scanner that takes user input as an input stream
	private Scanner in = new Scanner(System.in);

	// The initialisation x position for each rectangle in a table
	private int tableInitX = 70;

	// The initialisation y position for each rectangle in a table
	private int tableInitY = 430;

	// The PDF document
	private PDDocument doc = new PDDocument();

	// The PDF Page
	private PDPage page = new PDPage();

	// Create the content stream so can write to page
	private PDPageContentStream content;

	// Boundaries of the page
	private PDRectangle mediaBox = page.getMediaBox();

	private LabelDTO label = new LabelDTO();
	
	// Format prices and decimals to two decimal places
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	/**
	 * Method that generates a hardcoded label and returns the pdf in a ByteArrayInputStream
	 * @return ByteArrayInputStream to return to the user
	 * @throws IOException
	 */
	public ByteArrayInputStream generateTheSample() throws IOException {
		
		// Create hardcoded label
		label = createSampleLabel();

		// Create a new page and add the content
		newPage(label);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		doc.save(bao);
		doc.close();
		ByteArrayInputStream in = new ByteArrayInputStream(bao.toByteArray());
		return in;
	}
	
	public ByteArrayInputStream createLabel(LabelDTO label) throws IOException {

		// Create a new page and add the provided label
		newPage(label);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		doc.save(bao);
		doc.close();
		ByteArrayInputStream in = new ByteArrayInputStream(bao.toByteArray());
		return in;
	}
	
	private void newPage(LabelDTO label) throws IOException {
		// Create a new document and page, and add the page to the doc
		doc = new PDDocument();
		page = new PDPage();
		doc.addPage(page);
		
		// Starting Y Value for the delivery address section
		// Will change with each new line
		deliveryAddressCurrentYValue = 600;

		// X position for delivery address section
		// Will remain the same
		deliveryAddressCurrentXValue = 70;

		// The initialisation x position for each rectangle in a table
		tableInitX = 70;

		// The initialisation y position for each rectangle in a table
		tableInitY = 430;

		content = new PDPageContentStream(doc, page);

		// Title for the document
		String title = "Carolan Collect";
		// Font for the title
		PDFont font = PDType1Font.TIMES_BOLD_ITALIC;
		// Font size for the title
		int fontSize = 24;

		// Call method to create the title
		createTitle(title, font, fontSize, mediaBox, content);

		// Call method to print delivery address title
		printDeliveryAddressTitle(content);

		// Insert logo
		drawLogo(content);
		
		// Print the Delivery address
		printDeliveryAddress(label.getAddress(), content);
		
		// Print the product table
		createProductTable(content, label.getProducts());
		
		// Print the shipping table
		createShippingTable(content, label.getShipping());
	}

	/**
	 * Method to create the title for the PDF document
	 * 
	 * @param title    The text to include in the title
	 * @param font     The font type to use
	 * @param fontSize The font size to use
	 * @param mediaBox The boundaries of the page
	 * @param content  The content stream to use
	 * @throws IOException
	 */
	private void createTitle(String title, PDFont font, int fontSize, PDRectangle mediaBox,
			PDPageContentStream content) throws IOException {

		// Width of the title
		// The width of the string in 1/1000 units of text space, so divide by 1000
		float titleWidth = font.getStringWidth(title) / 1000 * fontSize;

		// Height of the title according to font
		float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		// Centre the text
		// Starting Y position of the text
		// Height of the page boundaries - 70 text space - the height of the title
		float startY = mediaBox.getHeight() - 70 - titleHeight;

		// Starting x position of the text
		// The (page width - the title width) / 2
		float startX = (mediaBox.getWidth() - titleWidth) / 2;

		// Set the stroking colour for the text underline
		content.setStrokingColor(Color.DARK_GRAY);

		// Set the underline width
		content.setLineWidth(2f);

		// Start writing to content stream
		content.beginText();

		// New line at the start x and y positions
		content.newLineAtOffset(startX, startY);

		// Setting the font
		content.setFont(font, fontSize);

		// Write text to content stream
		content.showText(title);

		// End writing text
		content.endText();

		// Move the content to the starting x position
		// and starting y position - 5 text space (for the underline)
		content.moveTo(startX, startY - 5);

		// Set the underline at the position for the width of the title
		content.lineTo(startX + titleWidth, startY - 5);

		// Draw the underline
		content.stroke();
	}

	/**
	 * Method to print the title for the delivery address
	 * 
	 * @param content The content stream to write to
	 * @throws IOException
	 */
	private void printDeliveryAddressTitle(PDPageContentStream content) throws IOException {
		// Begin writing text
		content.beginText();

		// Set the new line to the initial x and y values for the delivery address
		// (global vars)
		content.newLineAtOffset(deliveryAddressCurrentXValue, deliveryAddressCurrentYValue);

		// Set the font and font size
		content.setFont(PDType1Font.HELVETICA_BOLD, 14);

		// Write the text
		content.showText("Delivery Address");

		// Finish writing text
		content.endText();
	}

	/**
	 * Method to draw the courier logo to PDF
	 * 
	 * @param content The content stream to write to
	 * @throws IOException
	 */
	private void drawLogo(PDPageContentStream content) throws IOException {

		File f = new File("/src/main/resources/Logo4.jpg");
		
		// Creates the image object from a file
		//PDImageXObject logo = PDImageXObject.
		PDImageXObject logo = PDImageXObject.createFromFileByContent(f, doc);

		// Draw the image at the specified xy coordinates
		content.drawImage(logo, 70, 650);
	}

	private LabelDTO createSampleLabel() throws IOException {
		// Delivery address object created based on hardcoded values
		DeliveryAddressDTO address = createSampleDeliveryAddress();

		// The list of product objects
		List<ProductDTO> products = new ArrayList<>();

		// Product object created based on hardcoded values
		ProductDTO product = createSampleProduct();

		// Shipping object created based on hardcoded values
		ShippingDTO shipping = createSampleShipping();

		// Add product to list
		products.add(product);

		// Return the Label
		return new LabelDTO(100000L, address, products, shipping);
	}

	/**
	 * Method to print the delivery address
	 * 
	 * @param address The Address to print
	 * @param content The content stream to write to
	 * @throws IOException
	 */
	private void printDeliveryAddress(DeliveryAddressDTO address, PDPageContentStream content) throws IOException {
		// Begin writing text
		content.beginText();

		// Set the font and size
		content.setFont(PDType1Font.COURIER, 12);

		// The delivery address y value moves down 20 text spaces i.e., go down 1 linw
		deliveryAddressCurrentYValue -= 20;

		// New line at the delivery address x and y coordinates
		content.newLineAtOffset(deliveryAddressCurrentXValue, deliveryAddressCurrentYValue);

		// Write the address name
		content.showText(address.getFirstName() + " " + address.getLastName());

		// Keep the same x position but move y down 20 text spaces - new lines
		// Write the address values to the content stream
		content.newLineAtOffset(0, -20);
		content.showText(address.getAddress1());
		content.newLineAtOffset(0, -20);
		content.showText(address.getAddress2());
		content.newLineAtOffset(0, -20);
		content.showText(address.getCity());
		content.newLineAtOffset(0, -20);
		content.showText(address.getCounty());
		content.newLineAtOffset(0, -20);
		content.showText(address.getPostcode());
		content.newLineAtOffset(0, -20);
		content.showText(address.getPhoneNo());

		// End writing text to content stream
		content.endText();
	}

	/**
	 * Method to create a table of products
	 * 
	 * @param content  The content stream to write to
	 * @param products The list of products to use
	 * @throws IOException
	 */
	private void createProductTable(PDPageContentStream content, List<ProductDTO> products) throws IOException {

		// Set the colour
		content.setStrokingColor(Color.DARK_GRAY);

		// Set line width
		content.setLineWidth(1f);

		// Set cell height and width
		int cellHeight = 20;
		int cellWidth = 200;

		// Move the content i.e., the cells, to the current x and y coordinates
		content.moveTo(tableInitX, tableInitY);

		// Set column count
		int cols = 5;

		// Create productTable headers and set fonts
		String heading = "";
		int fontSize = 10;
		PDFont font = PDType1Font.HELVETICA_BOLD;

		// Loop to create the heading cells
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				cellWidth = 200;
				heading = "Product";
			} else if (i == 1) {
				cellWidth = 60;
				heading = "Quantity";
			} else if (i == 2) {
				cellWidth = 60;
				heading = "Price";
			} else if (i == 3) {
				cellWidth = 60;
				heading = "Tax Incl";
			} else if (i == 4) {
				cellWidth = 60;
				heading = "Total";
			}

			// Create the current cell
			addCell(cellWidth, cellHeight, content, font, heading, fontSize);
		}

		// Create next line in table
		newLineInTable(cellHeight);

		// Loop through the products to get the relevant values
		for (ProductDTO product : products) {
			for (int j = 0; j < cols; j++) {
				String value = "";
				font = PDType1Font.COURIER;
				if (j == 0) {
					cellWidth = 200;
					value = product.getName();
				} else if (j == 1) {
					cellWidth = 60;
					value = String.valueOf((product.getQuantity()));
				} else if (j == 2) {
					cellWidth = 60;
					value = '\u20ac' + String.valueOf(df.format(product.getPrice()));
				} else if (j == 3) {
					cellWidth = 60;
					value = '\u20ac' + String.valueOf(df.format(product.getPriceTaxIncluded()));
				} else if (j == 4) {
					cellWidth = 60;
					value = '\u20ac' +"" + String.valueOf(df.format(product.getQuantity() * product.getPriceTaxIncluded()));
				}

				// Shorten the length of the value if it is greater than 30 characters
				if (value.length() > 30) {
					value = value.substring(0, 30) + "-";
				}

				// Create the current cell
				addCell(cellWidth, cellHeight, content, font, value, fontSize);
			}
			// Create next line in table
			newLineInTable(cellHeight);
		}
		// Draw the content
		content.stroke();
	}

	/**
	 * Method to create the shipping table
	 * 
	 * @param content  The content stream to write to
	 * @param shipping The Shipping object to use
	 * @throws IOException
	 */
	private void createShippingTable(PDPageContentStream content, ShippingDTO shipping) throws IOException {
		// The new table will start 20 text spaces below the products table
		tableInitY -= 20;

		// Set the colour
		content.setStrokingColor(Color.DARK_GRAY);

		// Set line width
		content.setLineWidth(1f);

		// Set cell height and width
		int cellHeight = 20;
		int cellWidth = 120;

		// Move the content to the x and y coordinates
		content.moveTo(tableInitX, tableInitY);

		// Set column count
		int cols = 3;

		// Create shippingTable headers and set fonts
		String heading = "";
		int fontSize = 10;
		PDFont font = PDType1Font.HELVETICA_BOLD;

		// Loop to display the table headers
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				heading = "Shipping Weight (Kg)";
			} else if (i == 1) {
				heading = "Shipping Tax Excluded";
			} else if (i == 2) {
				heading = "Shipping Tax Included";
			}

			// Create the current cell
			addCell(cellWidth, cellHeight, content, font, heading, fontSize);
		}

		// Create next line in table
		newLineInTable(cellHeight);

		// Loop to display the shipping values
		for (int j = 0; j < cols; j++) {
			String value = "";
			font = PDType1Font.COURIER;
			if (j == 0) {
				value = String.valueOf(shipping.getShippingWeight());
			} else if (j == 1) {
				value = '\u20ac' + String.valueOf(shipping.getPriceTaxExcluded());
			} else if (j == 2) {
				value = '\u20ac' + String.valueOf(shipping.getPriceTaxIncluded());
			}

			// Shorten value length if greater than 30 characters
			if (value.length() > 30) {
				value = value.substring(0, 30) + "-";
			}

			// Create the current cell
			addCell(cellWidth, cellHeight, content, font, value, fontSize);
		}
		// Create next line in table
		newLineInTable(cellHeight);

		// Draw the content
		content.stroke();

		// Close the content stream
		content.close();
	}

	/**
	 * Method to add a cell to a table
	 * 
	 * @param cellWidth  The width of the cell
	 * @param cellHeight The height of the cell
	 * @param content    The content stream to write to
	 * @param font       The font type to use
	 * @param text       The text to use
	 * @param fontSize   The font size to use
	 * @throws IOException
	 */
	private void addCell(int cellWidth, int cellHeight, PDPageContentStream content, PDFont font, String text,
			int fontSize) throws IOException {

		// Create the current rectangle i.e., cell and add to content
		PDRectangle currentRect = new PDRectangle(tableInitX, tableInitY, cellWidth, -cellHeight);
		content.addRect(tableInitX, tableInitY, cellWidth, -cellHeight);

		content.beginText();

		// Centre the text
		// Create a float array with the coordinates to centre the text
		float[] centredCoordinates = centreTableText(currentRect, font, text, fontSize, cellHeight);

		// Extract the centred coordinates
		float startX = centredCoordinates[0];
		float startY = centredCoordinates[1];

		// New line at centred coordinates
		content.newLineAtOffset(startX, startY);

		// Set font and show text
		content.setFont(font, fontSize);
		content.showText(text);
		content.endText();

		// The new cell's x position moves across x axis by the value of the cell's
		// width
		tableInitX += cellWidth;
	}

	private void newLineInTable(int cellHeight) {
		// The new cell's x position is reset
		tableInitX = 70;

		// The new cell's y position moves down one line
		tableInitY -= cellHeight;
	}

	/**
	 * Method to convert label to JSON String and save to JSON file
	 * 
	 * @param label The label to save as JSON
	 * @throws StreamWriteException
	 * @throws DatabindException
	 * @throws IOException
	 */
//		private  void serializeLabelInJson(Label label) throws StreamWriteException, DatabindException, IOException {
//			System.out.println(label);
//
//			// Create object mapper to write label to JSON
//			ObjectMapper mapper = new ObjectMapper();
//
//			// Write the label to a JSON file
//			// File will be saved as SampleJSON + first name
//			mapper.writeValue(new File(
//					"C:\\Users\\adamc\\OneDrive\\Desktop\\College\\Year 5 Master's\\Placement\\SampleJson\\SampleLabel"
//							+ label.getAddress().getFirstName() + ".json"),
//					label);
//			String jsonInString = mapper.writeValueAsString(label);
//			System.out.println(jsonInString);
//		}

	/**
	 * Method to save label to PDF and close the document. Also asks user if they
	 * want to print.
	 * 
	 * @param label The Label to save to PDF
	 * @param doc   The PDF Document
	 * @throws IOException
	 * @throws PrinterException
	 */
	private void saveLabelAndCloseDoc(LabelDTO label, PDDocument doc) throws IOException, PrinterException {
		// Save the document to specified location
		doc.save(
				"C:\\Users\\adamc\\OneDrive\\Desktop\\College\\Year 5 Master's\\Placement\\Testing PDFs\\Apache PDF Box\\PDF_Test_Full.pdf");
		System.out.println("PDF created");

		// Take user input to determine if PDF should be printed
//		String input = "";
//		while (!(input.equals("1") || input.equals("-1"))) {
//			System.out.println("\nEnter 1 to print the document or -1 to skip printing: ");
//			input = in.nextLine();
//		}
//
//		// If input is 1, print the PDF
//		if (input.equals("1")) {
//			printPDF(doc);
//		}

		// Close the doc and finish application
		System.out.println("\nFinished!");
		doc.close();
	}

	/**
	 * Method to print the PDF using the default printer
	 * 
	 * @param doc The PDF Document to print
	 */
	public void printPDF(PDDocument doc) {
		// Create the pageable object
		PDFPageable pageable = new PDFPageable(doc);

		// Create a printer job
		PrinterJob printJob = PrinterJob.getPrinterJob();

		// Set the printer job to use the PDF
		printJob.setPageable(pageable);

		// Attempt to print using default printer
		try {
			printJob.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Method that hardcodes a delivery address
	 * 
	 * @return DeliveryAddress Object
	 * @throws IOException
	 */
	private DeliveryAddressDTO createSampleDeliveryAddress() throws IOException {
		DeliveryAddressDTO address = new DeliveryAddressDTO();
		address.setFirstName("Adam");
		address.setLastName("Carolan");
		address.setAddress1("Rathcobican");
		address.setAddress2("");
		address.setCity("Rhode");
		address.setCounty("Offaly");
		address.setPostcode("R35K308");
		address.setPhoneNo("087xxxxxxx");

		return address;
	}

	/**
	 * Method to create hardcoded Product
	 * 
	 * @return The Product
	 * @throws IOException
	 */
	private ProductDTO createSampleProduct() throws IOException {
		ProductDTO product = new ProductDTO();
		product.setName("XXL T-Shirt");
		product.setQuantity(5);
		product.setPrice(12.00);
		product.setPriceTaxIncluded(14.76);

		return product;
	}

	/**
	 * Method to create hardcoded Shipping
	 * 
	 * @return The Shipping Object
	 * @throws IOException
	 */
	private ShippingDTO createSampleShipping() throws IOException {
		ShippingDTO shipping = new ShippingDTO();
		shipping.setPriceTaxExcluded(5.80);
		shipping.setPriceTaxIncluded(7.30);
		shipping.setShippingWeight(0.3);

		return shipping;
	}

	/**
	 * Method to centre the text in a table cell
	 * 
	 * @param currentRect The current rectangle i.e., cell
	 * @param font        The font to use
	 * @param text        The text to use
	 * @param fontSize    The font size to use
	 * @param cellHeight  The height of the cell
	 * @return
	 * @throws IOException
	 */
	private float[] centreTableText(PDRectangle currentRect, PDFont font, String text, int fontSize,
			int cellHeight) throws IOException {
		// The width in text spaces of the text provided
		float titleWidth = font.getStringWidth(text) / 1000 * fontSize;

		// The starting x position of the text
		// Based on the starting x position of the cell + (cell width - title width) / 2
		float startX = tableInitX + (currentRect.getWidth() - titleWidth) / 2;

		// The starting y position of the text
		// Based on 7 text spaces up from the bottom of the cell
		float startY = tableInitY - cellHeight + 7;

		// Return the array with the starting x and y positions
		return new float[] { startX, startY };
	}
}
