package mk.learning.fileshare.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mk.learning.fileshare.Services.ValidateEmpAgainstHR;

@Controller
public class TryValidateEmpController {

	@Autowired
	ValidateEmpAgainstHR service;

	@RequestMapping(value = "/Validate", method = RequestMethod.GET)
	public String ValidateSuccess() {
		ArrayList<String> emp=service.FindAllEmpForHR();
		if (emp.isEmpty())
			return "redirect:Validate?state=failed";
		else
			return "redirect:Validate?state=success";

	}
}