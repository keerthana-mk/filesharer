package mk.learning.fileshare.controllers;

import java.io.File;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mk.learning.fileshare.Services.AuthenticationService;
import mk.learning.fileshare.Services.FileServices;
import mk.learning.fileshare.Services.UserRegistration;

@Controller
public class AdminController {

	@Value("${uploadBaseDir}")
	String uploadBaseDir;

	@Value("${hrDataFileName}")
	String hrDataFileName;
	/*
	 * @Value("${pathDelimiter}") String pathDelimiter;
	 */

	@Autowired
	FileServices fileService;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	AuthenticationService authService;

	@Autowired
	UserRegistration user;

	private final Logger logger = LoggerFactory.getLogger(AdminController.class);
	String pathDelimiter = File.separator;

	private String getCurrentLoggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//String Password = (((UserDetails) principal).getPassword()).toString();
		if (principal instanceof UserDetails)
			return ((UserDetails) principal).getUsername();
		else
			return principal.toString();

	}


	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public String uploadGetController() {
		if (getCurrentLoggedInUser().equalsIgnoreCase("admin") == false)
			return "HTML/notAuthorizedError.html";
		return "HTML/upload2.html";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadPostController(@RequestParam MultipartFile inputFile) {
		String targetPath = uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename();
		String defaultTargetPath = uploadBaseDir + pathDelimiter + hrDataFileName;
		logger.info("saving file at {}", targetPath);
		if (fileService.saveMultipartFile(inputFile, targetPath)
				&& fileService.saveMultipartFile(inputFile, defaultTargetPath)) {
			logger.info("fileSavedSuccesfully");
		//	logger.info("Input String={}{}{}", uploadBaseDir, pathDelimiter, inputFile.getOriginalFilename());
			if (fileService.setMapData(uploadBaseDir + pathDelimiter + inputFile.getOriginalFilename()))
				return "redirect:uploadFile?upload=success";
			else
				return "redirect:uploadFile?upload=failed";
		} else {
			return "redirect:uploadFile?upload=failed";
		}

	}
	
	@RequestMapping(value = { "/register" }, method = RequestMethod.GET)
	public String RegisterGetController() {
		if (getCurrentLoggedInUser().equalsIgnoreCase("admin") == true)
			return "/HTML/CreateUser.html";
		return "/HTML/welcome.html";
	}
	
	@RequestMapping(value="/deleteUser/{username}",method = RequestMethod.GET)
	public ResponseEntity<String> deleteUserGetController(@PathVariable String username){
		if(!getCurrentLoggedInUser().equalsIgnoreCase("admin"))
			return new ResponseEntity<String>("not authorized",HttpStatus.UNAUTHORIZED);
		if(user.deleteUser(username))
			return new ResponseEntity<String>("deleted",HttpStatus.OK);
		else
			return new ResponseEntity<String>("error while deleting user",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public String RegisterPostController(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
			if(username==null || password==null)
			return "redirect:/register?register=failed";
		else {
			boolean created = user.CreateNewUser(username, password, "HR");
			if(!created)
				return "redirect:/register?register=failed";
			else
				return "redirect:/register?register=success";
		}
	}
	

	/*@RequestMapping(value = "principalchangepassword" , method = RequestMethod.POST)
	public @ResponseBody String principalchangepassword(Model uiModel, HttpServletRequest httpServletRequest){
	    Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Principal me = Principal.findPrincipal(principal.getId());
	   me.setPassword(httpServletRequest.getParameter("password1"));
	    StandardStringDigester digester = new StandardStringDigester();
	    digester.setAlgorithm("SHA-256");   // optionally set the algorithm
	    digester.setStringOutputType("hexadecimal");
	    digester.setSaltSizeBytes(0);
	    digester.setIterations(1);
	    String digest = digester.digest(me.getPassword());
	    me.setPassword(digest.toLowerCase());
	    me.merge();
	    return "Password Updated successfully";
	}
	*/
}
