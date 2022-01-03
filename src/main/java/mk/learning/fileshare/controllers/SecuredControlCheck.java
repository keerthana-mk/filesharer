package mk.learning.fileshare.controllers;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredControlCheck {

	@RequestMapping(value= {"/Check"}, method=RequestMethod.GET)
	public String ReturnsIfHTTPS()
	{
		 System.out.println("Inside secured()");
	        return "Hello user !!! : " + new Date();		
	}
}
