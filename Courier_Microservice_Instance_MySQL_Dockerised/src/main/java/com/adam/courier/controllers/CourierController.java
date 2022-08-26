package com.adam.courier.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adam.courier.dao.DeliveryAddressRepository;
import com.adam.courier.dao.LabelRepository;
import com.adam.courier.dao.ProductRepository;
import com.adam.courier.dao.ShippingRepository;
import com.adam.courier.dto.LabelDTO;
import com.adam.courier.entities.Label;
import com.adam.courier.services.LabelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Service
@RequestMapping("/api")
@Api(value="", tags="Courier Manager")
@Tag(name="Courier Manager", description="Service to carry out Label functionality")
public class CourierController {
	
	@Autowired
	private LabelRepository labelRepo;
	
	@Autowired 
	private DeliveryAddressRepository addressRepo;
	
	@Autowired 
	private ProductRepository productRepo;
	
	@Autowired 
	private ShippingRepository shippingRepo;
	
	@Autowired
	private LabelService labelService;
	
//	@Value("${courier-manager.environmentProperty}")
//	private String environmentInstance;
	
	
	/**
	 * Method for pinging and checking the availability of the microservice
	 * @return String Hello World from Courier Microservice!
	 */
	@GetMapping("/ping")
	@ApiOperation(value="Ping the microservice.")
	public String ping()
	{
		return "Hello World from a Production Courier Microservice!";
	}
	
//	/**
//	 * Method added to demonstrate pipeline
//	 * @return String his is the added Ping2 method!
//	 */
//	@GetMapping("/ping2")
//	@ApiOperation(value="Ping the microservice.")
//	public String ping2()
//	{
//		return "This is the added Ping2 method!";
//	}
	
