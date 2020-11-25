package com.eqms.controller;


import com.eqms.model.*;
import com.eqms.service.StudentService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.web.JsonResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/exam")
public class ExamController {
    protected static Logger logger = Logger.getLogger("controller");

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
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
                                           ModelMap model, HttpServletRequest request){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Students currentStudent= getStudentService().getStudentByStudentId(currentStudentId);

        Test test = getTestService().getTestByTestId(testId);
        GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
        List <Question> questions = getTestService().getAllQuestionsByGroupId(groupId);
        Question question=questions.get(0);
        List <Answer> answers = getTestService().getAllAnswersByQuestionId(question.getQuestionId());

        String image=new String();
        if(question.getPictures() != null) {
            image = "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label>";
        } else {
            image = "";
        }


        model.put("currentTestModel",test);
        model.put("currentGroupModel",group);
        model.put("question",question);
        model.put("questionsModel",questions);
        model.put("answersModel", answers);
        model.put("URLwithContextPath",getURLWithContextPath(request));
        model.put("currentStudentModel", currentStudent);
        model.put("image", image);


        return "exampage";
    }

    @RequestMapping(value = "/nextQuestion", method = RequestMethod.POST)
    public @ResponseBody JsonResponse nextQuestion(@RequestParam (value = "nextQuestionReference") Integer questionNumber,
                                                  @RequestParam (value = "groupReference") Integer groupId,
                                                   HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();



        List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        Integer lastquestionId=questions.get(questions.size()-1).getQuestionId();
        Boolean isQuestionLast=false;
        Question question=new Question();
        question=questions.get(questionNumber+1);

        if(question.getQuestionId()==lastquestionId){
            isQuestionLast=true;
        }

        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(questions.get(questionNumber).getQuestionId());

        for(Answer a:previousAnswers){
            getTestService().deleteReferenceStudentToAnswers(currentStudentId,a.getAnswerId(),currentExamId);
        }

        if(choosedAnswers!=null){
            for (String a : choosedAnswers) {
                Integer answerId = getTestService().getAnswerByAnswerId(Integer.parseInt(a)).getAnswerId();
                getTestService().addReferenceStudentToAnswers(currentStudentId, answerId, currentExamId);
            }
        }


        List <Answer> answers=getTestService().getAllAnswersByQuestionId(question.getQuestionId());
        String resultsuccess=new String();

        for ( Answer a : answers){
            if(getTestService().checkIfAnswerIsChoosedByStudent(currentStudentId,a.getAnswerId(),currentExamId)){
                resultsuccess += "<label class=\"container\">" +
                        "<input type=\"checkbox\" id=\"answerReference\" name=\"answer\" value=\"" + a.getAnswerId() + "\" checked> " + a.getContentOfAnswer() +
                        "</label>";
            }
            else {
                resultsuccess += "<label class=\"container\">" +
                        "<input type=\"checkbox\" id=\"answerReference\" name=\"answer\" value=\"" + a.getAnswerId() + "\" > " + a.getContentOfAnswer() +
                        "</label>";
            }
            if(a.getPictures() != null){
                resultsuccess+="<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + a.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label><br><br>";
            }
        }

        String image=new String();
        if(question.getPictures() != null) {
            image = "<div align=\"center\" id=\"questionImageDiv\">" +
                    "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label><br><br>" +
                    "</div>";
        } else {
            image = "<div align=\"center\" id=\"questionImageDiv\"><div>";
        }

        JsonResponse response=new JsonResponse();
        Map<String, Object> nextQuestion = new HashMap<String, Object>();
        nextQuestion.put("contentOfQuestion",question.getContentOfQuestion());
        nextQuestion.put("questionReference",questionNumber+1);
        nextQuestion.put("resultsuccess",resultsuccess);
        nextQuestion.put("isQuestionLast",isQuestionLast);
        nextQuestion.put("image",image);

        response.setStatus("SUCCESS");
        response.setResult(nextQuestion);
        return response;
    }

    @RequestMapping(value = "/previousQuestion", method = RequestMethod.POST)
    public @ResponseBody JsonResponse previousQuestion(@RequestParam (value = "previousQuestionReference") Integer questionNumber,
                                                   @RequestParam (value = "groupReference") Integer groupId,
                                                   HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();






        List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        Integer firstquestionId=questions.get(0).getQuestionId();
        Boolean isQuestionFirst=false;
        Question question=questions.get(questionNumber-1);

        if(question.getQuestionId()==firstquestionId){
                    isQuestionFirst=true;
        }

        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(questions.get(questionNumber).getQuestionId());

            for(Answer a:previousAnswers){
                getTestService().deleteReferenceStudentToAnswers(currentStudentId,a.getAnswerId(), currentExamId);
            }

        if(choosedAnswers!=null){
            for (String a : choosedAnswers) {
                Integer answerId = getTestService().getAnswerByAnswerId(Integer.parseInt(a)).getAnswerId();
                getTestService().addReferenceStudentToAnswers(currentStudentId, answerId, currentExamId);
            }
        }

        List <Answer> answers=getTestService().getAllAnswersByQuestionId(question.getQuestionId());
        String resultsuccess=new String();
        String nextQuestionReference="<input type=\"hidden\" name=\"nextQuestionReference\" id=\"nextQuestionReference\" value=\""+ question.getQuestionId() +"\"/>";
        String previosQuestionReference="<input type=\"hidden\" name=\"previousQuestionReference\" id=\"previousQuestionReference\" value=\""+ question.getQuestionId() +"\"/>";
        for ( Answer a : answers){
            if(getTestService().checkIfAnswerIsChoosedByStudent(currentStudentId,a.getAnswerId(),currentExamId)){
                resultsuccess += "<label class=\"container\">" +
                        "<input type=\"checkbox\" id=\"answerReference\" name=\"answer\" value=\"" + a.getAnswerId() + "\" checked> " + a.getContentOfAnswer() +
                        "</label>";
            }
            else {
                resultsuccess += "<label class=\"container\">" +
                        "<input type=\"checkbox\" id=\"answerReference\" name=\"answer\" value=\"" + a.getAnswerId() + "\" > " + a.getContentOfAnswer() +
                        "</label>";
            }
            if(a.getPictures() != null){
                resultsuccess+="<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + a.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label><br><br>";
            }
        }

        String image=new String();
        if(question.getPictures() != null) {
            image = "<div align=\"center\" id=\"questionImageDiv\">" +
                    "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label><br><br>" +
                    "</div>";
        } else {
            image = "<div align=\"center\" id=\"questionImageDiv\"><div>";
        }

        JsonResponse response=new JsonResponse();
        Map<String, Object> nextQuestion = new HashMap<String, Object>();
        nextQuestion.put("question",question.getContentOfQuestion());
        nextQuestion.put("questionReference",questionNumber-1);
        nextQuestion.put("resultsuccess",resultsuccess);
        nextQuestion.put("isQuestionFirst",isQuestionFirst);
        nextQuestion.put("image",image);

        response.setStatus("SUCCESS");
        response.setResult(nextQuestion);
        return response;
    }

    @RequestMapping(value = "/saveLastAnswer", method = RequestMethod.POST)
    public @ResponseBody JsonResponse saveLastAnswer(@RequestParam (value = "previousQuestionReference") Integer questionNumber,
                                                       @RequestParam (value = "groupReference") Integer groupId,
                                                       HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();






        List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(questions.get(questionNumber).getQuestionId());

        for(Answer a:previousAnswers){
            getTestService().deleteReferenceStudentToAnswers(currentStudentId,a.getAnswerId(), currentExamId);
        }

        if(choosedAnswers!=null){
            for (String a : choosedAnswers) {
                Integer answerId = getTestService().getAnswerByAnswerId(Integer.parseInt(a)).getAnswerId();
                getTestService().addReferenceStudentToAnswers(currentStudentId, answerId, currentExamId);
            }
        }



        JsonResponse response=new JsonResponse();
        response.setStatus("SUCCESS");
        response.setResult("SUCCESS");
        return response;
    }


    @RequestMapping(value = "/finishExam", method = {RequestMethod.GET})
    public String finishExam(@RequestParam (value = "groupReference") Integer groupId,
                              ModelMap model,
                              HttpServletRequest request) throws Exception {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();

        Integer testId = getTestService().getTestIdByGroupId(groupId);
        Test test=getTestService().getTestByTestId(testId);

        List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);

        Integer result=0;

        for(Question q : questions){
            List<Answer> answers=getTestService().getAllAnswersByQuestionId(q.getQuestionId());
            result+=1;
            for(Answer a : answers){
                if (a.isWhetherCorrect() && getTestService().checkIfAnswerIsChoosedByStudent(currentStudentId,a.getAnswerId(),currentExamId)
                    || !a.isWhetherCorrect() && !getTestService().checkIfAnswerIsChoosedByStudent(currentStudentId,a.getAnswerId(),currentExamId)){
                    continue;
                }
                else {
                    result-=1;
                    break;
                }

            }


        }

        model.put("result",result);
        model.put("test",test);




        return "finishexampage";
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
