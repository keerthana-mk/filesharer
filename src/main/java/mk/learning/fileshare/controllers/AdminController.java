package mk.learning.fileshare.controllers;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mk.learning.fileshare.Services.FileServices;
import mk.learning.fileshare.Services.ReadExcel;

@Controller
public class AdminController {
		
	@Value("${uploadBaseDir}")
	String uploadBaseDir;

	/*@Value("${pathDelimiter}")
	String pathDelimiter;*/

	@Autowired
	ReadExcel excelRead;

	@Autowired
	FileServices fileService;

	private final Logger logger = LoggerFactory.getLogger(AdminController.class);
	String pathDelimiter=File.separator;
	

	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public String uploadGetController() {
		return "upload.html";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadPostController(@RequestParam MultipartFile inputFile) {
		String targetPath = uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename();
		logger.info("saving file at {}",targetPath);
		if (fileService.saveMultipartFile(inputFile, targetPath)) {
			logger.info("fileSavedSuccesfully");
			if (excelRead.setMapData(uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename()))
				return "redirect:uploadFile?upload=success";
			else
				return "redirect:uploadFile?upload=failed";
		} else {
			return "redirect:uploadFile?upload=failed";
		}

	}
		
}
