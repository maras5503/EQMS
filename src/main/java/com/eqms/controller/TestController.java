package com.eqms.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.eqms.model.*;
import com.eqms.service.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eqms.web.JsonResponse;

@Controller
@RequestMapping("tests")
public class TestController {

	/** Static logger */
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;

	@Autowired
	private StudentGroupsService studentGroupsService;


    @Autowired
    private StudentService studentService;

	public static String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	/**
	 * Gets main test page.
	 * 
	 * @param model the model
	 * @return logic view name represented by String type
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getTestPage(ModelMap model) {
		
		/**
		 * Getting email of current logged user
		 */
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("Address email of current logged user = " + currentUserEmail);
		
		/**
		 * Getting user from database by email
		 */
		User user = getUserService().findByEmail(currentUserEmail);
		List<GroupsOfStudents> g=getStudentGroupsService().getAllStudentGroups(Order.asc("studentgroupId"), null);
		model.put("userSubjects", getTestService().getSubjectsByUser(user));

		
		return "testspage";
	}
	
	/**
	 * Checks if name of test is unique for specific subject.
	 * 
	 * @param testName the name of test
	 * @param subjectId the subject identifier
	 * @param testId the test identifier
	 * @param model the model
	 * @return ResponseBody which contains success message when test doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkTestName", method = RequestMethod.POST)
	public @ResponseBody String checkTestName(@RequestParam(value="testName") String testName,
			@RequestParam(value="subjectId") Integer subjectId, 
			@RequestParam(value="testId", required=false) Integer testId,
			ModelMap model) {
		
		logger.debug("\tcheckTestName method:");
		logger.debug("\t\ttestName = " + testName);
		logger.debug("\t\tsubjectId = " + subjectId);
		logger.debug("\t\ttestId = " + testId);
		
		String response = null;
		
		if(testId != null && getTestService().getTestByTestId(testId).getTestName().toString().toLowerCase().equals(testName.toLowerCase())) {
			response = "SUCCESS";
		}
		
		if(response == null) {
			if(getTestService().checkTestName(testName, subjectId) == false) { // test doesn't exist
				response = "SUCCESS";
			} else { // test exist
				response = "FAIL";
			}
		}
		
		logger.debug("\t\tresponse = " + response);
		return response;
	}
	
	/**
	 * Checks if name of group is unique for specific test.
	 * 
	 * @param groupName the name of group
	 * @param testId the test identifier
	 * @param groupId the group identifier
	 * @param model the model
	 * @return ResponseBody which contains success message when group doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkGroupName", method = RequestMethod.POST)
	public @ResponseBody String checkGroupName(@RequestParam(value="groupName") String groupName, 
			@RequestParam(value="testId") Integer testId,
			@RequestParam(value="groupId", required=false) Integer groupId,
			ModelMap model) {
		
		logger.debug("\tcheckGroupName method:");
		logger.debug("\t\tgroupName = " + groupName);
		logger.debug("\t\tgroupId = " + groupId);
		
		String response = null;
		
		if(groupId != null && getTestService().getGroupByGroupId(groupId).getGroupName().toString().toLowerCase().equals(groupName.toLowerCase())) {
			response = "SUCCESS";
		}
		
		if(response == null) {
			if(getTestService().checkGroupName(groupName, testId) == false) { // group doesn't exist
				response = "SUCCESS";
			} else { // group exist
				response = "FAIL";
			}
		}
		
		logger.debug("\t\tresponse = " + response);
		return response;
	}
	
	/**
	 * Checks if content of question is unique for specific test.
	 * 
	 * @param contentOfQuestion the content of question
	 * @param testId the test identifier
	 * @param questionId the question identifier
	 * @param model the model
	 * @return ResponseBody which contains success message when content of question doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkContentOfQuestion", method = RequestMethod.POST)
	public @ResponseBody String checkContentOfQuestion(@RequestParam(value="contentOfQuestion") String contentOfQuestion, 
			@RequestParam(value="testId") Integer testId,
			@RequestParam(value="questionId", required=false) Integer questionId, 
			ModelMap model) {
	
		logger.debug("\tcheckContentOfQuestion method:");
		logger.debug("\t\tReceived contentOfQuestion = " + contentOfQuestion);
		logger.debug("\t\tReceived testId = " + testId);
		logger.debug("\t\tReceived questionId = " + questionId);
		
		String response = null;
		
		if(questionId != null && getTestService().getQuestionByQuestionId(questionId).getContentOfQuestion().toString().toLowerCase().equals(contentOfQuestion.toLowerCase())) {
			response = "SUCCESS";
		}
		
		if(response == null) {
			if(getTestService().checkContentOfQuestion(contentOfQuestion, testId) == false) { // contentOfQuestion doesn't exists
				response = "SUCCESS";
			} else {
				response = "FAIL";
			}
		}
		
		logger.debug("\t\tresponse = " + response);
		return response;
	}
	
	/**
	 * Checks if content of answer is unique for specific question.
	 * 
	 * @param contentOfAnswer the content of answer
	 * @param questionId the question identifier
	 * @param answerId the answer identifier
	 * @param model the model
	 * @return ResponseBody which contains success message when content of answer doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkContentOfAnswer", method = RequestMethod.POST)
	public @ResponseBody String checkContentOfAnswer(@RequestParam(value="contentOfAnswer") String contentOfAnswer, 
			@RequestParam(value="questionId") Integer questionId, 
			@RequestParam(value="answerId", required=false) Integer answerId, 
			ModelMap model) {
	
		logger.debug("\tcheckContentOfAnswer method:");
		logger.debug("\t\tReceived contentOfAnswer = " + contentOfAnswer);
		logger.debug("\t\tReceived questionId = " + questionId);
		logger.debug("\t\tReceived answerId = " + answerId);
		
		String response = null;

		if(answerId != null && getTestService().getAnswerByAnswerId(answerId).getContentOfAnswer().toString().toLowerCase().equals(contentOfAnswer.toLowerCase())) {
			response = "SUCCESS";
		}
		
		if(response == null) {
			if(getTestService().checkContentOfAnswer(contentOfAnswer, questionId) == false) { // contentOfAnswer doesn't exists
				response = "SUCCESS";
			} else {
				response = "FAIL";
			}
		}
		
		logger.debug("\t\tresponse = " + response);
		return response;
	}
	
	/**
	 * Checks if name of picture is unique.
	 * 
	 * @param pictureName the name of picture
	 * @param questionId the question identifier
	 * @param answerId the answer identifier
	 * @param model the model
	 * @return ResponseBody which contains success message when name of picture doesn't exists, or fail message when exists
	 */
	@RequestMapping(value = "/checkPictureName", method = RequestMethod.POST)
	public @ResponseBody String checkPictureName(@RequestParam(value="pictureName") String pictureName, 
			@RequestParam(value="questionId", required=false) Integer questionId, 
			@RequestParam(value="answerId", required=false) Integer answerId, 
			ModelMap model) {
		
		String response = null;
		
		if(getTestService().checkPictureName(pictureName) == false) {
			response = "SUCCESS";
		} else {
			response = "FAIL";
		}
	
		logger.debug("\t\tresponse = " + response);
		return response;
	}
	
	/**
	 * Adds new group to database and creates HTML code for added group, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param testId the test identifier
	 * @param groupName the name of group
	 * @return JSON response which contains HTML code for last added group
	 */
	@RequestMapping(value = "/doAddGroupAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doAddGroupAjax(@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="groupNameModal") String groupName) {
		
		logger.debug("Start adding new group to database ...");
		logger.debug("\tdoAddGroupAjax -> groupName = " + groupName);
		logger.debug("\tdoAddGroupAjax -> testId = " + testId);
		
		// Prepare new group to add to database
		GroupOfQuestions newGroup = new GroupOfQuestions();
		newGroup.setGroupName(groupName);
		newGroup.setNumberOfQuestions(0);
		newGroup.setTests(getTestService().getTestByTestId(testId));	// New group will be added to this test
		
		// Add new group to database
		getTestService().addGroup(newGroup);
	
		logger.debug("End adding new group to database ...");
		
		Test updatedTest = getTestService().getTestByTestId(testId);
		
		// Update modificationDate and modifiedBy parameters for current test
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();

		updatedTest.setModificationDate(new Date());
		updatedTest.setModifiedBy(currentUserEmail);
		getTestService().updateTest(updatedTest);
		
		// Get last group from database
		GroupOfQuestions lastGroup = getTestService().getAllGroups(Order.desc("groupId"), 1).get(0);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> addedGroup = new HashMap<String, Object>();
		
		addedGroup.put("groupDiv", "<div class=\"col-sm-12 well\" id=\"group_" + lastGroup.getGroupId() + "\" style=\"margin-bottom: 20px;\">"
							+ "<div class=\"col-sm-12\">"
								+ "<label for=\"groupName\" class=\"col-md-2 control-label\" style=\"padding-top: 7px; margin-bottom: 0px; text-align: right;\">Group name:</label>"
								+ "<label id=\"groupName\" class=\"col-md-5 control-label\" style=\"padding-top: 7px; margin-bottom: 0px; text-align: left;\">" + lastGroup.getGroupName() + "</label>"
								+ "<div class=\"col-md-5\" style=\"text-align: right;\">"
									+ "<button type=\"button\" class=\"btn btn-primary btn-sm\" id=\"collapseGroup\" name=\"collapseGroup\" data-reference=\"" + lastGroup.getGroupId() + "\" data-toggle=\"collapse\" href=\"#collapseGroup_" + lastGroup.getGroupId() + "\" aria-expanded=\"false\" aria-controls=\"collapseGroup_" + lastGroup.getGroupId() + "\" style=\"margin-right: 10px;\"><span class=\"glyphicon glyphicon-triangle-bottom\" aria-hidden=\"true\"></span> Show questions</button>"
									+ "<button type=\"button\" class=\"btn btn-success btn-sm\" id=\"addQuestionBtn\" name=\"addQuestionBtn\" data-toggle=\"modal\" data-target=\"#addQuestionModal\" data-group-reference=\"" + lastGroup.getGroupId() + "\" style=\"margin-right: 10px;\"><span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span> Add question</button>"
									+ "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editGroupBtn\" name=\"editGroupBtn\" data-toggle=\"modal\" data-target=\"#editGroupModal\" data-group-name=\"" + lastGroup.getGroupName() + "\" data-test-reference=\"" + testId + "\" data-group-reference=\"" + lastGroup.getGroupId() + "\" style=\"margin-right: 10px;\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>"
									+ "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteGroupBtn\" name=\"deleteGroupBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteGroup\" data-title=\"Delete Group\" data-message=\"Are you sure you want to delete group '" + lastGroup.getGroupName() + "'?\" data-group-reference=\"" + lastGroup.getGroupId() + "\" data-test-reference=\"" + testId + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
								+ "</div>"
							+ "</div>"
							+ "<div class=\"col-sm-12\">"
								+ "<label for=\"numberOfQuestions\" class=\"col-md-2 control-label\" style=\"padding-top: 7px; margin-bottom: 0px; text-align: right;\">Num. of questions:</label>"
								+ "<label id=\"numberOfQuestions\" class=\"col-md-1 control-label\" style=\"padding-top: 7px; margin-bottom: 0px; text-align: left;\">" + lastGroup.getNumberOfQuestions() + "</label>"
							+ "</div>"
							+ "<div class=\"collapse\" id=\"collapseGroup_" + lastGroup.getGroupId() + "\" data-reference=\"" + lastGroup.getGroupId() + "\">"
							+ "</div>"
						+ "</div>");
		addedGroup.put("numberOfGroupsForTest", updatedTest.getNumberOfGroups());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		addedGroup.put("modificationDate", sdf.format(updatedTest.getModificationDate()));
		addedGroup.put("modifiedBy", currentUserEmail);
		
		response.setResult(addedGroup);	// groupDiv
		response.setStatus("SUCCESS");
		
		return response;
	}
	
	/**
	 * Adds new question to database and creates HTML code for added question, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param groupId the group identifier
	 * @param testId the test identifier
	 * @param contentOfQuestion the content of question
	 * @param inputImageForQuestion the picture for question
	 * @param request the HttpServletRequest
	 * @return JSON response which contains HTML code for last added question
	 */
	@RequestMapping(value = "/doAddQuestionAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doAddQuestion(@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="contentOfQuestionModal") String contentOfQuestion, 
			@RequestParam(value="inputImageForQuestionModal", required=false) MultipartFile inputImageForQuestion,
			HttpServletRequest request) {
	
		logger.debug("Start adding new question to database ...");
		logger.debug("Received request with:");
		logger.debug("\tcontentOfQuestion = " + contentOfQuestion);
		logger.debug("\tgroupId = " + groupId);
		logger.debug("\ttestId = " + testId);
		
		if (inputImageForQuestion != null) {
			Picture picture = new Picture();
			picture.setPictureName(inputImageForQuestion.getOriginalFilename());
			picture.setPictureType(FilenameUtils.getExtension(inputImageForQuestion.getOriginalFilename()));
			try {
				picture.setPicture(inputImageForQuestion.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.debug("\t\tCurrent question has also image:"); 
			logger.debug("\t\toriginalFilename = " + inputImageForQuestion.getOriginalFilename());
			logger.debug("\t\tsize (in bytes) = " + inputImageForQuestion.getSize());
			
			// Add new picture to database
			getTestService().addPicture(picture);
		}
		
		// Add new question to database
		Question question = new Question();
		question.setContentOfQuestion(contentOfQuestion);
		question.setNumberOfAnswers(0);
		question.setNumberOfCorrectAnswers(0);
		if (inputImageForQuestion != null) {
			question.setPictures(getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0));
		}
		getTestService().addQuestion(question, groupId);
		
		// Update modificationDate and modifiedBy parameters for current test
		Test test = getTestService().getTestByTestId(testId);
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
		
		// Get last added question from database
		Question lastQuestion = getTestService().getAllQuestions(Order.desc("questionId"), 1).get(0);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> addedQuestionRow = new HashMap<String, Object>();
		addedQuestionRow.put("contentOfQuestion", lastQuestion.getContentOfQuestion());
		addedQuestionRow.put("numberOfAnswers", lastQuestion.getNumberOfAnswers());
		addedQuestionRow.put("numberOfCorrectAnswers", lastQuestion.getNumberOfCorrectAnswers());
		
		if(lastQuestion.getPictures() != null) {
			addedQuestionRow.put("picture", "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + lastQuestion.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\" width=\"100px\" height=\"100px\"/>");
			addedQuestionRow.put("editQuestion", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + lastQuestion.getContentOfQuestion() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + lastQuestion.getPictures().getPictureId() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + lastQuestion.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>");
		} else {
			addedQuestionRow.put("picture", "-");
			addedQuestionRow.put("editQuestion", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + lastQuestion.getContentOfQuestion() + "\" data-picture-src=\"\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + lastQuestion.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>");
		}
		
		addedQuestionRow.put("addAnswer", "<button type=\"button\" class=\"btn btn-success btn-sm\" id=\"addAnswerBtn\" name=\"addAnswerBtn\" data-toggle=\"modal\" data-target=\"#addAnswerModal\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + lastQuestion.getQuestionId() + "\"><span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span> Add answer</button>");
		addedQuestionRow.put("deleteQuestion", "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteQuestionBtn\" name=\"deleteQuestionBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteQuestion\" data-title=\"Delete Question\" data-message=\"Are you sure you want to delete question '" + lastQuestion.getContentOfQuestion() + "'?\" data-test-reference=\"" + testId + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + lastQuestion.getQuestionId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
		addedQuestionRow.put("detailsControl", "");
		addedQuestionRow.put("datakey", lastQuestion.getQuestionId());
		addedQuestionRow.put("numberOfQuestionsForGroup", getTestService().getGroupByGroupId(groupId).getNumberOfQuestions());
		addedQuestionRow.put("numberOfQuestionsForTest", test.getNumberOfQuestions());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		addedQuestionRow.put("modificationDate", sdf.format(test.getModificationDate()));
		addedQuestionRow.put("modifiedBy", currentUserEmail);
		
		response.setStatus("status");
		response.setResult(addedQuestionRow);
		
		return response;
	}
	
	/**
	 * Adds new answer to database and creates HTML code for added answer, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param groupId the group identifier
	 * @param questionId the question identifier
	 * @param contentOfAnswer content of answer
	 * @param whetherCorrect a variable that indicates whether the answer is correct
	 * @param inputImageForAnswer the picture for answer
	 * @param request the HttpServletRequest
	 * @return JSON response which contains HTML code for last added answer
	 */
	@RequestMapping(value = "/doAddAnswerAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doAddAnswer(@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="questionReference") Integer questionId,
			@RequestParam(value="contentOfAnswerModal") String contentOfAnswer,
			@RequestParam(value="whetherCorrect") Boolean whetherCorrect,
			@RequestParam(value="inputImageForAnswerModal", required=false) MultipartFile inputImageForAnswer,
			HttpServletRequest request) {
			
		logger.debug("Start adding new answer to database ...");
		logger.debug("Received request with:");
		logger.debug("\tcontentOfAnswer = " + contentOfAnswer);
		logger.debug("\twhetherCorrect = " + whetherCorrect);
		logger.debug("\tquestionId = " + questionId);
		logger.debug("\tgroupId = " + groupId);
		
		if (inputImageForAnswer != null) {
			Picture picture = new Picture();
			picture.setPictureName(inputImageForAnswer.getOriginalFilename());
			picture.setPictureType(FilenameUtils.getExtension(inputImageForAnswer.getOriginalFilename()));
			try {
				picture.setPicture(inputImageForAnswer.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.debug("\t\tCurrent answer has also image:"); 
			logger.debug("\t\toriginalFilename = " + inputImageForAnswer.getOriginalFilename());
			logger.debug("\t\tsize (in bytes) = " + inputImageForAnswer.getSize());
			
			// Add new picture to database
			getTestService().addPicture(picture);
		}
		
		// Add new answer to database
		Answer answer = new Answer();
		answer.setContentOfAnswer(contentOfAnswer);
		answer.setWhetherCorrect(whetherCorrect);
		if (inputImageForAnswer != null) {
			answer.setPictures(getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0));
		}
		getTestService().addAnswer(answer, questionId);

		// Update modificationDate and modifiedBy parameters for current test
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		Test test = getTestService().getTestByTestId(group.getTests().getTestId());
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
		
		// Get last added answer from database
		Answer lastAnswer = getTestService().getAllAnswers(Order.desc("answerId"), 1).get(0);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> addedAnswerRow = new HashMap<String, Object>();
		addedAnswerRow.put("contentOfAnswer", lastAnswer.getContentOfAnswer());
		addedAnswerRow.put("whetherCorrect", lastAnswer.isWhetherCorrect());
		
		if(lastAnswer.getPictures() != null) {
			addedAnswerRow.put("picture", "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + lastAnswer.getPictures().getPictureId() + "\" alt=\"answerImage\" name=\"answerImage\" id=\"answerImage\" width=\"100px\" height=\"100px\"/>");
			addedAnswerRow.put("editAnswer", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + lastAnswer.getContentOfAnswer() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + lastAnswer.getPictures().getPictureId() + "\" data-whether-correct=\"" + lastAnswer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + lastAnswer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>");
		} else {
			addedAnswerRow.put("picture", "-");
			addedAnswerRow.put("editAnswer", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + lastAnswer.getContentOfAnswer() + "\" data-picture-src=\"\" data-whether-correct=\"" + lastAnswer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + lastAnswer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>");
		}
		
		addedAnswerRow.put("deleteAnswer", "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteAnswerBtn\" name=\"deleteAnswerBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteAnswer\" data-title=\"Delete Answer\" data-message=\"Are you sure you want to delete answer '" + lastAnswer.getContentOfAnswer() + "'?\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + lastAnswer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
		addedAnswerRow.put("answerId", "answer_" + lastAnswer.getAnswerId());
		
		Question question = getTestService().getQuestionByQuestionId(questionId);
		addedAnswerRow.put("numberOfAnswers", question.getNumberOfAnswers());
		addedAnswerRow.put("numberOfCorrectAnswers", question.getNumberOfCorrectAnswers());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		addedAnswerRow.put("modificationDate", sdf.format(test.getModificationDate()));
		addedAnswerRow.put("modifiedBy", currentUserEmail);
		
		response.setStatus("status");
		response.setResult(addedAnswerRow);
		
		return response;
	}
	
	/**
	 * Updates existing test from database and creates HTML code for updated test, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param testId the test identifier
	 * @param setId the set identifier
	 * @param testName the name of test
	 * @param timeForTest the time for test
	 * @param mark2 the mark 2
	 * @param mark3 the mark 3
	 * @param mark3_5 the mark 3+
	 * @param mark4 the mark 4
	 * @param mark4_5 the mark 4+
	 * @param mark5 the mark 5
	 * @return JSON response which contains HTML code for updated test
	 */
	@RequestMapping(value = "/doEditTestAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEditTest(@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="setReference") Integer setId,
			@RequestParam(value="testNameModal") String testName,
			@RequestParam(value="timeForTestModal") Integer timeForTest,
			@RequestParam(value="mark2Modal") Integer mark2,
			@RequestParam(value="mark3Modal") Integer mark3,
			@RequestParam(value="mark3_5Modal") Integer mark3_5,
			@RequestParam(value="mark4Modal") Integer mark4,
			@RequestParam(value="mark4_5Modal") Integer mark4_5,
			@RequestParam(value="mark5Modal") Integer mark5) {
	
		logger.debug("Start editing test with set from database ...");
		logger.debug("Received request with:");
		logger.debug("\ttestName = " + testName);
		logger.debug("\ttimeForTest = " + timeForTest);
		logger.debug("\tmark2 = " + mark2);
		logger.debug("\tmark3 = " + mark3);
		logger.debug("\tmark3+ = " + mark3_5);
		logger.debug("\tmark4 = " + mark4);
		logger.debug("\tmark4+ = " + mark4_5);
		logger.debug("\tmark5 = " + mark5);
		logger.debug("\ttestId = " + testId);
		logger.debug("\tsetId = " + setId);
		
		Test test = getTestService().getTestByTestId(testId);
		test.setTestName(testName);
		test.setTimeForTest(timeForTest);
		
		/**
		 * Getting email of current logged user
		 */
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("Address email of current logged user = " + currentUserEmail);
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		
		SetOfRating set = getTestService().getSetOfRatingBySetId(setId);
		set.setMark2(mark2);
		set.setMark3(mark3);
		set.setMark3_5(mark3_5);
		set.setMark4(mark4);
		set.setMark4_5(mark4_5);
		set.setMark5(mark5);
		
		// Edit test and set from database
		getTestService().updateTest(test);
		getTestService().updateSetOfRating(set);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> updateTestParameters = new HashMap<String, Object>();
		updateTestParameters.put("testName", testName);
		updateTestParameters.put("timeForTest", timeForTest + " min");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		updateTestParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		updateTestParameters.put("modifiedBy", currentUserEmail);
		updateTestParameters.put("mark2", mark2);
		updateTestParameters.put("mark3", mark3);
		updateTestParameters.put("mark3_5", mark3_5);
		updateTestParameters.put("mark4", mark4);
		updateTestParameters.put("mark4_5", mark4_5);
		updateTestParameters.put("mark5", mark5);
		
		response.setStatus("SUCCESS");
		response.setResult(updateTestParameters);
		
		return response;
	}
	
	/**
	 * Updates existing group from database and creates HTML code for updated group, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param testId the test identifier
	 * @param groupId the group identifier
	 * @param groupName the name of group
	 * @return JSON response which contains HTML code for updated group
	 */
	@RequestMapping(value = "/doEditGroupAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEditGroup(@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="groupNameModal") String groupName) {
	
		logger.debug("Start editing group from database ...");
		logger.debug("Received request with:");
		logger.debug("\tgroupName = " + groupName);
		logger.debug("\ttestId = " + testId);
		logger.debug("\tgroupId = " + groupId);;
		
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		group.setGroupName(groupName);
		
		// Edit group from database
		getTestService().updateGroup(group);
		
		// Update modificationDate and modifiedBy parameters for current test
		Test test = getTestService().getTestByTestId(testId);
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
				
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> updateGroupParameters = new HashMap<String, Object>();
		updateGroupParameters.put("groupName", group.getGroupName());
		updateGroupParameters.put("numberOfQuestions", group.getNumberOfQuestions());
		updateGroupParameters.put("groupReference", group.getGroupId());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		updateGroupParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		updateGroupParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(updateGroupParameters);
		
		return response;
	}
	
	/**
	 * Updates existing question from database and creates HTML code for updated question, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param questionId the question identifier
	 * @param groupId the group identifier
	 * @param testId the test identifier
	 * @param contentOfQuestion the content of question
	 * @param inputImageForQuestion the picture for question
	 * @param request the HttpServletRequest
	 * @return JSON response which contains HTML code for updated question
	 */
	@RequestMapping(value = "/doEditQuestionAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEditQuestion(@RequestParam(value="questionReference") Integer questionId,
			@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="contentOfQuestionModal") String contentOfQuestion, 
			@RequestParam(value="inputImageForQuestionModal", required=false) MultipartFile inputImageForQuestion,
			HttpServletRequest request) {
		
		logger.debug("Start editing question from database ...");
		logger.debug("Received request with:");
		logger.debug("\tcontentOfQuestion = " + contentOfQuestion);
		logger.debug("\tquestionId = " + questionId);
		logger.debug("\tgroupId = " + groupId);
		logger.debug("\ttestId = " + testId);
		
		if (inputImageForQuestion != null) {
			Picture picture = new Picture();
			picture.setPictureName(inputImageForQuestion.getOriginalFilename());
			picture.setPictureType(FilenameUtils.getExtension(inputImageForQuestion.getOriginalFilename()));
			try {
				picture.setPicture(inputImageForQuestion.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.debug("\t\tCurrent question has also image:"); 
			logger.debug("\t\toriginalFilename = " + inputImageForQuestion.getOriginalFilename());
			logger.debug("\t\tsize (in bytes) = " + inputImageForQuestion.getSize());
			
			// Add new picture to database
			getTestService().addPicture(picture);
		}
		
		
		// Get question from database and make changes
		Question question = getTestService().getQuestionByQuestionId(questionId);
		
		// Getting reference to old picture
		Picture oldQuestionPicture = new Picture();
		if(question.getPictures() != null) {
			oldQuestionPicture = getTestService().getPictureByPictureId(question.getPictures().getPictureId());
		}
		
		question.setContentOfQuestion(contentOfQuestion);
		
		// If picture was edited, change old picture from database
		if(inputImageForQuestion != null) {
			question.setPictures(getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0));
			//getTestService().deletePicture(oldQuestionPicture.getPictureId());
		}
		
		getTestService().updateQuestion(question);
		
		// If picture was edited, delete old picture from database after update answer
		if(inputImageForQuestion != null) {
			getTestService().deletePicture(oldQuestionPicture.getPictureId());
		}
		
		// Update modificationDate and modifiedBy parameters for current test
		Test test = getTestService().getTestByTestId(testId);
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> updateQuestionParameters = new HashMap<String, Object>();
		updateQuestionParameters.put("contentOfQuestion", question.getContentOfQuestion());
		
		if(question.getPictures() != null) {
			updateQuestionParameters.put("picture", "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\" width=\"100px\" height=\"100px\"/>");
			updateQuestionParameters.put("editQuestion", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + question.getContentOfQuestion() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>");
		} else {
			updateQuestionParameters.put("picture", "-");
			updateQuestionParameters.put("editQuestion", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + question.getContentOfQuestion() + "\" data-picture-src=\"\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>");
		}
		
		updateQuestionParameters.put("deleteQuestion", "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteQuestionBtn\" name=\"deleteQuestionBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteQuestion\" data-title=\"Delete Question\" data-message=\"Are you sure you want to delete question '" + question.getContentOfQuestion() + "'?\" data-test-reference=\"" + testId + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		updateQuestionParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		updateQuestionParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(updateQuestionParameters);
		
		return response;
	}
	
	/**
	 * Updates existing answer from database and creates HTML code for updated answer, that will be directly inserted in proper place on the JSP.
	 * 
	 * @param groupId the group identifier
	 * @param questionId the question identifier
	 * @param answerId the answer identifier
	 * @param contentOfAnswer the content of answer
	 * @param whetherCorrect a variable that indicates whether the answer is correct
	 * @param inputImageForAnswer the picture for answer
	 * @param request the HttpServletRequest
	 * @return JSON response which contains HTML code for updated answer
	 */
	@RequestMapping(value = "/doEditAnswerAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEditAnswer(@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="questionReference") Integer questionId,
			@RequestParam(value="answerReference") Integer answerId,
			@RequestParam(value="contentOfAnswerModal") String contentOfAnswer, 
			@RequestParam(value="whetherCorrect") Boolean whetherCorrect,
			@RequestParam(value="inputImageForAnswerModal", required=false) MultipartFile inputImageForAnswer,
			HttpServletRequest request) {
		
		logger.debug("Start editing answer from database ...");
 		logger.debug("Received request with:");
		logger.debug("\tcontentOfAnswer = " + contentOfAnswer);
		logger.debug("\twhetherCorrect = " + whetherCorrect);
		logger.debug("\tanswerId = " + answerId);
		
		if (inputImageForAnswer != null) {
			Picture picture = new Picture();
			picture.setPictureName(inputImageForAnswer.getOriginalFilename());
			picture.setPictureType(FilenameUtils.getExtension(inputImageForAnswer.getOriginalFilename()));
			try {
				picture.setPicture(inputImageForAnswer.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.debug("\t\tCurrent answer has also image:"); 
			logger.debug("\t\toriginalFilename = " + inputImageForAnswer.getOriginalFilename());
			logger.debug("\t\tsize (in bytes) = " + inputImageForAnswer.getSize());
			
			// Add new picture to database
			getTestService().addPicture(picture);
		}
		
		
		// Get answer from database and make changes
		Answer answer = getTestService().getAnswerByAnswerId(answerId);
		Boolean oldWhetherCorrect = answer.isWhetherCorrect();
		
		// Getting reference to old picture
		Picture oldAnswerPicture = new Picture();
		if(answer.getPictures() != null) {
			oldAnswerPicture = getTestService().getPictureByPictureId(answer.getPictures().getPictureId());
		}
		
		answer.setContentOfAnswer(contentOfAnswer);
		answer.setWhetherCorrect(whetherCorrect);
		
		// If picture was edited, change old picture from database
		if(inputImageForAnswer != null) {
			answer.setPictures(getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0));
			//getTestService().deletePicture(oldAnswerPicture.getPictureId());
		}
		
		getTestService().updateAnswer(answer);
		
		// If picture was edited, delete old picture from database after update answer
		if(inputImageForAnswer != null) {
			getTestService().deletePicture(oldAnswerPicture.getPictureId());
		}
		
		if(oldWhetherCorrect != whetherCorrect) {
			// Update question
			Question question = getTestService().getQuestionByQuestionId(questionId);
			Integer newNumberOfCorrectAnswers = null;
			
			if(oldWhetherCorrect) {
				newNumberOfCorrectAnswers = question.getNumberOfCorrectAnswers() - 1;
			} else {
				newNumberOfCorrectAnswers = question.getNumberOfCorrectAnswers() + 1;
			}
			
			question.setNumberOfCorrectAnswers(newNumberOfCorrectAnswers);
			getTestService().updateQuestion(question);
		}
		
		// Update modificationDate and modifiedBy parameters for current test
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		Test test = getTestService().getTestByTestId(group.getTests().getTestId());
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
				
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> updateAnswerParameters = new HashMap<String, Object>();
		updateAnswerParameters.put("contentOfAnswer", answer.getContentOfAnswer());
		updateAnswerParameters.put("whetherCorrect", answer.isWhetherCorrect());
		
		if(answer.getPictures() != null) {
			updateAnswerParameters.put("picture", "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + answer.getPictures().getPictureId() + "\" alt=\"answerImage\" name=\"answerImage\" id=\"answerImage\" width=\"100px\" height=\"100px\"/>");
			updateAnswerParameters.put("editAnswer", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + answer.getContentOfAnswer() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + answer.getPictures().getPictureId() + "\" data-whether-correct=\"" + answer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>");
		} else {
			updateAnswerParameters.put("picture", "-");
			updateAnswerParameters.put("editAnswer", "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + answer.getContentOfAnswer() + "\" data-picture-src=\"\" data-whether-correct=\"" + answer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>");
		}
		
		updateAnswerParameters.put("deleteAnswer", "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteAnswerBtn\" name=\"deleteAnswerBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteAnswer\" data-title=\"Delete Answer\" data-message=\"Are you sure you want to delete answer '" + answer.getContentOfAnswer() + "'?\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
		updateAnswerParameters.put("numberOfCorrectAnswers", getTestService().getQuestionByQuestionId(questionId).getNumberOfCorrectAnswers());
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		updateAnswerParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		updateAnswerParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(updateAnswerParameters);
		
		return response;
	}
	
	/**
	 * Removes group from database and creates HTML code which updates JSP after removing group.
	 * 
	 * @param testId the test identifier
	 * @param groupId the group identifier
	 * @return JSON response which contains HTML code which updates JSP after removing group
	 */
	@RequestMapping(value = "/doDeleteGroupAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDeleteGroup(@RequestParam(value="testReference") Integer testId, 
			@RequestParam(value="groupReference") Integer groupId) {
	
		logger.debug("Start deleting group from database ...");
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);
		logger.debug("\tgroupId = " + groupId);
		
		// Update test
		Test test = getTestService().getTestByTestId(testId);
		Integer newNumberOfGroups = test.getNumberOfGroups() - 1;
		test.setNumberOfGroups(newNumberOfGroups);
		Integer newNumberOfQuestions = test.getNumberOfQuestions() - getTestService().getGroupByGroupId(groupId).getNumberOfQuestions();
		test.setNumberOfQuestions(newNumberOfQuestions);
		
		// Update modificationDate and modifiedBy parameters for current test
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
		
		// Delete group from database (cascade deleting)
		logger.debug("Start cascade deleting group");
		getTestService().deleteGroup(groupId);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> deleteGroupParameters = new HashMap<String, Object>();
		
		deleteGroupParameters.put("groupDiv", "#group_" + groupId);
		deleteGroupParameters.put("numberOfGroupsForTest", test.getNumberOfGroups());
		deleteGroupParameters.put("numberOfQuestionsForTest", test.getNumberOfQuestions());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		deleteGroupParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		deleteGroupParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(deleteGroupParameters);
		
		return response;
	}

	/**
	 * Enable test and creates HTML code which updates JSP after removing group.
	 *
	 * @param testId the test identifier
	 * @return JSON response which contains HTML code which updates JSP after removing group
	 */
	@RequestMapping(value = "/doEnableTestAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doEnableTestGroup(@RequestParam(value="testReference") Integer testId) {

		logger.debug("Start enabling test ...");
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);

		// Update test
		Test test = getTestService().getTestByTestId(testId);
		test.setEnabled(true);

		// Delete group from database (cascade deleting)
		logger.debug("Start enabling the test");
		getTestService().updateTest(test);

		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> enableTestParameters = new HashMap<String, Object>();

		enableTestParameters.put("testDiv", testId);
		enableTestParameters.put("testIsEnabled", test.isEnabled());



		response.setStatus("SUCCESS");
		response.setResult(enableTestParameters);

		return response;
	}

	/**
	 * Enable test and creates HTML code which updates JSP after removing group.
	 *
	 * @param testId the test identifier
	 * @return JSON response which contains HTML code which updates JSP after removing group
	 */
	@RequestMapping(value = "/doDisableTestAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDisableTestGroup(@RequestParam(value="testReference") Integer testId) {

		logger.debug("Start disabling test ...");
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);

		// Update test
		Test test = getTestService().getTestByTestId(testId);
		test.setEnabled(false);

		// Delete group from database (cascade deleting)
		logger.debug("Start disabling the test");
		getTestService().updateTest(test);

		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> disableTestParameters = new HashMap<String, Object>();

		disableTestParameters.put("testDiv", testId);
		disableTestParameters.put("testIsEnabled", test.isEnabled());



		response.setStatus("SUCCESS");
		response.setResult(disableTestParameters);

		return response;
	}



    @RequestMapping(value = "/generatedPasswords", method = {RequestMethod.GET, RequestMethod.POST})
    public String getGeneratePasswordsPage(@RequestParam(value = "studentGroupReference") Integer studentgroupId,
                                                    @RequestParam(value = "testReference") Integer testId,
                                                    @RequestParam(value = "groupReference") Integer groupId,
                                                    ModelMap model){

        List<Students> students = getStudentService().getStudentsByStudentGroupId(studentgroupId);

        List<String> passwords=new ArrayList<String>();

            for (Students student : students) {

				String password=getTestService().generatePassword();

				/*User registerUser=new User();

				ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();	// hash SHA1
				String hashedPassword = shaPasswordEncoder.encodePassword(password, null);
				logger.debug("HashedPassword = " + hashedPassword);

				registerUser.setFirstname(student.getStudentFirstname());
				registerUser.setLastname(student.getStudentLastname());
				registerUser.setUserRoles(getUserService().getUserRolebyNazwa("ROLE_STUDENT"));
				registerUser.setEnabled(true);
				registerUser.setEmail(student.getStudentEmail());
				registerUser.setPassword(hashedPassword);

                getUserService().add(registerUser);
*/
                passwords.add(password);


            }
            model.put("passwordsModel",passwords);
            model.put("allStudentsModel",students);
            model.put("testModel",testId);
            model.put("groupModel",groupId);
            model.put("studentGroupIdModel",studentgroupId);



        return "generatedpasswordspage";
    }

    @RequestMapping(value = "/doSendEmails", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doSendEmails(@RequestParam(value="testReference") Integer testId,
                                                   @RequestParam(value = "studentGroupReference") Integer studentgroupId,
                                                   @RequestParam(value = "passwordsReference") List<String> passwords,
                                                   @RequestParam(value="_csrf") String csrfToken){


        List<Students> students = getStudentService().getStudentsByStudentGroupId(studentgroupId);
        int i=0;
        for (Students student : students) {

            EmailService emailService = new EmailService(student.getStudentEmail(), "EQMS login credentials", "Login with this credentials to take the exam."+ System.lineSeparator() +"E-mail: "+student.getStudentEmail()+System.lineSeparator()+"Password: " + passwords.get(i));
            emailService.sendMessage();
            i++;
        }

        // Create JSON response
        JsonResponse response = new JsonResponse();

        response.setStatus("SUCCESS");

        return response;
    }


	@RequestMapping(value = "/printToPdf", method = RequestMethod.POST)
	public @ResponseBody JsonResponse printToPdf(@RequestParam(value="testReference") Integer testId,
												   @RequestParam(value = "studentGroupReference") Integer studentgroupId,
												   @RequestParam(value = "passwordsReference") List<String> passwords,
												   @RequestParam(value="_csrf") String csrfToken){
		try{
			Document document=new Document();
			PdfWriter.getInstance(document, new FileOutputStream("d:/Exam_passwords_" + getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId).getStudentgroupName() + ".pdf"));
			document.open();
			document.add(new Paragraph("Passwords for the test: '"+getTestService().getTestByTestId(testId).getTestName()
			+ "' for students from group: '" + getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId).getStudentgroupName()+"'"));
			document.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}

		JsonResponse response=new JsonResponse();
		response.setStatus("SUCCESS");
		return response;
	}







	/**
	 * Removes question from database and creates HTML code which updates JSP after removing question.
	 * 
	 * @param testId the test identifier
	 * @param groupId the group identifier
	 * @param questionId the question identifier
	 * @return JSON response which contains HTML code which updates JSP after removing question
	 */
	@RequestMapping(value = "/doDeleteQuestionAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDeleteQuestion(@RequestParam(value="testReference") Integer testId,
			@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="questionReference") Integer questionId) {
	
		logger.debug("Start deleting question from database ...");
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);
		logger.debug("\tgroupId = " + groupId);
		logger.debug("\tquestionId = " + questionId);
	
		// Update group and test
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		Integer newNumberOfQuestionsForGroup = group.getNumberOfQuestions() - 1;
		group.setNumberOfQuestions(newNumberOfQuestionsForGroup);
		getTestService().updateGroup(group);
		
		Test test = getTestService().getTestByTestId(testId);
		Integer newNumberOfQuestionsForTest = test.getNumberOfQuestions() - 1;
		test.setNumberOfQuestions(newNumberOfQuestionsForTest);
		
		// Update modificationDate and modifiedBy parameters for current test
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);
		
		// Delete question from database (cascade deleting)
		logger.debug("Start cascade deleting question");
		getTestService().deleteQuestion(questionId);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> deleteQuestionParameters = new HashMap<String, Object>();
		
		deleteQuestionParameters.put("numberOfQuestionsForGroup", getTestService().getGroupByGroupId(groupId).getNumberOfQuestions());
		deleteQuestionParameters.put("numberOfQuestionsForTest", getTestService().getTestByTestId(testId).getNumberOfQuestions());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		deleteQuestionParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		deleteQuestionParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(deleteQuestionParameters);
		
		return response;
	}
	
	/**
	 * Removes answer from database and creates HTML code which updates JSP after removing answer.
	 * 
	 * @param groupId the group identifier
	 * @param questionId the question identifier
	 * @param answerId the answer identifier
	 * @return JSON response which contains HTML code which updates JSP after removing answer
	 */
	@RequestMapping(value = "/doDeleteAnswerAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDeleteAnswer(@RequestParam(value="groupReference") Integer groupId,
			@RequestParam(value="questionReference") Integer questionId,
			@RequestParam(value="answerReference") Integer answerId) {
	
		logger.debug("Start deleting answer from database ...");
		logger.debug("Received request with:");
		logger.debug("\tgroupId = " + groupId);
		logger.debug("\tquestionId = " + questionId);
		logger.debug("\tanswerId = " + answerId);
	
		// Update question
		Answer answer = getTestService().getAnswerByAnswerId(answerId);
		Question question = getTestService().getQuestionByQuestionId(questionId);
		Integer newNumberOfAnswers = question.getNumberOfAnswers() - 1;
		question.setNumberOfAnswers(newNumberOfAnswers);
		
		if(answer.isWhetherCorrect()) {
			Integer newNumberOfCorrectAnswers = question.getNumberOfCorrectAnswers() - 1;
			question.setNumberOfCorrectAnswers(newNumberOfCorrectAnswers);
		}
		
		getTestService().updateQuestion(question);
		
		// Delete answer from database (cascade deleting)
		logger.debug("Start cascade deleting answer");
		getTestService().deleteAnswer(answerId);
		
		// Update modificationDate and modifiedBy parameters for current test
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		Test test = getTestService().getTestByTestId(group.getTests().getTestId());
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setModificationDate(new Date());
		test.setModifiedBy(currentUserEmail);
		getTestService().updateTest(test);

		// Create JSON response
		JsonResponse response = new JsonResponse();
		Map<String, Object> deleteAnswerParameters = new HashMap<String, Object>();
		
		//Question question = getTestService().getQuestionByQuestionId(questionId);
		deleteAnswerParameters.put("numberOfAnswers", question.getNumberOfAnswers());
		deleteAnswerParameters.put("numberOfCorrectAnswers", question.getNumberOfCorrectAnswers());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		deleteAnswerParameters.put("modificationDate", sdf.format(test.getModificationDate()));
		deleteAnswerParameters.put("modifiedBy", currentUserEmail);
		
		response.setStatus("SUCCESS");
		response.setResult(deleteAnswerParameters);
		
		return response;
	}
	
	/**
	 * Removes test from database and creates HTML code which updates JSP after removing test.
	 * 
	 * @param testId the test identifier
	 * @return JSON response which contains HTML code which updates JSP after removing test
	 */
	@RequestMapping(value = "/doDeleteTestAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doDeleteTest(@RequestParam(value="testReference") Integer testId) {
		
		logger.debug("Start deleting test from database ...");
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);
		
		// Delete test from database (cascade deleting)
		logger.debug("Start cascade deleting test");
		getTestService().deleteTest(testId);
		
		// Create JSON response
		JsonResponse response = new JsonResponse();
		
		response.setStatus("SUCCESS");
		
		return response;
	}
	
	/**
	 * Prints all details of test.
	 * 
	 * @param test the test
	 */
	public void printTestDetails(Test test) {
		logger.debug("Printing test details\n");
		
		logger.debug("\ttestId            = " + test.getTestId());
		logger.debug("\ttestName          = " + test.getTestName());
		logger.debug("\ttimeForTest       = " + test.getTimeForTest());
		logger.debug("\tnumberOfGroups    = " + test.getNumberOfGroups());
		logger.debug("\tnumberOfQuestions = " + test.getNumberOfQuestions());
		logger.debug("\tcreatedBy         = " + test.getCreatedBy());
		logger.debug("\tcreationDate      = " + test.getCreationDate());
		logger.debug("\tmodifiedBy        = " + test.getModifiedBy());
		logger.debug("\tmodificationDate  = " + test.getModificationDate() + "\n");
		
		logger.debug("\tsetId  = " + test.getSetsOfRating().getSetId());
		logger.debug("\tmark5  = " + test.getSetsOfRating().getMark5());
		logger.debug("\tmark4+ = " + test.getSetsOfRating().getMark4_5());
		logger.debug("\tmark4  = " + test.getSetsOfRating().getMark4());
		logger.debug("\tmark3+ = " + test.getSetsOfRating().getMark3_5());
		logger.debug("\tmark3  = " + test.getSetsOfRating().getMark3());
		logger.debug("\tmark2  = " + test.getSetsOfRating().getMark2() + "\n");
		
		Set<GroupOfQuestions> groups = test.getGroupsOfQuestionses();
		for(GroupOfQuestions group : groups) {
			logger.debug("\t{");
			logger.debug("\t\tgroupId           = " + group.getGroupId());
			logger.debug("\t\tgroupName         = " + group.getGroupName());
			logger.debug("\t\tnumberOfQuestions = " + group.getNumberOfQuestions());
			
			Set<Question> questions = group.getQuestionses();
			for(Question question : questions) {
				logger.debug("\t\t{");
				logger.debug("\t\t\tquestionId             = " + question.getQuestionId());
				logger.debug("\t\t\tcontentOfQuestion      = " + question.getContentOfQuestion());
				logger.debug("\t\t\tnumberOfAnswers        = " + question.getNumberOfAnswers());
				logger.debug("\t\t\tnumberOfCorrectAnswers = " + question.getNumberOfCorrectAnswers());
				
				if(question.getPictures() != null) {
					logger.debug("\t\t\tpictureId              = " + question.getPictures().getPictureId());
					logger.debug("\t\t\tpictureName            = " + question.getPictures().getPictureName());
					logger.debug("\t\t\tpictureType            = " + question.getPictures().getPictureType());
				}
				
				Set<Answer> answers = question.getAnswerses();
				for(Answer answer : answers) {
					logger.debug("\t\t\t{");
					logger.debug("\t\t\t\tanswerId         = " + answer.getAnswerId());
					logger.debug("\t\t\t\tcontentOfAnswer  = " + answer.getContentOfAnswer());
					logger.debug("\t\t\t\tisWhetherCorrect = " + answer.isWhetherCorrect());
					
					if(answer.getPictures() != null) {
						logger.debug("\t\t\t\tpictureId        = " + answer.getPictures().getPictureId());
						logger.debug("\t\t\t\tpictureName      = " + answer.getPictures().getPictureName());
						logger.debug("\t\t\t\tpictureType      = " + answer.getPictures().getPictureType());
					}
					
					logger.debug("\t\t\t}");
				}
				logger.debug("\t\t}");
			}
			logger.debug("\t}");
		}
	}
	
	/**
	 * Gets page which contains form to add test.
	 * 
	 * @param subjectId the subject identifier
	 * @param model the model
	 * @return logic view name represented by String type
	 */
	@RequestMapping(value = "/addTest", method = RequestMethod.POST)
	public String getAddTestPage(@RequestParam(value="subjectReference") Integer subjectId, ModelMap model) {
		logger.debug("Received request to show add test page");
		logger.debug("Received subjectId = " + subjectId);
		
		model.put("subjectId", subjectId);
		
		return "addtestpage";
	}
	
	/**
	 * Gets page which contains form to update test.
	 * 
	 * @param testId the test identifier
	 * @param subjectId the subject identifier
	 * @param model the model
	 * @return logic view name represented by String type
	 */
	@RequestMapping(value = "/editTest", method = {RequestMethod.POST, RequestMethod.GET})
	public String getEditTestPage(@RequestParam(value="testId") Integer testId, 
			@RequestParam(value="subjectId") Integer subjectId, ModelMap model) {
		
		logger.debug("Received request with testId to edit to database");
		logger.debug("Received request with:");
		logger.debug("testId = " + testId);
		logger.debug("subjectId = " + subjectId);
		
		Test test = getTestService().getTestByTestId(testId);
		
		model.put("subject", test.getSubjects());
		model.put("test", test);
		model.put("set", getTestService().getSetOfRatingBySetId(test.getSetsOfRating().getSetId()));
		model.put("groups", getTestService().getAllGroupsByTestId(testId));
        model.put("allStudentGroupsModel", getStudentGroupsService().getAllStudentGroups(Order.asc("studentgroupId"), null));
		
		return "edittestpage";
	}
	
	/**
	 * Gets picture as array of bytes.
	 * 
	 * @param pictureId the picture identifier
	 * @return the picture as array of bytes
	 */
	@RequestMapping(value = "/image/{pictureId}")
	@ResponseBody
	public byte[] getPicture(@PathVariable Integer pictureId)  {
	  Picture picture = getTestService().getPictureByPictureId(pictureId);
	  return picture.getPicture();
	}
	
	/**
	 * Reads all parameters from MultipartHttpServletRequest and creates instance of test.
	 * In the next step, the test is added to the database.
	 * 
	 * @param request the MultipartHttpServletRequest
	 * @param model the model
	 * @return redirect to main tests page
	 */
	@RequestMapping(value = "/doAddTest", method = RequestMethod.POST)
	public String doAddTest(MultipartHttpServletRequest request, ModelMap model) {
		logger.debug("Received request with test parameters to add to database");
		
		Boolean hasErrors = false;
		String reason = "";
		
		/**
		 * Displaying all request parameters
		 */
		Enumeration<String> parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			logger.debug("paramName: " + paramName.toString());
			
			String[] paramValues = request.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				logger.debug("paramValue: " + paramValue.toString());
			}
		}
		
		Iterator<String> fileNames = request.getFileNames();
		
		while(fileNames.hasNext()) {
			String fileName = fileNames.next();
			logger.debug("submitedFileName: " + fileName.toString());
			
			
			List<MultipartFile> files = request.getFiles(fileName);
			for(MultipartFile mFile : files) {
				System.out.println("originalFileName = " + mFile.getOriginalFilename());
			}
		}
		
		
		logger.debug("Start parsing MultipartHTTPServletRequest");
		
		/**
		 * Getting email of current logged user
		 */
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		logger.debug("Address email of current logged user = " + currentUserEmail);
		
		logger.debug("Creating test details ...");
		Test test = new Test();
		test.setTestName(request.getParameter("testName"));
		test.setTimeForTest(Integer.parseInt(request.getParameter("timeForTest")));
		if(request.getParameter("numberOfGroups").equals("")) {
			test.setNumberOfGroups(0);
		} else {
			test.setNumberOfGroups(Integer.parseInt(request.getParameter("numberOfGroups")));
		}
		if(request.getParameterValues("contentOfQuestion") == null) {
			test.setNumberOfQuestions(0);
		} else {
			test.setNumberOfQuestions(request.getParameterValues("contentOfQuestion").length);
		}
		test.setCreatedBy(currentUserEmail);
		test.setCreationDate(new Date());
		
		SetOfRating set = new SetOfRating();
		set.setMark2(Integer.valueOf(request.getParameter("mark2")));
		set.setMark3(Integer.valueOf(request.getParameter("mark3")));
		set.setMark3_5(Integer.valueOf(request.getParameter("mark3_5")));
		set.setMark4(Integer.valueOf(request.getParameter("mark4")));
		set.setMark4_5(Integer.valueOf(request.getParameter("mark4_5")));
		set.setMark5(Integer.valueOf(request.getParameter("mark5")));
		test.setSetsOfRating(set);
		
		Subject subject = getTestService().getSubjectBySubjectId(Integer.valueOf(request.getParameter("subjectId")));
		test.setSubjects(subject);
		

		Set<GroupOfQuestions> groupsSet = new HashSet<GroupOfQuestions>();						// Set contains all groups for test
		Integer numberOfGroups;																	// Number of groups for test

		// Variables for groups
		String [] groupNameValues = request.getParameterValues("groupName");					// Tab contains all group names
		String [] numbersOfQuestionsTab = request.getParameterValues("numberOfQuestions");		// Tab with numbers of questions for each group
		
		// Variables for questions
		String [] contentOfQuestionValues = request.getParameterValues("contentOfQuestion");	// Tab contains all contents of questions
		String [] numbersOfAnswersTab = request.getParameterValues("numberOfAnswers");			// Tab with numbers of answers for each question
		List<MultipartFile> picturesForQuestion = request.getFiles("inputImageForQuestion");	// List contains pictures for each question
		Integer allQuestionsCounter = 0;
		
		// Variables for answer
		String [] contentOfAnswerValues = request.getParameterValues("contentOfAnswer");		// Tab contains all contents of answers
		String [] whetherCorrectValues = request.getParameterValues("whetherCorrectSelect");	// Tab contains all "whetherCorrect" status for each answer
		List<MultipartFile> picturesForAnswer = request.getFiles("inputImageForAnswer");		// List with pictures for each answer
		Integer allAnswersCounter = 0;
		
		if(!request.getParameter("numberOfGroups").equals("")) {
			logger.debug("\tRequest contains group(s)");
			
			numberOfGroups = Integer.parseInt(request.getParameter("numberOfGroups"));
			
			// Loop with groups
			for(int i=0; i<numberOfGroups; i++) {
				
				// Number of questions for current group - inside form this value can be empty
				Integer numberOfQuestionsForCurrentGroup;
				if(numbersOfQuestionsTab[i].equals("")) {
					numberOfQuestionsForCurrentGroup = 0;
				} else {
					numberOfQuestionsForCurrentGroup = Integer.parseInt(numbersOfQuestionsTab[i]);
				}
				
				GroupOfQuestions group = new GroupOfQuestions();
				group.setGroupName(groupNameValues[i]);
				group.setNumberOfQuestions(numberOfQuestionsForCurrentGroup);
				
				Set<Question> questiosSet = new HashSet<Question>();
				
				if(contentOfQuestionValues != null) {
					logger.debug("Request contains group(s) and question(s)"); 
					
					// Loop with questions for current group
					for(int j = 0; j < numberOfQuestionsForCurrentGroup; j++) {
						
						// Number of answers for current question - inside form this value can be empty
						Integer numberOfAnswersForCurrentQuestion;
						if(numbersOfAnswersTab[allQuestionsCounter].equals("")) {
							numberOfAnswersForCurrentQuestion = 0;
						} else {
							numberOfAnswersForCurrentQuestion = Integer.parseInt(numbersOfAnswersTab[allQuestionsCounter]);
						}
						
						/*if(numberOfAnswersForCurrentQuestion < 2) {
							hasErrors = true;
							reason = reason + "Number of answers for question " + (j+1) + " for group " + (i+1) + " is < 2\n";
						}*/
						
						Question question = new Question();
						question.setContentOfQuestion(contentOfQuestionValues[allQuestionsCounter]);
						question.setNumberOfAnswers(numberOfAnswersForCurrentQuestion);
						
						MultipartFile pictureQuestion = picturesForQuestion.get(allQuestionsCounter);
						if(!pictureQuestion.isEmpty()) {
							Picture picture = new Picture();
							picture.setPictureName(pictureQuestion.getOriginalFilename());
							picture.setPictureType(FilenameUtils.getExtension(pictureQuestion.getOriginalFilename()));
							try {
								picture.setPicture(pictureQuestion.getBytes());
							} catch (IOException e) {
								e.printStackTrace();
							}
							question.setPictures(picture);
						}
						
						Set<Answer> answersSet = new HashSet<Answer>();
						
						if(contentOfAnswerValues != null) {
							logger.debug("\t\t\tRequest contains group(s), question(s) and answers(s)");
							
							// Loop with answers for current question
							for(int k = 0; k < numberOfAnswersForCurrentQuestion; k++) {
								
								Answer answer = new Answer();
								answer.setContentOfAnswer(contentOfAnswerValues[allAnswersCounter]);
								answer.setWhetherCorrect(Boolean.parseBoolean(whetherCorrectValues[allAnswersCounter]));
								
								MultipartFile pictureAnswer = picturesForAnswer.get(allAnswersCounter);
								if(!pictureAnswer.isEmpty()) {
									Picture picture = new Picture();
									picture.setPictureName(pictureAnswer.getOriginalFilename());
									picture.setPictureType(FilenameUtils.getExtension(pictureAnswer.getOriginalFilename()));
									try {
										picture.setPicture(pictureAnswer.getBytes());
									} catch (IOException e) {
										e.printStackTrace();
									}
									answer.setPictures(picture);
								}
								
								answersSet.add(answer);
								allAnswersCounter++;
							}
						}
						
						question.setAnswerses(answersSet);
						questiosSet.add(question);
						allQuestionsCounter++;
					}
				}
				
				group.setQuestionses(questiosSet);
				groupsSet.add(group);
			}
		}
		
		test.setGroupsOfQuestionses(groupsSet);
		
		printTestDetails(test);
		
		logger.debug("Checking whether current parsed test has errors = " + hasErrors);
		if (hasErrors) {
			logger.debug("Error reason = " + reason);
		} else {
			logger.debug("Current test has no errors after parsing");
			logger.debug("\tStart adding test to database ...");
			
			addTestToDatabase(test);
			
			logger.debug("\tEnd adding test to database ...");
		}
		
		logger.debug("End parsing MultipartHTTPServletRequest");
	
		return "redirect:/tests/index";
	}
	
	/**
	 * Adds new test to database.
	 * 
	 * @param test the test
	 */
	public void addTestToDatabase(Test test) {
		
		SetOfRating set = test.getSetsOfRating();
		getTestService().addSetOfRating(set);
		
		SetOfRating lastAddedSet = getTestService().getAllSetsOfRating(Order.desc("setId"), 1).get(0);
		
		test.setSetsOfRating(lastAddedSet);
		test.setNumberOfGroups(0);
		test.setNumberOfQuestions(0);
		test.setEnabled(false);
		getTestService().addTest(test);
		
		Test lastAddedTest = getTestService().getAllTests(Order.desc("testId"), 1).get(0);
		
		Set<GroupOfQuestions> groupsSet = test.getGroupsOfQuestionses();
		for(GroupOfQuestions group : groupsSet) {
			
			Set<Question> questionsSet = group.getQuestionses();
			
			group.setQuestionses(null);
			group.setTests(lastAddedTest);
			group.setNumberOfQuestions(0);
			getTestService().addGroup(group);
			
			GroupOfQuestions lastAddedGroup = getTestService().getAllGroups(Order.desc("groupId"), 1).get(0);
			
			for(Question question : questionsSet) {
				
				Set<Answer> answersSet = question.getAnswerses();
				
				if(question.getPictures() != null) {
					Picture questionPicture = question.getPictures();
					getTestService().addPicture(questionPicture);
					
					Picture lastAddedQuestionPicture = getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0);
					question.setPictures(lastAddedQuestionPicture);
				}
				
				question.setAnswerses(null);
				question.setNumberOfAnswers(0);
				question.setNumberOfCorrectAnswers(0);
				getTestService().addQuestion(question, lastAddedGroup.getGroupId());
				
				Question lastAddedQuestion = getTestService().getAllQuestions(Order.desc("questionId"), 1).get(0);
				
				for(Answer answer : answersSet) {
					
					if(answer.getPictures() != null) {
						Picture answerPicture  = answer.getPictures();
						getTestService().addPicture(answerPicture);
						
						Picture lastAddedAnswerPicture = getTestService().getAllPictures(Order.desc("pictureId"), 1).get(0);
						answer.setPictures(lastAddedAnswerPicture);
					}
					
					getTestService().addAnswer(answer, lastAddedQuestion.getQuestionId());
				}
			}
		}
	}
	
	/**
	 * Gets all test by specific subject.
	 * 
	 * @param subjectId the subject identifier
	 * @param csrfToken the CSRF token
	 * @param request the HttpServletRequest
	 * @return JSON response which contains table with tests as HTML code
	 */
	@RequestMapping(value = "/getTestsBySubject", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getTestsBySubject(@RequestParam(value="subjectId") Integer subjectId,
			@RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {
		
		JsonResponse response = new JsonResponse();

		List<Test> tests = getTestService().getTestsBySubjectId(subjectId);
		
		if(tests.isEmpty()) {	// If list is empty returns comment "There are no tests", otherwise returns table with retrieved data 
			
			String resultEmptyList = "<div class=\"well\">"
										+ "There is no tests."
										+ "</div>";
		
			response.setStatus("SUCCESS");
			response.setResult(resultEmptyList);
		} else {
		
			String resultSuccess = "<div class=\"table-responsive\" style=\"margin-bottom: 0px\">"
					+ "<table class=\"table table-bordered\" id=\"tests_table\">"
					+ "<thead>"
						+ "<tr class=\"success\">"
							+ "<th style=\"width: 30%;\">NAME</th>"
							+ "<th>GROUPS</th>"
							+ "<th>QUESTIONS</th>"
							+ "<th>TIME</th>"
							+ "<th style=\"width: 10%;\">CREATION DATE</th>"
							+ "<th>CREATED BY</th>"
							+ "<th style=\"width: 10%;\">MODIFICATION DATE</th>"
							+ "<th>MODIFIED BY</th>"
							+ "<th style=\"border-right-width: 0px;\"></th>"
							+ "<th style=\"border-right-width: 0px;\"></th>"
						+ "</tr>"
					+ "</thead>"
					+ "<tbody>";
			
			for(Test test : tests) {
				User userCreatedBy = getUserService().findByEmail(test.getCreatedBy());
				User userModifiedBy = getUserService().findByEmail(test.getModifiedBy());
				
				resultSuccess += "<tr class=\"success\" data-key=\"" + test.getTestId() + "\">";
				resultSuccess += "<td>" + test.getTestName() + "</td>"
								+ "<td>" + test.getNumberOfGroups() + "</td>"
				 				+ "<td>" + test.getNumberOfQuestions() + "</td>"
				 				+ "<td>" + test.getTimeForTest() + "</td>"
				 				+ "<td>" + ((test.getCreationDate() != null) ? test.getCreationDate() : "-") + "</td>"
				 				+ "<td data-toggle=\"tooltip\" data-placement=\"left\" title=\"" + ((userCreatedBy != null) ? (userCreatedBy.getFirstname() + " " + userCreatedBy.getLastname()) : "") + "\">" + test.getCreatedBy() + "</td>"
				 				+ "<td>" + ((test.getModificationDate() != null) ? test.getModificationDate() : "-") + "</td>"
				 				+ "<td data-toggle=\"tooltip\" data-placement=\"left\" title=\"" + ((userModifiedBy != null) ? (userModifiedBy.getFirstname() + " " + userModifiedBy.getLastname()) : "") + "\">" + ((test.getModifiedBy() != null) ? test.getModifiedBy() : "-") + "</td>"
				 				
				 				+ "<td>"
					 				+ "<form action=\"" + getURLWithContextPath(request) + "/tests/editTest\" method=\"post\" id=\"editTestForm\">"
						 				+ "<input type=\"hidden\" name=\"testId\" value=\"" + test.getTestId() + "\" />"
						 				+ "<input type=\"hidden\" name=\"subjectId\" value=\"" + subjectId + "\" />"
						 				+ "<button type=\"submit\" id=\"editTestBtn\" name=\"editTestBtn\" class=\"btn btn-info btn-block btn-sm\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>"
						 				+ "<input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken + "\" />"
					 				+ "</form>"
				 				+ "</td>"
					 				
				 				+ "<td>"
				 					+ "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteTestBtn\" name=\"deleteTestBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteTest\" data-title=\"Delete Test\" data-message=\"Are you sure you want to delete test '" + test.getTestName() + "'?\" data-test-reference=\"" + test.getTestId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
				 				+ "</td>";
				 				
				resultSuccess += "</tr>";
			}
			
			resultSuccess += "</tbody>";
			resultSuccess += "</table>";
			resultSuccess += "</div>";
				
			response.setStatus("SUCCESS");
			response.setResult(resultSuccess);
		}

		return response;
	}
	
	/**
	 * Gets all groups by specific test.
	 * 
	 * @param testId the test identifier
	 * @param csrfToken the CSRF token
	 * @param request the HttpServletRequest
	 * @return JSON response which contains table with groups as HTML code
	 */
	@RequestMapping(value = "/getGroupsByTest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getGroupsByTest(@RequestParam(value="testId") Integer testId, 
			@RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {
		JsonResponse response = new JsonResponse();
		
		List<GroupOfQuestions> groups = getTestService().getAllGroups(Order.asc("groupId"), null);
		Boolean hasGroups = false;

		String resultSuccess = "<div class=\"slider\">";
		//resultSuccess += "<div class=\"table-responsive\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<table class=\"table table-bordered warning\" id=\"group_table_" + testId + "\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<thead>"
							+ "<tr class=\"warning\">"
								+ "<th>GROUP NAME</th>"
								+ "<th>NUMBER OF QUESTIONS</th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>";
		
		for(GroupOfQuestions group : groups) {
			if(testId == group.getTests().getTestId()) {
				hasGroups = true;
				
				resultSuccess += "<tr class=\"warning\">"
									+ "<td>" + group.getGroupName() + "</td>"
									+ "<td>" + group.getNumberOfQuestions() + "</td>"
									+ "<td>"
						 				+ "<form action=\"" + getURLWithContextPath(request) + "/tests/editGroup\" method=\"post\" id=\"editGroupForm\">"
							 				+ "<input type=\"hidden\" name=\"groupId\" value=\"" + group.getGroupId() + "\" />"
							 				+ "<button type=\"submit\" id=\"editGroupBtn\" name=\"editGroupBtn\" class=\"btn btn-info btn-block btn-sm\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>"
							 				+ "<input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken + "\" />"
						 				+ "</form>"
					 				+ "</td>"
					 				+ "<td>"
					 					+ "<form action=\"" + getURLWithContextPath(request) + "/tests/deleteGroup\" method=\"post\" id=\"deleteGroupForm\">"
					 						+ "<input type=\"hidden\" name=\"groupId\" value=\"" + group.getGroupId() + "\" />"
					 						+ "<button type=\"submit\" id=\"deleteGroupBtn\" name=\"deleteGroupBtn\" class=\"btn btn-danger btn-block btn-sm\" data-toggle=\"modal\" data-target=\"#confirmDeleteGroupModal\" data-title=\"Delete Group\" data-message=\"Are you sure you want to delete group '" + group.getGroupName() + "'?\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
					 						+ "<input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken + "\" />"
					 					+ "</form>"
				 					+ "</td>"
								+ "</tr>";
			}
		}
		
		resultSuccess += "</tbody>";
		resultSuccess += "</table>";
		//resultSuccess += "</div>";
		resultSuccess += "</div>";
		
		resultSuccess += "<script type=\"text/javascript\">"
				+ "$('#group_table_" + testId + "').DataTable( {"
					+ "\"autoWidth\": false,"
					+ "\"paging\": true,"
					+ "\"searching\": true,"
					+ "\"ordering\":  true,"
					+ "\"lengthMenu\": [ [1, 5, 10, 25, 50, -1], [1, 5, 10, 25, 50, \"All\"] ],"
					+ "\"pageLength\": 5,"
					+ "\"columns\": ["
						+ "{ \"width\": \"68%\" },"
						+ "{ \"width\": \"20%\" },"
						+ "{ \"orderable\": false, \"width\": \"5%\" },"
						+ "{ \"orderable\": false, \"width\": \"7%\" }"
					+ " ],"
					+ "\"language\": {"
						+ "\"info\": \"Showing _START_ to _END_ of _TOTAL_ groups\","
						+ "\"infoEmpty\": \"Showing 0 to 0 of 0 groups\","
						+ "\"infoFiltered\": \"(filtered from _MAX_ total groups)\","
						+ "\"lengthMenu\": \"Show _MENU_ groups\""
					+ "}"
				+ "} );"
				+ "</script>";
		
		if (hasGroups) {
			response.setStatus("SUCCESS");
			response.setResult(resultSuccess);
			
		} else {
			String resultNoGroups = "<div class=\"slider\">"
					+ "<div class=\"well\" style=\"margin-bottom: 0px\">"
						+ "There is no groups."
					+ "</div>"
					+ "</div>"; 
			
			response.setStatus("FAIL");
			response.setResult(resultNoGroups);
		}
		
		return response;
	}
	
	/**
	 * Gets all questions by specific group.
	 * 
	 * @param groupId the group identifier
	 * @param csrfToken the CSRF token
	 * @param request the HttpServletRequest
	 * @return JSON response which contains table with questions as HTML code
	 */
	@RequestMapping(value = "/getQuestionsByGroup", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getQuestionsByGroup(@RequestParam(value="groupId") Integer groupId, 
			@RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {
		JsonResponse response = new JsonResponse();
		
		GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
		Integer testId = group.getTests().getTestId();
		List<Question> questions = getTestService().getAllQuestionsByGroupId(groupId);
		Boolean hasQuestions = false;

		if(!questions.isEmpty()) { 
			hasQuestions = true; 
		}
		
		String resultSuccess = "<div class=\"col-sm-12\" style=\"margin-top: 25px;\">";
		resultSuccess += "<div class=\"table-responsive\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<table class=\"table table-bordered warning\" id=\"question_table_" + groupId + "\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<thead>"
							+ "<tr class=\"success\">"
								+ "<th>CONTENT OF QUESTION</th>"
								+ "<th>NUMBER OF ANSWERS</th>"
								+ "<th>NUMBER OF CORRECT ANSWERS</th>"
								+ "<th>PICTURE</th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>";
		
		for(Question question : questions) {

			resultSuccess += "<tr class=\"success\" data-key=\"" + question.getQuestionId() + "\">"
								+ "<td>" + question.getContentOfQuestion() + "</td>"
								+ "<td>" + question.getNumberOfAnswers() + "</td>"
								+ "<td>" + question.getNumberOfCorrectAnswers() + "</td>";
								
								if(question.getPictures() != null) {
									// <c:url value="/tests/image/${question.getPictures().getPictureId()}
									resultSuccess += "<td style=\"overflow: hidden; padding: 5px;\" align=\"center\"><img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\" width=\"100px\" height=\"100px\"/></td>";
								} else {
									resultSuccess += "<td>-</td>";
								}
						
				resultSuccess += "<td>"
									+ "<button type=\"button\" class=\"btn btn-success btn-sm\" id=\"addAnswerBtn\" name=\"addAnswerBtn\" data-toggle=\"modal\" data-target=\"#addAnswerModal\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span> Add answer</button>"
								+ "</td>"		
								+ "<td>";
									
									if(question.getPictures() != null) {
										resultSuccess += "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + question.getContentOfQuestion() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>";
									} else {
										resultSuccess += "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editQuestionBtn\" name=\"editQuestionBtn\" data-toggle=\"modal\" data-target=\"#editQuestionModal\" data-content-of-question=\"" + question.getContentOfQuestion() + "\" data-picture-src=\"\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit question</button>";
									}

				resultSuccess += "</td>"
				 				+ "<td>"
				 					+ "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteQuestionBtn\" name=\"deleteQuestionBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteQuestion\" data-title=\"Delete Question\" data-message=\"Are you sure you want to delete question '" + question.getContentOfQuestion() + "'?\" data-test-reference=\"" + testId + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + question.getQuestionId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
			 					+ "</td>"
			 					+ "<td class=\"details-control\"></td>"
							+ "</tr>";
		}
		
		resultSuccess += "</tbody>";
		resultSuccess += "</table>";
		resultSuccess += "</div>";
		resultSuccess += "</div>";
		
		resultSuccess += "<script type=\"text/javascript\">"
				+ "$('#question_table_" + groupId + "').DataTable( {"
					+ "\"autoWidth\": false,"
					+ "\"paging\": true,"
					+ "\"searching\": true,"
					+ "\"ordering\":  true,"
					+ "\"lengthMenu\": [ [1, 5, 10, 25, 50, -1], [1, 5, 10, 25, 50, \"All\"] ],"
					+ "\"pageLength\": 5,"
					+ "\"columns\": ["
						+ "{ \"width\": \"47%\" },"
						+ "{ \"width\": \"14%\" },"
						+ "{ \"width\": \"14%\" },"
						+ "{ \"orderable\": false, \"width\": \"11%\" },"
						+ "{ \"orderable\": false, \"width\": \"5%\" },"
						+ "{ \"orderable\": false, \"width\": \"5%\" },"
						+ "{ \"orderable\": false, \"width\": \"4%\" },"
						+ "{ \"orderable\": false, \"width\": \"3%\" }"
					+ " ],"
					+ "\"language\": {"
						+ "\"info\": \"Showing _START_ to _END_ of _TOTAL_ questions\","
						+ "\"infoEmpty\": \"Showing 0 to 0 of 0 questions\","
						+ "\"infoFiltered\": \"(filtered from _MAX_ total questions)\","
						+ "\"lengthMenu\": \"Show _MENU_ questions\""
					+ "}"
				+ "} );"
				+ "</script>";
		
		if (hasQuestions) {
			response.setStatus("SUCCESS");
			response.setResult(resultSuccess);
			
		} else {
			String resultNoGroups = "<div class=\"col-sm-12\" style=\"margin-bottom: 0px; margin-top: 25px;\">"
						+ "There is no questions."
					+ "</div>"; 
			
			response.setStatus("FAIL");
			response.setResult(resultNoGroups);
		}
		
		return response;
	}
	
	/**
	 * Gets all answers by specific question.
	 * 
	 * @param questionId the question identifier
	 * @param csrfToken the CSRF token
	 * @param request the HttpServletRequest
	 * @return JSON response which contains table with answers as HTML code
	 */
	@RequestMapping(value = "/getAnswersByQuestion", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getAnswersByQuestion(@RequestParam(value="questionId") Integer questionId, 
			@RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {
		
		logger.debug("Start creating response contains answers related to specific question");
		logger.debug("\tReceived questionId = " + questionId);
		
		Integer groupId = getTestService().getGroupByQuestionId(questionId).getGroupId();
		
		JsonResponse response = new JsonResponse();
		
		List<Answer> answers = getTestService().getAllAnswersByQuestionId(questionId);
		Boolean hasAnswers = false;
		
		if(!answers.isEmpty()) { 
			hasAnswers = true; 
		}

		String resultSuccess = "<div class=\"slider\">";
		//resultSuccess += "<div class=\"table-responsive\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<table class=\"table table-bordered warning\" id=\"answer_table_" + questionId + "\" style=\"margin-bottom: 0px\">";
		resultSuccess += "<thead>"
							+ "<tr class=\"warning\">"
								+ "<th>CONTENT OF ANSWER</th>"
								+ "<th>WHETHER CORRECT</th>"
								+ "<th>PICTURE</th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
								+ "<th style=\"border-right-width: 0px;\"></th>"
							+ "</tr>"
						+ "</thead>"
						+ "<tbody>";
		
		for(Answer answer : answers) {
				
			resultSuccess += "<tr class=\"warning\" id=\"answer_" + answer.getAnswerId() + "\">"
								+ "<td>" + answer.getContentOfAnswer() + "</td>"
								+ "<td>" + answer.isWhetherCorrect() + "</td>";
					
								if(answer.getPictures() != null) {
									resultSuccess += "<td style=\"overflow: hidden; padding: 5px;\" align=\"center\"><img src=\"" + getURLWithContextPath(request) + "/tests/image/" + answer.getPictures().getPictureId() + "\" alt=\"answerImage\" name=\"answerImage\" id=\"answerImage\" width=\"100px\" height=\"100px\"/></td>";
								} else {
									resultSuccess += "<td>-</td>";
								}
								
				resultSuccess += "<td>";
						
								if(answer.getPictures() != null) {
									resultSuccess += "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + answer.getContentOfAnswer() + "\" data-picture-src=\"" + getURLWithContextPath(request) + "/tests/image/" + answer.getPictures().getPictureId() + "\" data-whether-correct=\"" + answer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>";
								} else {
									resultSuccess += "<button type=\"button\" class=\"btn btn-info btn-sm\" id=\"editAnswerBtn\" name=\"editAnswerBtn\" data-toggle=\"modal\" data-target=\"#editAnswerModal\" data-content-of-answer=\"" + answer.getContentOfAnswer() + "\" data-picture-src=\"\" data-whether-correct=\"" + answer.isWhetherCorrect() + "\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit answer</button>";
								}
									

				resultSuccess += "</td>"
				 				+ "<td>"
				 					+ "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteAnswerBtn\" name=\"deleteAnswerBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteAnswer\" data-title=\"Delete Answer\" data-message=\"Are you sure you want to delete answer '" + answer.getContentOfAnswer() + "'?\" data-group-reference=\"" + groupId + "\" data-question-reference=\"" + questionId + "\" data-answer-reference=\"" + answer.getAnswerId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
			 					+ "</td>"
								
							+ "</tr>";
		}
		
		resultSuccess += "</tbody>";
		resultSuccess += "</table>";
		//resultSuccess += "</div>";
		resultSuccess += "</div>";
		
		resultSuccess += "<script type=\"text/javascript\">"
				+ "$('#answer_table_" + questionId + "').DataTable( {"
					+ "\"autoWidth\": false,"
					+ "\"paging\": true,"
					+ "\"searching\": true,"
					+ "\"ordering\":  true,"
					+ "\"lengthMenu\": [ [-1], [\"All\"] ],"
					+ "\"pageLength\": -1,"
					+ "\"columns\": ["
						+ "{ \"width\": \"57%\" },"
						+ "{ \"width\": \"20%\" },"
						+ "{ \"orderable\": false, \"width\": \"11%\" },"
						+ "{ \"orderable\": false, \"width\": \"5%\" },"
						+ "{ \"orderable\": false, \"width\": \"7%\" }"
					+ " ],"
					+ "\"language\": {"
						+ "\"info\": \"Showing _START_ to _END_ of _TOTAL_ answers\","
						+ "\"infoEmpty\": \"Showing 0 to 0 of 0 answers\","
						+ "\"infoFiltered\": \"(filtered from _MAX_ total answers)\","
						+ "\"lengthMenu\": \"Show _MENU_ answers\""
					+ "}"
				+ "} );"
				+ "</script>";
		
		if (hasAnswers) {
			response.setStatus("SUCCESS");
			response.setResult(resultSuccess);
			
		} else {
			String resultNoAnswers = "<div class=\"slider\">"
					+ "<div class=\"well\" style=\"margin-bottom: 0px\">"
						+ "There is no answers."
					+ "</div>"
					+ "</div>"; 
			
			response.setStatus("FAIL");
			response.setResult(resultNoAnswers);
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

	/**
	 * Gets StudentGroupsService.
	 *
	 * @return the StudentGroupsService
	 */
	public StudentGroupsService getStudentGroupsService() {
		return studentGroupsService;
	}

	/**
	 * Sets StudentGroupsService.
	 *
	 * @param studentGroupsService the TestService to set
	 */
	public void setStudentGroupsService(StudentGroupsService studentGroupsService) {
		this.studentGroupsService = studentGroupsService;
	}
    /**
     * Gets StudentService.
     *
     * @return the StudentService
     */
    public StudentService getStudentService() {
        return studentService;
    }

    /**
     * Sets StudentService.
     *
     * @param studentService the studentService to set
     */
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }
}
