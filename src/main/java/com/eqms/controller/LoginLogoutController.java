/**
 * 
 */
package com.eqms.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import com.eqms.model.Subject;
import com.eqms.model.User;
import com.eqms.service.EmailService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.validator.PasswordValidator;
import com.eqms.web.JsonResponse;

/**
 * Handles and retrieves the login or denied page depending on the URI template
 */
@Controller
@RequestMapping("/auth")
public class LoginLogoutController {
        
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(@RequestParam(value="error", required=false) boolean error, 
			ModelMap model) {
		logger.debug("Received request to show login page");
		
		if (error == true) {
			// Assign an error message
			model.put("error", "You have entered an invalid email or password or your account is not enabled!");
		} else {
			model.put("error", "");
		}
		
		// This will resolve to /WEB-INF/jsp/loginpage.jsp
		return "loginpage";
	}
	
	@RequestMapping(value  = "/register", method = RequestMethod.GET)
	public String getRegisterPage() {
		logger.debug("Received request to show register page");
		
		return "registerpage";
	}
	
	@RequestMapping(value = "/registerCheck", method = RequestMethod.POST)
	public @ResponseBody String checkEmail(@RequestParam(value="email") String email) {
		
		logger.debug("Method checkEmail in LoginLogoutController");
		logger.debug("Method checkEmail - requested email = " + email);
		logger.debug("Method checkEmail - userDAO.isEmailExists(email) = " + getUserService().isEmailExists(email));
		
		String response = null;
		 
		if(getUserService().isEmailExists(email) == false) { 
			logger.debug("Email is free");
			response = "SUCCESS";
		} else {
			logger.debug("Email is not free");
			response = "FAIL";
		}
		
		return response;
	}
	
	@RequestMapping(value = "/registerCheck2", method = RequestMethod.POST)
	public @ResponseBody JsonResponse checkEmail2(@RequestParam(value="email") String email) {
		
		logger.debug("Method checkEmail2 in LoginLogoutController");
		logger.debug("Method checkEmail2 - requested email = " + email);
		logger.debug("Method checkEmail2 - userDAO.isEmailExists(email) = " + getUserService().isEmailExists(email));
		
		JsonResponse response = new JsonResponse();
		 
		if(getUserService().isEmailExists(email) == false) { 
			logger.debug("Email is free");
			response.setStatus("SUCCESS");
			response.setResult("SUCCESS");
		} else {
			logger.debug("Email is not free");
			response.setStatus("FAIL");
			response.setResult("FAIL");
		}
		
		return response;
	}
	
	@RequestMapping(value = "checkPasswordValidation", method = RequestMethod.POST)
	public @ResponseBody JsonResponse checkPassword(@RequestParam(value="password") String password/*, @RequestParam(value="confirm_password") String confirm_password*/) {
		
		logger.debug("Received password: " + password);
		
		JsonResponse response = new JsonResponse();
		
		PasswordValidator passwordValidator = new PasswordValidator();
		if(passwordValidator.validatePassword(password)) {
			response.setStatus("SUCCESS");
			response.setResult(PasswordValidator.PASSWORD_OK);
		} else {
			response.setStatus("FAIL");
			response.setResult(PasswordValidator.PASSWORD_FAIL);
		}
		
		return response;
	}
	
