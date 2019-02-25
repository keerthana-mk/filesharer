package mk.learning.fileshare.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mk.learning.fileshare.Services.FileServices;
import mk.learning.fileshare.Services.SearchFileBasedOnEmp;
import mk.learning.fileshare.constants.HashmapConstants;;

@RestController
public class DownloadControllerTest {
	private final Logger logger = LoggerFactory.getLogger(DownloadControllerTest.class);

	@Autowired
	SearchFileBasedOnEmp service;
	
	
	@RequestMapping(value = "download/{functionality}/{empCode}", method = RequestMethod.GET)
	public ResponseEntity<Resource> Download(HttpServletResponse response, @PathVariable String functionality,
			@PathVariable String empCode) throws IOException {
		if (functionality.equalsIgnoreCase("hr")) {
			//ArrayList<String> empCodeList = new ArrayList<String>();
			//empCodeList.add(empCode);
			//ArrayList<String> filePaths = service.getFilePaths4Employee(empCodeList, HashmapConstants.FUNCTIONALITY_HR);
			ArrayList<String> empCodeList = new ArrayList<String>();
			empCodeList.add(empCode);
			ArrayList<String> filePaths = service.getFilePaths4Employee(empCodeList, HashmapConstants.FUNCTIONALITY_HR);
			// logger.info("Filepaths= {}",filePaths);
			for (int i = 0; i < filePaths.size(); i++)
				System.out.println(filePaths.get(i));

			if (filePaths.size() == 0)
				return ResponseEntity.noContent().build();

			System.out.println(filePaths.size());
			String downloadZipFileName = service.zipFiles(filePaths);

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

/*
 * File fileFound=service.searchPath(new File("E:\\"), "1626285.PDF"); String
 * fileFoundName=fileFound.getName();
 */