	/**
	 * Method that returns the PDF_Test_Full.pdf label from a file
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/sampleLabel2")
	@ApiOperation(value="Returns the PDF_Test_Full.pdf label from a file.")
	public ResponseEntity<byte[]> sampleLabel2() throws IOException
	{
		System.out.println("SAMPLE LABEL 2 METHOD CALLED!");
		try {
	        FileInputStream fis= new FileInputStream(new File("C:\\Users\\adamc\\OneDrive\\Desktop\\College\\Year 5 Master's\\Placement\\Testing PDFs\\Apache PDF Box\\PDF_Test_Full.pdf"));
	        byte[] targetArray = new byte[fis.available()];
	        fis.read(targetArray);
	        fis.close();
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=sampleReport.pdf");
	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(targetArray);
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/sampleLabel")
	@ApiOperation(value="Returns a hardcoded label as a sample",
	notes="Create and return the label in json format")
	public ResponseEntity sampleLabel() throws IOException
	{
		System.out.println("SAMPLE LABEL METHOD CALLED!");
		ByteArrayInputStream in = labelService.generateTheSample();
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sampleReport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(targetArray);
                //.body(new InputStreamResource(in));
	}
	
	@PostMapping("/label2")
	@ApiOperation(value="Creates a label and returns the json representation of the label in the body",
	notes="Create and return the label in json format")
	ResponseEntity createLabel2(@RequestBody LabelDTO label) {
		//Label label = new Label(sellerDTO);
		//log.info("CreateLabel method called");
		String result = validateLabelComponents(label);
		if(!(result.equals("")))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		labelRepo.save(new Label(label));
		return ResponseEntity.status(HttpStatus.CREATED).body(label);
	}
	
	@PostMapping("/label")
	@ApiOperation(value="Create a label",
	notes="Create and return a label in PDF format")
	ResponseEntity createLabel(@RequestBody LabelDTO labelDTO) throws IOException {
		//Label label = new Label(sellerDTO);
		//log.info("CreateLabel method called");
		String result = validateLabelComponents(labelDTO);
		if(!(result.equals("")))
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
		//ResponseEntity labelAlreadyExists = getLabelById(labelDTO.getId());
		Optional<Label> labelExists = labelRepo.findById(labelDTO.getId());
		if(labelExists.isPresent())
		{
			return ResponseEntity.ok("Label with id of " + labelDTO.getId() + " already exists");
		}

		ByteArrayInputStream in = labelService.createLabel(labelDTO);
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sampleReport.pdf");
        Label label = new Label(labelDTO);
        try
        {
        	labelRepo.save(label);
            
            return ResponseEntity
            		.status(HttpStatus.CREATED)
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(targetArray);
                    //.body(new InputStreamResource(in));
        }
        catch(ConstraintViolationException e)
        {
        	
        	StringBuilder message = new StringBuilder();
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
              message.append(violation.getMessage().concat("\n"));
            }

        	return ResponseEntity
            		.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message.toString());
                    //.body(new InputStreamResource(in));
        }
	}
	
	@PostMapping("/label3")
	@ApiOperation(value="Creates a label and returns it as an attachment",
	notes="Create and return the label as an attachment")
	ResponseEntity createLabel3(@RequestBody LabelDTO label) throws IOException {
		//Label label = new Label(sellerDTO);
		//log.info("CreateLabel method called");
		if(label.getAddress() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		ByteArrayInputStream in = labelService.createLabel(label);
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sampleReport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(targetArray);
                //.body(new InputStreamResource(in));
	}
	
	@GetMapping("/labels")
	@ApiOperation(value="Return the list of labels",
	notes="Returns all labels in json format")
	Iterable<Label> getLabels()
	{
		Iterable<Label> labels = labelRepo.findAll();
		return labels;
	}
	
	@GetMapping("/checkLabel/{id}")
	@ApiOperation(value="Check if the label exists",
	notes="Returns a 200 if exists or 404 if not")
	ResponseEntity checkLabel(@PathVariable Long id)
	{
		Optional<Label> label = labelRepo.findById(id);
		if(label.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/label/{id}")
	@ApiOperation(value="Return a label in PDF format",
	notes="Returns the label based on the provided id")
	ResponseEntity getLabelById(@PathVariable Long id) throws IOException
	{
		Optional<Label> label = labelRepo.findById(id);
		if(label.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}
		LabelDTO labelDTO = new LabelDTO(label.get());
		ByteArrayInputStream in = labelService.createLabel(labelDTO);
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sampleReport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(targetArray);
	}
	
	@DeleteMapping("/label/{id}")
	@ApiOperation(value="Deletes a label based on the provided id",
	notes="Also deletes the associated address, products and shipping records")
	ResponseEntity deleteLabelById(@PathVariable Long id) throws IOException
	{
		Optional<Label> label = labelRepo.findById(id);
		if(label.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}
		
		labelRepo.deleteById(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Deleted label with ID: " + id);
	}
	
	@DeleteMapping("/labels")
	@ApiOperation(value="Deletes all labels",
	notes="Also deletes the associated address, products and shipping records")
	ResponseEntity deleteLabels() throws IOException
	{
		try
		{
			labelRepo.deleteAll();
			
	        return ResponseEntity
	                .ok()
	                .contentType(MediaType.TEXT_PLAIN)
	                .body("Deleted all labels");
		}
		catch(Exception e)
		{
			return ResponseEntity
	                .accepted()
	                .contentType(MediaType.TEXT_PLAIN)
	                .body("Error deleting labels: " + e.getMessage());
		}
	}
	
	@PutMapping("/label/{id}")
	@ApiOperation(value="Update a label based on the provided id",
	notes="Takes in a label body and updates the label and it's associations")
	ResponseEntity updateLabelById(@PathVariable Long id, @RequestBody LabelDTO labelDTO) throws IOException
	{
		Optional<Label> label = labelRepo.findById(id);
		if(label.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Label with id " + id + " not found");
		}
		
		labelDTO.setId(id);
		
		ByteArrayInputStream in = labelService.createLabel(labelDTO);
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sampleReport.pdf");
        Label updatedLabel = new Label(labelDTO);
        labelRepo.save(updatedLabel);
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(targetArray);
	}
	
	/**
	 * Takes in an id and returns a PDF as an attachment
	 * @param id
	 * @return The PDF content
	 * @throws IOException
	 */
	@GetMapping("/label/download/{id}")
	@ApiOperation(value="Returns a label associated with the given id",
	notes="Returns the label as an attachment")
	ResponseEntity getLabelByIdDownload(@PathVariable Long id) throws IOException
	{
		Optional<Label> label = labelRepo.findById(id);
		if(label.isEmpty())
		{
			return ResponseEntity.notFound().build();
		}
		LabelDTO labelDTO = new LabelDTO(label.get());
		ByteArrayInputStream in = labelService.createLabel(labelDTO);
		//ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		byte[] targetArray = in.readAllBytes();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=sampleReport.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(targetArray);
	}
	
	/**
	 * Method to validate that the child objects of the label are not null
	 * @param label
	 * @return An empty string if valid, or a message explaining the issue
	 */
	private String validateLabelComponents(LabelDTO label)
	{
		if(label.getAddress() == null) {
			return "Address cannot be null";
		}
		if(label.getProducts() == null || label.getProducts().isEmpty())
		{
			return "Products list cannot be null and must contain at least one product";
		}
		if(label.getShipping() == null)
		{
			return "Shipping details cannot be null";
		}
		return "";
	}

}
