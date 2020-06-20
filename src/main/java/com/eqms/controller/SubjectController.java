package com.eqms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eqms.model.Subject;
import com.eqms.model.User;
import com.eqms.service.EmailService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.web.JsonResponse;

@Controller
@RequestMapping("subjects")
public class SubjectController {
	
	/** Static logger */
	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;

	/**
	 * Gets main subjects page.
	 * 
	 * @param map the map
	 * @param model the model
	 * @return logic view name represented by String type
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getSubjectsPage(Map<String, List<Subject>> map, ModelMap model) {
		logger.debug("Received request to show subjects page");
		
		/**
		 * Getting email of current logged user
		 */
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("Address email of current logged user = " + currentUserEmail);
		
		model.put("userSubjectsModel", getTestService().getSubjectsByUser(getUserService().findByEmail(currentUserEmail)));
		model.put("allSubjectsModel", getTestService().getAllSubjects(Order.asc("subjectId"), null));
		
    	return "subjectspage";
	}
	
	/**
	 * Adds new subjects to database and creates HTML code for added subject, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param subjectName the name of subject
	 * @return JSON response which contains HTML code for last added subject
	 */
	@RequestMapping(value = "/doAddSubjectAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doAddSubject(@RequestParam(value="subjectNameModal") String subjectName) {
	
		logger.debug("Start adding new subject to database ...");
		logger.debug("Received request with:");
		logger.debug("\tsubjectName = " + subjectName);
		
		// Get address e-mail of current logged user
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("Address email of current logged user = " + currentUserEmail);
		
		// Get information about current logged user
		User loggedUser = getUserService().findByEmail(currentUserEmail);
		
		// Create new subject
		Subject subject = new Subject();
		subject.setSubjectName(subjectName);
		subject.setCreationDate(new Date());
		subject.setCreatedBy(currentUserEmail);
		
		// Add new subject to database
		getTestService().addSubject(subject, loggedUser.getUserId());
		
		// Get last subject from database
		Subject lastSubject = getTestService().getAllSubjects(Order.desc("subjectId"), 1).get(0);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> addedSubjectRow = new HashMap<String, Object>();
		addedSubjectRow.put("subjectName", lastSubject.getSubjectName());
		addedSubjectRow.put("creationDate", lastSubject.getCreationDate());
		addedSubjectRow.put("createdBy", lastSubject.getCreatedBy());
		addedSubjectRow.put("sendMessage", "<a href=\"#\" class=\"btn btn-primary btn-block btn-sm disabled\" role=\"button\"><span class=\"glyphicon glyphicon-share\" aria-hidden=\"true\"></span> Ask for access</a>");
		addedSubjectRow.put("editSubject", "<button type=\"button\" class=\"btn btn-info btn-block btn-sm\" id=\"editSubjectBtn\" name=\"editSubjectBtn\" data-toggle=\"modal\" data-target=\"#editSubjectModal\" data-subject-name=\"" + lastSubject.getSubjectName() + "\" data-subject-reference=\"" + lastSubject.getSubjectId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
		addedSubjectRow.put("deleteSubject", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteSubjectBtn\" name=\"deleteSubjectBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteSubject\" data-title=\"Delete Subject\" data-message=\"Are you sure you want to delete subject '" + lastSubject.getSubjectName() + "'?\" data-subject-reference=\"" + lastSubject.getSubjectId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
		addedSubjectRow.put("subjectId", lastSubject.getSubjectId());
		
		response.setStatus("SUCCESS");
		response.setResult(addedSubjectRow);
		
		return response;
	}
	
	/**
	 * Updates existing subject from database and creates HTML code for updated subject, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param subjectName the name of subject
	 * @param subjectId the subject identifier
	 * @return JSON response which contains HTML code for updated subject
	 */
	@RequestMapping(value = "/doEditSubjectAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEditSubject(@RequestParam(value="subjectNameModal") String subjectName,
			@RequestParam(value="subjectReference") Integer subjectId) {
	
		logger.debug("Start editing subject from database ...");
		logger.debug("Received request with:");
		logger.debug("\tsubjectName = " + subjectName);
		logger.debug("\tsubjectId = " + subjectId);
		
		// Get edited subject with default information from database
		Subject subject = getTestService().getSubjectBySubjectId(subjectId);
		// Change subject name 
		subject.setSubjectName(subjectName);
		
		// Update subject to database
		getTestService().updateSubject(subject);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> updateSubjectParameters = new HashMap<String, Object>();
		updateSubjectParameters.put("subjectName", subject.getSubjectName());
		updateSubjectParameters.put("editSubject", "<button type=\"button\" class=\"btn btn-info btn-block btn-sm\" id=\"editSubjectBtn\" name=\"editSubjectBtn\" data-toggle=\"modal\" data-target=\"#editSubjectModal\" data-subject-name=\"" + subject.getSubjectName() + "\" data-subject-reference=\"" + subject.getSubjectId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
		updateSubjectParameters.put("deleteSubject", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteSubjectBtn\" name=\"deleteSubjectBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteSubject\" data-title=\"Delete Subject\" data-message=\"Are you sure you want to delete subject '" + subject.getSubjectName() + "'?\" data-subject-reference=\"" + subject.getSubjectId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");

		response.setStatus("SUCCESS");
		response.setResult(updateSubjectParameters);
	
		return response;
	}
	
	/**
	 * Removes subject from database and creates HTML code which updates JSP after removing subject.
	 * 
	 * @param subjectId the subject identifier
	 * @return JSON response which contains HTML code which updates JSP after removing subject
	 */
	@RequestMapping(value = "/doDeleteSubjectAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDeleteSubject(@RequestParam(value="subjectReference") Integer subjectId) {
		
		logger.debug("Start deleting subject from database ...");
		logger.debug("Received request with:");
		logger.debug("\tsubjectId = " + subjectId);
		
		// Delete subject from database
		getTestService().deleteSubject(subjectId);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		//Map<String, Object> deleteSubjectParameters = new HashMap<String, Object>();
	
		response.setStatus("SUCCESS");
	
		return response;
	}
	
	/**
	 * Sends a message requesting access for specific subject.
	 * 
	 * @param recipient the recipient
	 * @param message the message
	 * @param topic the topic
	 * @param subjectId the subject identifier
	 * @return JSON response
	 */
	@RequestMapping(value = "/askForAccessAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doAskForAccess(@RequestParam(value="recipientName") String recipient,
			@RequestParam(value="messageText") String message,
			@RequestParam(value="topic") String topic,
			@RequestParam(value="subjectReference") Integer subjectId) {
		
		logger.debug("Start sending message for access to subject ...");
		logger.debug("Received request with:");
		logger.debug("\trecipient = " + recipient);
		logger.debug("\ttopic = " + topic);
		logger.debug("\tmessage = " + message);
		logger.debug("\tsubjectId = " + subjectId);
	
		// Get address e-mail of current logged user
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("\tAddress email of current logged user = " + currentUserEmail);
		
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();	// hash SHA1
		String hashedToken = shaPasswordEncoder.encodePassword(currentUserEmail + subjectId, null);
		logger.debug("\tHashedToken = " + hashedToken);
		
		/**
		 * Sending email
		 */
		EmailService emailService = new EmailService(currentUserEmail, recipient, topic, message, hashedToken); 
		emailService.sendMessage();
	
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		//Map<String, Object> deleteSubjectParameters = new HashMap<String, Object>();
	
		response.setStatus("SUCCESS");
		
		return response;
	}

	/**
	 * Checks if name of subject is unique.
	 * 
	 * @param subjectName the name of subject
	 * @param model the model
	 * @return ResponseBody which contains success message when subject doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkSubjectName", method = RequestMethod.POST)
	public @ResponseBody String checkSubjectName(@RequestParam(value="subjectName") String subjectName, ModelMap model) {
		
		String response = null;
		
		if(getTestService().checkSubjectName(subjectName) == false) { // subject doesn't exist
			response = "SUCCESS";
		} else { // subject exist
			response = "FAIL";
		}
		
		return response;
	}
	
	/**
	 * Gets UserService.
	 * 
	 * @return the UserService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets UserService.
	 * 
	 * @param userService the UserService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Gets TestService.
	 * 
	 * @return the TestService
	 */
	public TestService getTestService() {
		return testService;
	}

	/**
	 * Sets TestService.
	 * 
	 * @param testService the TestService to set
	 */
	public void setTestService(TestService testService) {
		this.testService = testService;
	}

}