	/**
	 * Using @RequestParam we have to bind our own variable with parameter from submitted form
	 */
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String createNewUser(@ModelAttribute User user,  /*@RequestParam(value="e_mail") String email,*/
			@RequestParam(value="confirm_password") String confirm_password,
			BindingResult result,
			SessionStatus status,
			Errors errors,
			ModelMap model) {
		
		logger.debug("Firstname = " + user.getFirstname());
		logger.debug("Lastname = " + user.getLastname());
		logger.debug("Password = " + user.getPassword());
		logger.debug("User_id = " + user.getUserId());
		logger.debug("Email = " + user.getEmail());
		logger.debug("UserRole = " +user.getUserRoles());
		logger.debug("Subjectses = " + user.getSubjectses());
		
		if(!user.getPassword().equals(confirm_password)) {
			ObjectError error = new ObjectError("error_password", "Passwords are different!");
			result.addError(error);
			
			model.put("error", "Passwords are different!");
		}
		
		if(getUserService().isEmailExists(user.getEmail()) == true) {
			ObjectError error = new ObjectError("error_email", "An account already exists for this email.");
			result.addError(error);
			
			model.put("error", "An account already exists for this email.");
		}
		
		PasswordValidator passwordValidator = new PasswordValidator();
		if(!passwordValidator.validatePassword(user.getPassword())) {
			ObjectError error = new ObjectError("error_password_validation", "The password does not meet the following requirements:"+PasswordValidator.PASSWORD_FAIL);
			result.addError(error);
			
			model.put("error", "The password does not meet the following requirements:"+PasswordValidator.PASSWORD_FAIL);
		}
				
		
		if(!result.hasErrors()) {
			User registerUser;
			
			ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();	// hash SHA1
			String hashedPassword = shaPasswordEncoder.encodePassword(user.getPassword(), null);
			logger.debug("HashedPassword = " + hashedPassword);
			
			registerUser = user;
			registerUser.setUserRoles(getUserService().getUserRolebyNazwa("ROLE_USER"));
			registerUser.setEnabled(false);
			//registerUser.setEMail(email);
			registerUser.setPassword(hashedPassword);
			
			logger.debug("REGISTER USER before adding - email -> " + registerUser.getEmail());
			
			getUserService().add(registerUser);
			
			/**
			 * Create verification token for new user
			 */
			User newUser = getUserService().findByEmail(user.getEmail());
			getUserService().addVerificationToken(newUser);
			
			logger.debug("Verification token was successful created for user " + newUser.getEmail().toUpperCase());
			
			/**
			 * Send email to new user with verification token for activate account
			 */
			EmailService emailService = new EmailService(newUser.getEmail(), "Activate your account", "To activate your account, please open the following link", getUserService().getVerificationTokenbyUser(newUser));
			emailService.sendMessage();
			
			logger.debug("Email was successful sent");
			
			model.put("info", "Registration was successful :)");
			
			return "loginpage";
		} else {
			return "registerpage";
		}
	}
	
	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, ModelMap model, @RequestParam("token") String token) {
		
		User user = getUserService().getUserByVerificationToken(token);
		user.setEnabled(true);
		
		getUserService().updateEnabledVariableForUser(user);
		
		model.put("info", "Your account has been activated. You can now sign up.");
		return "loginpage";
	}
	
	@RequestMapping(value = "/askForAccessConfirm", method = RequestMethod.GET) 
	public String askForAccessConfirm(WebRequest request, ModelMap model, 
			@RequestParam("initial") String initialAddressEmail,
			@RequestParam("token") String hashedToken) {
		
		logger.debug("\t\tReceived initial = " + initialAddressEmail);
		logger.debug("\t\tReceived token = " + hashedToken);
		
		User initialUser = getUserService().findByEmail(initialAddressEmail);
		
		List<Subject> subjects = getTestService().getAllSubjects(Order.desc("subjectId"), null);
		
		for(Subject subject : subjects) {
			
			ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();	// hash SHA1
			String token = shaPasswordEncoder.encodePassword(initialAddressEmail + subject.getSubjectId(), null);
			logger.debug("\t\tHashedToken = " + token);
			
			if(token.equals(hashedToken)) {
				
				logger.debug("\t\t\tFound subjectId from request = " + subject.getSubjectId());
				logger.debug("\t\t\tFound subject from request = " + subject.getSubjectName());
				
				// Adding reference to user to subject
				getTestService().addReferenceUserToSubject(initialUser.getUserId(), subject.getSubjectId());
				
				model.put("info", "Access was granted successfully.");
				break;
			} else {
				model.put("info", "Access wasn't granted successfully.");
			}
		}
		
		return "loginpage";
	}
	
	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a regular user
	 * tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
 	public String getDeniedPage() {
		logger.debug("Received request to show denied page");
		
		// This will resolve to /WEB-INF/jsp/deniedpage.jsp
		return "deniedpage";
	}
	
	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}