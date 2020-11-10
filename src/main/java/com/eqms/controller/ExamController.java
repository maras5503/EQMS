package com.eqms.controller;


import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Question;
import com.eqms.model.Test;
import com.eqms.service.StudentService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exam")
public class ExamController {
    protected static Logger logger = Logger.getLogger("controller");

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TestService testService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getExamPage(Map<String, List<GroupOfQuestions>> map, ModelMap model) {
        logger.debug("Received request to show exam page");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        logger.debug("Address email of current logged user = " + currentUserEmail);

        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentGroupId = getTestService().getGroupOfQuestionsIdbyStudentId(currentStudentId);
        Integer currentTestId = getTestService().getTestIdByGroupId(currentGroupId);

        GroupOfQuestions currentGroup = getTestService().getGroupByGroupId(currentGroupId);
        Test currentTest = getTestService().getTestByTestId(currentTestId);
        List <Question> questions = getTestService().getAllQuestionsByGroupId(currentGroupId);
        Boolean isEnabled = currentTest.isEnabled();

        model.put("currentTestModel",currentTest);
        model.put("currentGroupModel",currentGroup);
        model.put("questionsModel",questions);
        model.put("isTestEnabledModel",isEnabled);


        return "studenthomepage";
    }

    @RequestMapping(value = "/startExam", method = {RequestMethod.GET, RequestMethod.POST})
    public String getGeneratePasswordsPage(@RequestParam(value = "testReference") Integer testId,
                                           @RequestParam(value = "groupReference") Integer groupId,
                                           ModelMap model){

        Test test = getTestService().getTestByTestId(testId);
        GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
        List <Question> questions = getTestService().getAllQuestionsByGroupId(groupId);
        model.put("currentTestModel",test);
        model.put("currentGroupModel",group);
        model.put("questionsModel",questions);

        return "exampage";
    }


    public StudentService getStudentService() {
        return studentService;
    }
    public void setStudentService(StudentService studentService){
        this.studentService=studentService;
    }

    public UserService getUserService(){
        return userService;
    }
    public void setUserService(UserService userService){
        this.userService=userService;
    }

    public TestService getTestService(){
        return testService;
    }
    public void setTestService(TestService testService){
        this.testService=testService;
    }
}
