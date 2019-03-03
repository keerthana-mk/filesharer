package mk.learning.fileshare.controllers;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mk.learning.fileshare.Services.FileServices;

@Controller
public class AdminController {

	@Value("${uploadBaseDir}")
	String uploadBaseDir;

	/*
	 * @Value("${pathDelimiter}") String pathDelimiter;
	 */

	@Autowired
	FileServices fileService;

	private final Logger logger = LoggerFactory.getLogger(AdminController.class);
	String pathDelimiter = File.separator;
	
	private String getCurrentLoggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails)
			return ((UserDetails) principal).getUsername();
		else
			return principal.toString();
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public String uploadGetController() {
		if(getCurrentLoggedInUser().equalsIgnoreCase("admin") == false)
			return "HTML/notAuthorizedError.html";
		return "HTML/upload2.html";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadPostController(@RequestParam MultipartFile inputFile) {
		String targetPath = uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename();
		logger.info("saving file at {}", targetPath);
		if (fileService.saveMultipartFile(inputFile, targetPath)) {
			logger.info("fileSavedSuccesfully");
			logger.info("Input String={}{}{}", uploadBaseDir, pathDelimiter, inputFile.getOriginalFilename());
			if (fileService.setMapData(uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename()))
				return "redirect:uploadFile?upload=success";
			else
				return "redirect:uploadFile?upload=failed";
		} else {
			return "redirect:uploadFile?upload=failed";
		}

	}

	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String loginGetController() {

		return "/HTML/Login.html";
	}

	@RequestMapping(value = {"/welcome","/"}, method = RequestMethod.GET)
	public String WelcomeGetController() {
		if(getCurrentLoggedInUser().equalsIgnoreCase("admin") == true)
			return "HTML/notAuthorizedError.html";
		return "/HTML/welcome.html";
	}
	
}
