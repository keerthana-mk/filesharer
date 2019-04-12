package mk.learning.fileshare.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mk.learning.fileshare.Services.AuthenticationService;
import mk.learning.fileshare.Services.UserRegistration;

@Controller
public class UserMVCController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRegistration userRegService;

	@Autowired
	AuthenticationService authService;

	private String getCurrentLoggedInUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// String Password = (((UserDetails) principal).getPassword()).toString();
		if (principal instanceof UserDetails)
			return ((UserDetails) principal).getUsername();
		else
			return principal.toString();

	}

	@RequestMapping(value = { "/resetPassword" }, method = RequestMethod.GET)
	public String resetPasswordGetController() {
		// return "/HTML/resetPassword.html";
		return "/HTML/resetPassword2.html";
	}

	@RequestMapping(value = { "/resetPassword" }, method = RequestMethod.POST)
	public String resetPasswordPostController(HttpServletRequest request) {
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		String newPasswordAgain = request.getParameter("newPasswordAgain");

		if (authService.verifyUserFromDb(getCurrentLoggedInUser(), authService.EncryptPassword(oldPassword))) {
			logger.info("old password verified");
			if (newPassword.equalsIgnoreCase(newPasswordAgain) && userRegService
					.updatePassword(getCurrentLoggedInUser(), authService.EncryptPassword(newPassword))) {
				request.getSession().invalidate();
				return "redirect:/login";
			} else {
				return "redirect:/resetPassword?reset=failed&reason=NewPasswordsMismatch";
			}
		} else {
			return "redirect:/resetPassword?reset=failed&reason=InvalidOldPassword";
		}

	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	
	public String loginGetController() {
		return "/HTML/Login.html";
	}

	@RequestMapping(value = { "/help" }, method = RequestMethod.GET)
	public String HelpController() {
		return "/HTML/Docs.html";

	}

	@RequestMapping(value = { "/welcome", "/" }, method = RequestMethod.GET)
	public String WelcomeGetController(HttpServletRequest request) {

		if (getCurrentLoggedInUser().equalsIgnoreCase("admin") == true)
			return "/HTML/upload2.html";
		else if (userRegService.isFirstTimeLogin(getCurrentLoggedInUser())) {
			// request.getSession().invalidate();
			return "redirect:/resetPassword";
		}
		return "/HTML/welcome.html";
	}
}
