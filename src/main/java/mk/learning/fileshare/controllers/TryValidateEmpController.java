package mk.learning.fileshare.controllers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mk.learning.fileshare.Services.SearchFileBasedOnEmp;

@Controller
public class TryValidateEmpController {

	@Autowired
	SearchFileBasedOnEmp service;
	
	private final Logger logger=LoggerFactory.getLogger(TryValidateEmpController.class);

	@RequestMapping(value = "/Validate", method = RequestMethod.GET)
	public String ValidateSuccess() {
		ArrayList<String> emp=service.FindAllEmpForHR("1234");
		logger.info("Emp={}",emp);
		if (emp.isEmpty())
			return "redirect:Validate?state=failed";
		else
			return "redirect:Validate?state=success";

	}
}