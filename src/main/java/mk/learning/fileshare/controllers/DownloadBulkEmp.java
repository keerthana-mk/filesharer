package mk.learning.fileshare.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mk.learning.fileshare.Services.SearchFileBasedOnEmp;
import mk.learning.fileshare.constants.HashmapConstants;

@RestController
public class DownloadBulkEmp {
	
	private final Logger logger=LoggerFactory.getLogger(DownloadBulkEmp.class);
	

	@Autowired
	SearchFileBasedOnEmp service;
	

	
	/*
	 * * input : functionality, HR's employee code { which eventually we will get from login user}
	 * * output : list of all files of employees for whom HR is the given HR.
	 * * resources : A hashmap of employee code mapped to HR empcode {which is formed using /upload through admin}
	 * * logic : 1) From the hashmap get all keys(empCodes) for which value is HR's empcode { this will be a list }
	 * 			 2) pass this list to fileServices.getFilePaths(), this will then return a list of filePaths.
	 * 			 3) further, pass this list of filepaths to zipFunction, this will create a zip and return its name.
	 * 			 4) Download that zip.
	 */
	//downloadBulk/{functionality}/{subFunc}
	@RequestMapping(value = "downloadBulk/{functionality}", method = RequestMethod.GET)
	public ResponseEntity<Resource> Download(HttpServletResponse response, @PathVariable String functionality) throws IOException {
		ArrayList<String> filePaths= new ArrayList<>(); //filepaths is list of all files for a given HR
		boolean a=functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_HR);
		System.out.println("a="+a);
		if (functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_HR)) {
		ArrayList<String> empCodeList = new ArrayList<String>();
		 Object Principle=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String loggedInUsername;
		 if(Principle instanceof UserDetails)
			 loggedInUsername=((UserDetails)Principle).getUsername();
		 else
			 loggedInUsername=Principle.toString();
		 System.out.println("logged in username="+loggedInUsername);
		 
			empCodeList = service.FindAllEmpForHR(loggedInUsername); //should be returned by a function. logic (1)
			
			//logic(2) service.getFilePaths4Employee(empCodeList,HashmapConstants.FUNCTIONALITY_HR)
			filePaths = service.getFilePaths4Employee(empCodeList, HashmapConstants.FUNCTIONALITY_HR);
			logger.info("Filepaths= {}",filePaths);
			
			if (filePaths.size() == 0)
				return ResponseEntity.noContent().build();

			System.out.println(filePaths.size());
			String downloadZipFileName = service.zipFiles(filePaths); //logic(3)
			logger.info("Download Bulk :downloadZipFileName={}",downloadZipFileName);

			if (downloadZipFileName.equalsIgnoreCase(HashmapConstants.ZIP_EXCEPTION))
				return ResponseEntity.noContent().build();

			InputStreamResource ipStreamResource = new InputStreamResource(
					new FileInputStream(new File(downloadZipFileName)));
			/*
			 * return ResponseEntity .ok()
			 * .contentType(MediaType.parseMediaType("application/octet-stream"))
			 * .body(ipStreamResource) ;
			 */
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
					.header("Content-Disposition", "attachment;filename= Keerthana.zip")
					.body(ipStreamResource)
					;

		} else {
			logger.info("In Else part of DownloadController");
			return null;
		}
	}

	@RequestMapping(value = "downloadBulk/{functionality}/{subfunc}", method = RequestMethod.GET)
	public ResponseEntity<Resource> Download(HttpServletResponse response, @PathVariable String functionality,@PathVariable String subfunc) throws IOException {
		ArrayList<String> filePaths= new ArrayList<>(); //filepaths is list of all files for a given HR
		boolean a=functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_HR);
		System.out.println("a="+a);
		if (functionality.equalsIgnoreCase(HashmapConstants.FUNCTIONALITY_HR)) {
		ArrayList<String> empCodeList = new ArrayList<String>();
		 Object Principle=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 String loggedInUsername;
		 if(Principle instanceof UserDetails)
			 loggedInUsername=((UserDetails)Principle).getUsername();
		 else
			 loggedInUsername=Principle.toString();
		 System.out.println("logged in username="+loggedInUsername);
		 
			empCodeList = service.FindAllEmpForHR(loggedInUsername); //should be returned by a function. logic (1)
			
			//logic(2) service.getFilePaths4Employee(empCodeList,HashmapConstants.FUNCTIONALITY_HR)
			filePaths = service.getFilePaths4Employee(empCodeList, HashmapConstants.FUNCTIONALITY_HR,subfunc);
			logger.info("Filepaths= {}",filePaths);
			
			if (filePaths.size() == 0)
				return ResponseEntity.noContent().build();

			System.out.println(filePaths.size());
			String downloadZipFileName = service.zipFiles(filePaths); //logic(3)
			logger.info("Download Bulk :downloadZipFileName={}",downloadZipFileName);

			if (downloadZipFileName.equalsIgnoreCase(HashmapConstants.ZIP_EXCEPTION)) 
				return ResponseEntity.noContent().build();

			InputStreamResource ipStreamResource = new InputStreamResource(
					new FileInputStream(new File(downloadZipFileName)));
			/*
			 * return ResponseEntity .ok()
			 * .contentType(MediaType.parseMediaType("application/octet-stream"))
			 * .body(ipStreamResource) ;
			 */
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
					.header("Content-Disposition", "attachment;filename= Keerthana.zip")
					.body(ipStreamResource)
					;

		} else {
			logger.info("In Else part of DownloadController");
			return null;
		}
	}

}
