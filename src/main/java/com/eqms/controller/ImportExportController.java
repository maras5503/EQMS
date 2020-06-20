package com.eqms.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Picture;
import com.eqms.model.Question;
import com.eqms.model.SetOfRating;
import com.eqms.model.Subject;
import com.eqms.model.Test;
import com.eqms.model.User;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.web.JsonResponse;

@Controller
@RequestMapping("import_export")
public class ImportExportController {

	/** Static logger */
	protected static Logger logger = Logger.getLogger("controller");

	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;
	
	/**
	 * Gets URL with context path based on HttpServletRequest.
	 * 
	 * @param request the HttpServletRequest
	 * @return the URL with context path
	 */
	public static String getURLWithContextPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	/**
	 * Gets main import/export page.
	 * 
	 * @param model the model
	 * @return logic view name represented by String type
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getImportExport(ModelMap model) {
		
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
		
		/**
		 * Putting all subjects that belong to current logged user
		 */
		model.put("userSubjects", getTestService().getSubjectsByUser(user));
		
		return "importexportpage";
	}
	
	/**
	 * Creates and gets select with all tests assigned to specific subject.
	 * 
	 * @param subjectId the subject identifier
	 * @return JSON response which contains select with tests
	 */
	@RequestMapping(value = "/getTestsBySubjectSelect", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getTestsBySubjectSelect(@RequestParam(value="subjectId") Integer subjectId) {
		
		List<Test> tests = getTestService().getTestsBySubjectId(subjectId);
		
		// Create response that contains tests as options to test select
		JsonResponse response = new JsonResponse();
		
		String testOptions = "<option value=\"\" selected=\"selected\">Select test</option>";
		
		if(!tests.isEmpty()) {
			for(Test test : tests ) {
				testOptions += "<option value=\"" + test.getTestId() + "\">" + test.getTestName() + "</option>";
			}
			
			response.setStatus("SUCCESS");
			response.setResult(testOptions);
		} else {
			response.setStatus("FAIL");
			response.setResult("<option value=\"\" selected=\"selected\">Selected subject doesn't have tests.</option>");
		}
		
		return response;
	}
	
	/**
	 * Creates and gets form with numbers of questions for each group assigned to specific test.
	 * 
	 * @param testId the test identifier
	 * @param csrfToken the CSRF token
	 * @param request the HttpServletRequest
	 * @return JSON response which contains form with numbers of questions
	 */
	@RequestMapping(value = "/getExportFormByTestSelect", method = RequestMethod.POST)
	public @ResponseBody JsonResponse getExportFormByTestSelect(@RequestParam(value="testId") Integer testId,
			@RequestParam(value="_csrf") String csrfToken,
			HttpServletRequest request) {
	
		List<GroupOfQuestions> groupsForCurrentTest = getTestService().getAllGroupsByTestId(testId);
		
		// Create response that contains content for export form for specified test
		JsonResponse response = new JsonResponse();
		
		String formContent = "<form class=\"form-horizontal\" action=\"" + getURLWithContextPath(request) + "/import_export/doExport\" method=\"POST\" id=\"exportForm\">"
							+ "<div class=\"form-group\">"
								+ "<label class=\"control-label col-sm-12\" style=\"text-align: center;\">Choose number of questions for each group</label>"
							+ "</div>";
		
		if(!groupsForCurrentTest.isEmpty()) {
			for(GroupOfQuestions group : groupsForCurrentTest) {
				formContent += "<div class=\"form-group\">"
								+ "<label for=\"numberOfQuestions\" class=\"control-label col-sm-9\" style=\"text-align: left; padding-top: 0px;\">" + group.getGroupName() + " (" + group.getNumberOfQuestions() + " question(s) in total): </label>"
								+ "<div class=\"col-sm-3\">"
									+ "<input type=\"number\" class=\"form-control\" id=\"numberOfQuestions_group_" + group.getGroupId() + "\" name=\"numberOfQuestions\" min=\"0\" max=\"" + group.getNumberOfQuestions() + "\"/>"
								+ "</div>"
						+ "</div>";
			}
			
			formContent += "<input type=\"hidden\" name=\"testReference\" id=\"testReference\" value=\"" + testId + "\" />"
					+ "<input type=\"hidden\" name=\"_csrf\" value=" + csrfToken + " />"
					+ "</form>";
			
			response.setStatus("SUCCESS");
			response.setResult(formContent);
			
		} else {
			
			String resultNoGroups = "<div class=\"col-sm-12\" style=\"margin-bottom: 0px; margin-top: 25px;\">"
					+ "There is no groups."
				+ "</div>"; 
			
			response.setStatus("FAIL");
			response.setResult(resultNoGroups);
		}
		
		return response;
	}
	
	/**
	 * Export test to file on disk.
	 * 
	 * @param testId the test identifier
	 * @param request the HttpServletRequest
	 * @return JSON response which contains success message when test was successfully exported, otherwise fail message with errors
	 */
	@RequestMapping(value = "/doExportAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doExportAjax(@RequestParam(value="testReference") Integer testId,
			HttpServletRequest request) {
		
		logger.debug("Received request with:");
		logger.debug("\ttestId = " + testId);
		
		String [] numbersOfQuestions = request.getParameterValues("numberOfQuestions");
	
		for(int i=0; i<numbersOfQuestions.length; i++) {
			logger.debug("\tnumberOfQuestions = " + numbersOfQuestions[i]);
		}
		
		JsonResponse response = new JsonResponse();
		String responseMessage = "";
		
		Map<String, Object> map = saveTestToFile(testId, numbersOfQuestions);
		Set<String> keys = map.keySet();
		for(String key : keys) {
			responseMessage += map.get(key) + "<br/>";
		}
		
		if(!responseMessage.equals("")) {
			response.setStatus("FAIL");
			response.setResult(responseMessage);
		} else {
			response.setStatus("SUCCESS");
			response.setResult("The file has been successfully created.");
		}
		
		return response;
	}
	
	/**
	 * Saves test to file on disk.
	 * 
	 * @param testId the test identifier
	 * @param numbersOfQuestions the list with numbers of questions for each group assigned to specific test
	 * @return the map which contains validation errors
	 */
	public Map<String, Object> saveTestToFile(Integer testId, String [] numbersOfQuestions) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		final String OUTPUT_DIR_PATH = "D:\\eqms_output_directory\\tests\\";
		final String OUTPUT_DIR_PATH_PICTURES = "D:\\eqms_output_directory\\pictures\\";
		final String EXTENSION_FOR_TEST = ".tg";	// .tg_org
		final String NEW_LINE = "\r\n";
		
		Test test = getTestService().getTestByTestId(testId);
		SetOfRating set = getTestService().getSetOfRatingBySetId(test.getSetsOfRating().getSetId());
		List<GroupOfQuestions> groups = getTestService().getAllGroupsByTestId(testId);
		
		
		// Check if all question from each group contain at least 2 answers
		logger.debug("Start pre-validation before writing test to file");
		Boolean hasErrors = false;
		//grouploop:
		for(GroupOfQuestions group : groups) {
			
			List<Question> questions = getTestService().getAllQuestionsByGroupId(group.getGroupId());
			for(Question question : questions) {
				
				if(question.getNumberOfAnswers() < 2) {
					hasErrors = true;
					map.put("error_question_" + String.valueOf(question.getQuestionId()), "The question of the name '" + question.getContentOfQuestion() + "' for group '" + group.getGroupName() + "' has too few answers.");
					//break grouploop;
				}
			}
		}
		logger.debug("End of pre-validation");
		
		
		if(!hasErrors) {
			logger.debug("Pre-validation has been completed successfully");
			logger.debug("Start writing test to file");
			
			try {
				// Example path -> D:\\eqms_output_directory\\1_JezykSQL.tg_org
				// Example path -> D:\\eqms_output_directory\\test_1.tg_org
				//File file = new File(OUTPUT_DIR_PATH + test.getTestId() + "_" + test.getTestName() + EXTENSION_FOR_TEST);
				File file = new File(OUTPUT_DIR_PATH + "test_" + test.getTestId() + EXTENSION_FOR_TEST);
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
				
				//FileWriter fw = new FileWriter(file.getAbsoluteFile());
				//BufferedWriter bw = new BufferedWriter(fw);
				//bw.append("TestName = \"" + test.getTestName() + "\"\n");												// TestName = "JezykSQL"
				//bw.append("Time = " + test.getTimeForTest() + "\n\n");												// Time = 60

				bw.append("TestName = \"" + test.getTestName() + "\"").append(NEW_LINE);
				bw.append("Time = " + test.getTimeForTest()).append(NEW_LINE).append(NEW_LINE);
				
				bw.append("Mark \"5\"  > " + String.valueOf(set.getMark5()) + "%").append(NEW_LINE);					// Mark "5"  > 90%
				bw.append("Mark \"4+\" > " + String.valueOf(set.getMark4_5()) + "%").append(NEW_LINE);					// Mark "4+" > 80%
				bw.append("Mark \"4\"  > " + String.valueOf(set.getMark4()) + "%").append(NEW_LINE);					// Mark "4"  > 70%
				bw.append("Mark \"3+\" > " + String.valueOf(set.getMark3_5()) + "%").append(NEW_LINE);					// Mark "3+" > 65%
				bw.append("Mark \"3\"  > " + String.valueOf(set.getMark3()) + "%").append(NEW_LINE);					// Mark "3"  > 60%
				bw.append("Mark \"2\"  > " + String.valueOf(set.getMark2()) + "%").append(NEW_LINE).append(NEW_LINE);	// Mark "2"  > 40%
				
				Integer groupCounter = 0;
				for(GroupOfQuestions group : groups) {
					String DASHES = new String(new char[100 - group.getGroupName().length() - 3]).replace("\0", "-");
					bw.append("//---------------------------------------------------------------------------------------------------------------------------------------").append(NEW_LINE);
					bw.append("//--------------------------------   " + group.getGroupName() + "   " + DASHES).append(NEW_LINE);
					bw.append("//---------------------------------------------------------------------------------------------------------------------------------------").append(NEW_LINE);
					bw.append("Group //" + group.getGroupName()).append(NEW_LINE);												// Group //Wprowadzenie	
					bw.append("NumberOfQuestions = " + numbersOfQuestions[groupCounter]).append(NEW_LINE);						// NumberOfQuestions = 2
					bw.append("//AllNumberOfQuestions = " + String.valueOf(group.getNumberOfQuestions())).append(NEW_LINE);		// //AllNumberOfQuestions = 3
					
					// Get all questions for current group
					List<Question> questions = getTestService().getAllQuestionsByGroupId(group.getGroupId());
					
					Integer questionCounter = 1;
					for(Question question : questions) {
						bw.append("Question //" + String.valueOf(questionCounter)).append(NEW_LINE);							// Question //1
						
						if(question.getPictures() != null) {
							// Example path -> D:\\eqms_output_directory\\pictures\\picture_for_question_1.jpg
							Picture questionPicture = getTestService().getPictureByPictureId(question.getPictures().getPictureId());
							String pathToQuestionPicture = OUTPUT_DIR_PATH_PICTURES + "picture_for_question_" + question.getQuestionId() + "." + questionPicture.getPictureType();
		
							savePictureToFile(questionPicture, pathToQuestionPicture);
							
							// Example append ->   Text = "Akronim SQL pochodzi ze slow<IMG SRC=D:\eqms_output_directory\pictures\picture_for_question_1.jpg><BR/>" 
							bw.append("  Text = \"" + question.getContentOfQuestion() + "<IMG SRC=" + pathToQuestionPicture + "><BR/>\"").append(NEW_LINE);
						} else {
							// Example append ->   Text = "Akronim SQL pochodzi ze slow<BR/>"
							bw.append("  Text = \"" + question.getContentOfQuestion() + "<BR/>\"").append(NEW_LINE);
						}
						
						// Get all answers for current question
						List<Answer> answers = getTestService().getAllAnswersByQuestionId(question.getQuestionId());
						
						for(Answer answer : answers) {
							if(answer.isWhetherCorrect()) {	// if answer is true
								// Example append ->   TrueAnswer	= "Standard Query Language
								bw.append("  TrueAnswer\t= \"" + answer.getContentOfAnswer());
							} else {
								// Example append ->   FalseAnswer	= "Standard Query Language
								bw.append("  FalseAnswer\t= \"" + answer.getContentOfAnswer());
							}
							
							if(answer.getPictures() != null) {
								// Example path -> D:\\eqms_output_directory\\pictures\\picture_for_answer_1.jpg
								Picture answerPicture = getTestService().getPictureByPictureId(answer.getPictures().getPictureId());
								String pathToAnswerPicture = OUTPUT_DIR_PATH_PICTURES + "picture_for_answer_" + answer.getAnswerId() + "." + answerPicture.getPictureType();
								
								savePictureToFile(answerPicture, pathToAnswerPicture);
								
								// Example append -> <IMG SRC=D:\eqms_output_directory\pictures\picture_for_answer_1.jpg><BR/>"
								bw.append("<IMG SRC=" + pathToAnswerPicture +"><BR/>\"").append(NEW_LINE);
							} else {
								// Example append -> <BR/>"
								bw.append("<BR/>\"").append(NEW_LINE);
							}
						}
						
						bw.append("EndQuestion").append(NEW_LINE);
						questionCounter++;
					}
					
					bw.append("EndGroup").append(NEW_LINE);
					groupCounter++;
				}
				
				bw.close();
				
				logger.debug("Writing has been ended");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("Pre-validation has been completed with errors");
		}
		
		return map;
	}
	
	/**
	 * Saves picture to file on disk.
	 * 
	 * @param picture the picture to save
	 * @param pathToOutputFile the path to output file with picture
	 */
	public void savePictureToFile(Picture picture, String pathToOutputFile) {
		
		try {
			// Convert byte array back to BufferedImage
			InputStream is = new ByteArrayInputStream(picture.getPicture());
			BufferedImage bufferedImage = ImageIO.read(is);
			
			ImageIO.write(bufferedImage, picture.getPictureType(), new File(pathToOutputFile));
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates and gets preview of imported test. This preview will be directly inserted to proper window modal.
	 * 
	 * @param subjectId the subject identifier
	 * @param inputTestFile the file which contains test
	 * @param request the HttpServletRequest
	 * @return JSON response which contains preview of imported test
	 * @throws IOException the IOExeption
	 */
	@RequestMapping(value = "/doPreviewAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doPreviewAjax(@RequestParam(value="subjectReference") Integer subjectId,
			@RequestParam(value="inputTestFile") MultipartFile inputTestFile,
			HttpServletRequest request) throws IOException {
		
		logger.debug("Received request with:");
		logger.debug("\tsubjectId = " + subjectId);
		logger.debug("\tinputTestFile = " + inputTestFile.getOriginalFilename());
		
		System.out.println("Request encoding = " + request.getCharacterEncoding());
		System.out.println("Request contentType = " + request.getContentType());
		System.out.println("Request contextPath = " + request.getContextPath());
		System.out.println("MultipartFile contentType = " + inputTestFile.getContentType());
		
		if(request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			System.out.println("MultipartRequest encoding = " + multipartRequest.getCharacterEncoding());
			System.out.println("MultipartRequest contentType = " + multipartRequest.getContentType());
		}
		
		//InputStream is = inputTestFile.getInputStream();
		//String contentOfTestFile = IOUtils.toString(is);
		
		byte[] byteArray = inputTestFile.getBytes();
		String contentOfTestFile = new String(byteArray, "UTF-8");
		logger.debug("*********************** " + contentOfTestFile);
	
		Map<String, Object> testAndParsingErrors = readTestFromString(contentOfTestFile, subjectId);
		Test test = (Test) testAndParsingErrors.get("TEST");	// returns test
		printTestDetails(test);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> errors = (Map<String, Object>) testAndParsingErrors.get("PARSING_ERRORS");	// returns map that contains parsing errors
		String importErrors = "Test contains error(s):</BR>";
		
		logger.debug("Printing parsing errors:\n");
		Set<String> keys = errors.keySet();
		for(String key : keys) {
			logger.debug("\t" + errors.get(key));
			importErrors += errors.get(key) + "</BR>";
		}
		
		String modalBody = getTestContentAsModalBody(test, subjectId);
	
		JsonResponse response = new JsonResponse();
		
		Map<String, Object> previewData = new HashMap<String, Object>();
		previewData.put("modalBody", modalBody);
		
		if(errors.isEmpty()) {
			previewData.put("previewMessage", "Test doesn't contains any errors.");
		} else {
			previewData.put("importErrors", importErrors);
		}
		
		response.setResult(previewData);
		
		return response;
	}
	
	/**
	 * Gets test content as modal body.
	 * 
	 * @param test the test
	 * @param subjectId the subject identifier
	 * @return test the content as modal body
	 */
	public String getTestContentAsModalBody(Test test, Integer subjectId) {
		
		/*
		<h4>This test will be added to subject 'Nazwa przedmiotu'</h4>
		<h4>Test name: Nazwa testu</h4>
		<h4>Time for test: 30 min</h4>
		<h4>Number of groups: 1</h4>
		<h4>Number of questions: 1</h4>
		<h4>Mark 2 &nbsp; > 0%</h4>
		<h4>Mark 3 &nbsp; > 10%</h4>
		<h4>Mark 3+ > 20%</h4>
		<h4>Mark 4 &nbsp; > 30%</h4>
		<h4>Mark 4+ > 40%</h4>
		<h4>Mark 5 &nbsp; > 50%</h4>
		<br/>
		<h4>Test contains group(s):</h4>
		<h4 style="text-indent: 1cm;">Group name: Nazwa grupy</h4>
		<h4 style="text-indent: 1cm;">Number of questions: 1</h4>
		<!-- <h4 style="text-indent: 1cm;">This group contains question(s):</h4> -->
		<h4 style="text-indent: 2cm;">Content of question: Nazwa pytania</h4>
		<h4 style="text-indent: 2cm;">Number of answers: 1</h4>
		<h4 style="text-indent: 2cm;">Number of correct answers: 1</h4>
		<!-- <h4 style="text-indent: 2cm;">This question contains answer(s):</h4> -->
		<h4 style="text-indent: 3cm;">Content of answer: Nazwa odpowiedzi</h4>
		<h4 style="text-indent: 3cm;">Whether correct: true</h4>
		*/
		
		Subject subject = getTestService().getSubjectBySubjectId(subjectId);
		
		String modalBody = "<h4>This test will be added to subject '" + subject.getSubjectName() + "'</h4>"
				+ "<h4>Test name: " + test.getTestName() + "</h4>"
				+ "<h4>Time for test: " + test.getTimeForTest() + "</h4>"
				+ "<h4>Number of groups: " + test.getNumberOfGroups() + "</h4>"
				+ "<h4>Number of questions: " + test.getNumberOfQuestions() + "</h4>"
				+ "<h4>Mark 2 &nbsp; > " + test.getSetsOfRating().getMark2() + "%</h4>"
				+ "<h4>Mark 3 &nbsp; > " + test.getSetsOfRating().getMark3() + "%</h4>"
				+ "<h4>Mark 3+ > " + test.getSetsOfRating().getMark3_5() + "%</h4>"
				+ "<h4>Mark 4 &nbsp; > " + test.getSetsOfRating().getMark4() + "%</h4>"
				+ "<h4>Mark 4+ > " + test.getSetsOfRating().getMark4_5() + "%</h4>"
				+ "<h4>Mark 5 &nbsp; > " + test.getSetsOfRating().getMark5() + "%</h4>"
				+ "</br>";
		
		Set<GroupOfQuestions> groups = test.getGroupsOfQuestionses();
		if(!groups.isEmpty()) {
			modalBody += "<h4>Test contains group(s):</h4>";
		}
		
		for(GroupOfQuestions group : groups) {
			modalBody += "<h4 style=\"text-indent: 1cm;\">Group name: " + group.getGroupName() + "</h4>"
					+ "<h4 style=\"text-indent: 1cm;\">Number of questions: " + group.getNumberOfQuestions() + "</h4>";
			
			Set<Question> questions = group.getQuestionses();
			for(Question question : questions) {
				modalBody += "<h4 style=\"text-indent: 2cm;\">Content of question: " + question.getContentOfQuestion() + "</h4>"
						+ "<h4 style=\"text-indent: 2cm;\">Number of answers: " + question.getNumberOfAnswers() + "</h4>"
						+ "<h4 style=\"text-indent: 2cm;\">Number of correct answers: " + question.getNumberOfCorrectAnswers() + "</h4>";
			
				if(question.getPictures() != null) {
					String questionPictureAsBase64 = "data:image/" + question.getPictures().getPictureType() + ";base64," + Base64.getEncoder().encodeToString(question.getPictures().getPicture());
					
					modalBody += "<h4 style=\"text-indent: 2cm;\">Question contains picture:</h4>"
							+ "<h4 style=\"text-indent: 2cm;\">Picture name: " + question.getPictures().getPictureName() + "</h4>"
							+ "<h4 style=\"text-indent: 2cm;\">Picture type: " + question.getPictures().getPictureType() + "</h4>"
							+ "<h4 style=\"text-indent: 2cm;\">Picture:</h4>"
							+ "<center><img src=\"" + questionPictureAsBase64 + "\" height=\"200\" width=\"200\"></center>";
				}
			
				Set<Answer> answers = question.getAnswerses();
				for(Answer answer : answers) {
					modalBody += "<h4 style=\"text-indent: 3cm;\">Content of answer: " + answer.getContentOfAnswer() + "</h4>"
							+ "<h4 style=\"text-indent: 3cm;\">Whether correct: " + answer.isWhetherCorrect() + "</h4>";
				
					if(answer.getPictures() != null) {
						String answerPictureAsBase64 = "data:image/" + answer.getPictures().getPictureType() + ";base64," + Base64.getEncoder().encodeToString(answer.getPictures().getPicture());
						
						modalBody += "<h4 style=\"text-indent: 3cm;\">Answer contains picture:</h4>"
								+ "<h4 style=\"text-indent: 3cm;\">Picture name: " + answer.getPictures().getPictureName() + "</h4>"
								+ "<h4 style=\"text-indent: 3cm;\">Picture type: " + answer.getPictures().getPictureType() + "</h4>"
								+ "<h4 style=\"text-indent: 3cm;\">Picture:</h4>"
								+ "<center><img src=\"" + answerPictureAsBase64 + "\" height=\"200\" width=\"200\"></center>";
					}
				}
			}
		}
		
		return modalBody;
	}
	
	/**
	 * Imports test from file on disk to database. If the validation of test passed successfully, test will be added to database.
	 * 
	 * @param subjectId the subject identifier
	 * @param inputTestFile the file which contains test
	 * @param request the HttpServletRequest
	 * @return JSON response which contains success message when test was successfully added to database, or fail message when imported test contains errors
	 * @throws IOException the IOException
	 */
	@RequestMapping(value = "/doImportAjax", method = RequestMethod.POST)
	public @ResponseBody JsonResponse doImportAjax(@RequestParam(value="subjectReference") Integer subjectId,
			@RequestParam(value="inputTestFile") MultipartFile inputTestFile,
			HttpServletRequest request) throws IOException {
		
		logger.debug("Received request with:");
		logger.debug("\tsubjectId = " + subjectId);
		logger.debug("\tinputTestFile = " + inputTestFile.getOriginalFilename());
		
		InputStream is = inputTestFile.getInputStream();
		//byte [] byteArray = inputTestFile.getBytes();
		
		String contentOfTestFile = IOUtils.toString(is);
		logger.debug("*********************** " + contentOfTestFile);
		
		Map<String, Object> testAndParsingErrors = readTestFromString(contentOfTestFile, subjectId);
		Test test = (Test) testAndParsingErrors.get("TEST");	// returns test
		printTestDetails(test);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> errors = (Map<String, Object>) testAndParsingErrors.get("PARSING_ERRORS");	// returns map that contains parsing errors
		String importErrors = "Test contains error(s):</BR>";
		
		logger.debug("Printing parsing errors:\n");
		Set<String> keys = errors.keySet();
		for(String key : keys) {
			logger.debug("\t" + errors.get(key));
			importErrors += errors.get(key) + "</BR>";
		}
		
		JsonResponse response = new JsonResponse();
		Map<String, Object> importData = new HashMap<String, Object>();
		
		if(errors.isEmpty()) {
			addTestToDatabase(test);
			
			response.setStatus("SUCCESS");
			importData.put("successMessage", "The test has been successfully added.");
		} else {
			response.setStatus("FAIL");
			importData.put("importErrors", importErrors);
		}
		
		response.setResult(importData);
		
		return response;
	}
	
	/**
	 * Reads test from type String and creates new instance of test. Reading is done using regular expressions.<br/>
	 * See: <a href="https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html">https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html</a><br/>
	 * When reading test from type String any errors are saved to the map.
	 * 
	 * @param contentOfTestFile content of file as type String
	 * @param subjectId the subject identifier
	 * @return map which contains test and another map with parsing errors
	 */
	public Map<String, Object> readTestFromString(String contentOfTestFile, Integer subjectId) {
		
		final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|jpeg|png|gif|bmp))$)";
		final String PICTURE_NAME_PATTERN = ".*\\\\(.*?)\\.(jpg|jpeg|png|gif|bmp)$";
		
		Map<String, Object> testAndParsingErrors = new HashMap<String, Object>();
		
		Test test = new Test();
		SetOfRating set = new SetOfRating();
		Set<GroupOfQuestions> groupsSet = new HashSet<GroupOfQuestions>();
		Integer allNumberOfQuestions = 0;
		Map<String, Object> errors = new HashMap<String, Object>();
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserEmail = userDetails.getUsername();
		
		test.setCreationDate(new Date());
		test.setCreatedBy(currentUserEmail);
		
		Subject subject = getTestService().getSubjectBySubjectId(subjectId);
		test.setSubjects(subject);
		
		
		/*
		 * START PARSING TEXT USING REGULAR EXPRESSION
		 * https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
		 */
		
		Matcher matcher = Pattern.compile("TestName\\s=\\s\"(.*?)\"").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("TestName = " + matcher.group(1));
			test.setTestName(matcher.group(1));
			
			if(getTestService().checkTestName(matcher.group(1), subjectId) == true) {
				errors.put("TEST_NAME_EXISTS", "Test name arleady exists in this subject");
			}
		} else {
			errors.put("NO_TEST_NAME", "Test name not found.");
		}
		
		matcher = Pattern.compile("Time\\s=\\s(.*?)\r\n").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Time = " + matcher.group(1));
			test.setTimeForTest(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("NO_TIME", "Time not found.");
		}
		logger.debug("\n");
		
		matcher = Pattern.compile("Mark\\s\"5\"\\s{2}>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 5 = " + matcher.group(1));
			set.setMark5(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_5", "Mark 5 not found.");
		}

		matcher = Pattern.compile("Mark\\s\"4\\+\"\\s>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 4+ = " + matcher.group(1));
			set.setMark4_5(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_4_5", "Mark 4+ not found.");
		}
		
		matcher = Pattern.compile("Mark\\s\"4\"\\s{2}>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 4 = " + matcher.group(1));
			set.setMark4(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_4", "Mark 4 not found.");
		}
		
		matcher = Pattern.compile("Mark\\s\"3\\+\"\\s>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 3+ = " + matcher.group(1));
			set.setMark3_5(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_3_5", "Mark 3+ not found.");
		}
		
		matcher = Pattern.compile("Mark\\s\"3\"\\s{2}>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 3 = " + matcher.group(1));
			set.setMark3(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_3", "Mark 3 not found.");
		}
		
		matcher = Pattern.compile("Mark\\s\"2\"\\s{2}>\\s(.*?)%").matcher(contentOfTestFile);
		if(matcher.find()) {
			logger.debug("Mark 2 = " + matcher.group(1));
			set.setMark2(Integer.valueOf(matcher.group(1)));
		} else {
			errors.put("MARK_2", "Mark 2 not found.");
		}
		
		test.setSetsOfRating(set);
		logger.debug("\n");
		
		// The regular expression . matches any character except a line terminator unless the DOTALL flag is specified.
		matcher = Pattern.compile("(Group\\s*//.*?EndGroup\r\n)", Pattern.DOTALL).matcher(contentOfTestFile);
		Integer groupCounter = 1;
		while(matcher.find()) {
			//logger.debug("Group = " + matcher.group(1) + "\n");
			String groupSequence = matcher.group(1);
			GroupOfQuestions group = new GroupOfQuestions();
			Set<Question> questionsSet = new HashSet<Question>();
			
			// Find group name with this pattern
			Matcher groupMatcher = Pattern.compile("Group\\s*//\\s*(.*?)\r\n").matcher(groupSequence);
			if(groupMatcher.find()) {
				logger.debug("\tGroupName = " + groupMatcher.group(1));
				group.setGroupName(groupMatcher.group(1));
			}
			
			// Find all numbers of question for this group with this pattern
			groupMatcher = Pattern.compile("//AllNumberOfQuestions\\s*=\\s*(.*?)\r\n").matcher(groupSequence);
			if(groupMatcher.find()) {
				logger.debug("\tAllNumberOfQuestions = " + groupMatcher.group(1));
			}
			
			// Find all questions for this group with this pattern
			groupMatcher = Pattern.compile("(Question\\s*//\\s*.*?EndQuestion\r\n)", Pattern.DOTALL).matcher(groupSequence);
			Integer questionCounter = 1;
			while(groupMatcher.find()) {
				//logger.debug("\t\tQuestion = " + groupMatcher.group(1));
				String questionSequence = groupMatcher.group(1);
				Question question = new Question();
				Set<Answer> answersSet = new HashSet<Answer>();
				
				// Find content of question with this pattern
				Matcher questionMatcher = Pattern.compile("Text\\s*=\\s*\"(.*?)(<IMG|<BR/>\"\r\n|<IMG|<BR/>\"\n|\"\r\n\\s{2}|\"\n\\s{2}|\"\r\nEndQuestion|\"\nEndQuestion)", Pattern.DOTALL).matcher(questionSequence);
				if(questionMatcher.find()) {
					logger.debug("\t\tContentOfQuestion = " + questionMatcher.group(1));
					question.setContentOfQuestion(questionMatcher.group(1));
				}
				
				// Find path to question picture with this pattern
				questionMatcher = Pattern.compile("Text\\s*=\\s*\"(.*?)<IMG SRC=(.*?)>", Pattern.DOTALL).matcher(questionSequence);
				if(questionMatcher.find()) {
					logger.debug("\t\tQuestion contains path to picture = " + questionMatcher.group(2));
					String pathToQuestionPicture = questionMatcher.group(2);
					
					// Check if file exists
					File questionPictureFile = new File(pathToQuestionPicture);
					if(questionPictureFile.exists()) {
						logger.debug("\t\tFile exists");
						
						Matcher imageMatcher = Pattern.compile(IMAGE_PATTERN).matcher(pathToQuestionPicture);
						if(imageMatcher.matches()){
							logger.debug("\t\tFile is an image");
							
							String pictureNameForQuestion = null;
							imageMatcher = Pattern.compile(PICTURE_NAME_PATTERN).matcher(pathToQuestionPicture);
							if(imageMatcher.matches()) {
								pictureNameForQuestion = imageMatcher.group(1);
							}
							
							// Convert a File object into an array of bytes using FileInputStream
							byte[] questionPictureByteArray = new byte[(int) questionPictureFile.length()];
							
							try {
								FileInputStream fis = new FileInputStream(questionPictureFile);
								fis.read(questionPictureByteArray);
								fis.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							Picture questionPicture = new Picture();
							questionPicture.setPictureName(pictureNameForQuestion);
							questionPicture.setPicture(questionPictureByteArray);
							questionPicture.setPictureType(FilenameUtils.getExtension(pathToQuestionPicture));
							question.setPictures(questionPicture);
							
						} else {
							logger.debug("\t\tFile isn't an image");
							errors.put("BAD_FILE_UNDER_CURRENT_PATH_" + pathToQuestionPicture, "File exists under path \"" + pathToQuestionPicture + "\", but this is not an image.");
						}
					} else {
						logger.debug("\t\tFile not found");
						errors.put("BAD_PATH_TO_FILE_" + pathToQuestionPicture, "Picture under path \"" + pathToQuestionPicture + "\" not found.");
					}
				}
				
				// Find answers for this question with this pattern
				questionMatcher = Pattern.compile("((TrueAnswer|FalseAnswer)(\\t|\\s+)=\\s*\"(.*?)\")(\r\n\\s{2}|\n\\s{2}|\r\nEndQuestion|\nEndQuestion)", Pattern.DOTALL).matcher(questionSequence);
				Integer answerCounter = 1;
				while(questionMatcher.find()) {
					//logger.debug("\t\t\tAnswer = " + questionMatcher.group(1));
					String answerSequence = questionMatcher.group(1);
					Answer answer = new Answer();
					
					// Find content of answer with this pattern
					Matcher answerMatcher = Pattern.compile("(TrueAnswer|FalseAnswer)(\\t|\\s+)=\\s*\"(.*?)(<IMG|<BR/>\"$|\"$)", Pattern.DOTALL).matcher(answerSequence);
					if(answerMatcher.find()) {
						logger.debug("\t\t\tContentOfAnswer = " + answerMatcher.group(3));
						answer.setContentOfAnswer(answerMatcher.group(3));
					}
					
					if(answerSequence.contains("TrueAnswer")) {
						logger.debug("\t\t\tAnswer is true");
						answer.setWhetherCorrect(true);
					} else if(answerSequence.contains("FalseAnswer")) {
						logger.debug("\t\t\tAnswer is false");
						answer.setWhetherCorrect(false);
					}
					
					// Find path to answer picture with this pattern
					answerMatcher = Pattern.compile("<IMG SRC=(.*?)>", Pattern.DOTALL).matcher(answerSequence);
					if(answerMatcher.find()) {
						logger.debug("\t\t\tAnswer contains path to picture = " + answerMatcher.group(1));
						String pathToAnswerPicture = answerMatcher.group(1);
						
						// Check if file exists
						File answerPictureFile = new File(pathToAnswerPicture);
						if(answerPictureFile.exists()) {
							logger.debug("\t\t\tFile exists");
							
							Matcher imageMatcher = Pattern.compile(IMAGE_PATTERN).matcher(pathToAnswerPicture);
							if(imageMatcher.matches()){
								logger.debug("\t\t\tFile is an image");
								
								String pictureNameForAnswer = null;
								imageMatcher = Pattern.compile(PICTURE_NAME_PATTERN).matcher(pathToAnswerPicture);
								if(imageMatcher.matches()) {
									pictureNameForAnswer = imageMatcher.group(1);
								}
								
								// Convert a File object into an array of bytes using FileInputStream
								byte[] answerPictureByteArray = new byte[(int) answerPictureFile.length()];
								
								try {
									FileInputStream fis = new FileInputStream(answerPictureFile);
									fis.read(answerPictureByteArray);
									fis.close();
								} catch (IOException e) {
									e.printStackTrace();
								}

								Picture answerPicture = new Picture();
								answerPicture.setPictureName(pictureNameForAnswer);
								answerPicture.setPicture(answerPictureByteArray);
								answerPicture.setPictureType(FilenameUtils.getExtension(pathToAnswerPicture));
								answer.setPictures(answerPicture);
								
							} else {
								logger.debug("\t\t\tFile isn't an image");
								errors.put("BAD_FILE_UNDER_CURRENT_PATH_" + pathToAnswerPicture, "File exists under path \"" + pathToAnswerPicture + "\", but this is not an image.");
							}
						} else {
							logger.debug("\t\t\tFile not found");
							errors.put("BAD_PATH_TO_FILE_" + pathToAnswerPicture, "Picture under path \"" + pathToAnswerPicture + "\" not found.");
						}
						
						logger.debug("\n");
					}
					
					answersSet.add(answer);
					answerCounter++;
				}
				
				question.setNumberOfAnswers(answersSet.size());
				
				// Check number of correct answers from answersSet
				Integer numberOfCorrectAnswer = 0;
				for(Answer answer : answersSet) {
					if(answer.isWhetherCorrect()) numberOfCorrectAnswer++;
				}
				
				question.setNumberOfCorrectAnswers(numberOfCorrectAnswer);
				question.setAnswerses(answersSet);
				questionsSet.add(question);
				questionCounter++;
				
				logger.debug("\n");
			}
			
			allNumberOfQuestions = allNumberOfQuestions + questionsSet.size();
			group.setNumberOfQuestions(questionsSet.size());
			group.setQuestionses(questionsSet);
			groupsSet.add(group);
			groupCounter++;
			
			logger.debug("\n");
		} 
	
		test.setNumberOfGroups(groupsSet.size());
		test.setNumberOfQuestions(allNumberOfQuestions);
		test.setGroupsOfQuestionses(groupsSet);
		
		testAndParsingErrors.put("TEST", test);
		testAndParsingErrors.put("PARSING_ERRORS", errors);
		
		return testAndParsingErrors;
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
	 * Adds test to database.
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

	/*public String convertInputStreamToString(InputStream is) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		try {
			String line;
			
			while((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}*/

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
