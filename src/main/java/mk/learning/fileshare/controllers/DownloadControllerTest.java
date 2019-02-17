package mk.learning.fileshare.controllers;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mk.learning.fileshare.Services.SearchFileBasedOnEmp;;

@Controller
public class DownloadControllerTest {
	
@Autowired	
SearchFileBasedOnEmp service;
@RequestMapping("/Download")
public @ResponseBody File Download() {
    return service.searchPath(new File("E:\\"), "1626285.pdf");
}
}
/*File fileFound=service.searchPath(new File("E:\\"), "1626285.PDF");
String fileFoundName=fileFound.getName();*/




	
	
	
	


