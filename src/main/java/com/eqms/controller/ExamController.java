package com.eqms.controller;


import com.eqms.model.*;
import com.eqms.service.HistoryService;
import com.eqms.service.StudentService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.web.JsonResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.time.LocalTime;
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
    @Autowired
    private HistoryService historyService;

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
    public String startExam(@RequestParam(value = "testReference") Integer testId,
                                           @RequestParam(value = "groupReference") Integer groupId,
                                           ModelMap model, HttpServletRequest request){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Students currentStudent= getStudentService().getStudentByStudentId(currentStudentId);

        Test test = getTestService().getTestByTestId(testId);
        GroupOfQuestions group = getTestService().getGroupByGroupId(groupId);
        List <Question> questions = getTestService().getAllQuestionsByGroupId(groupId);
        Collections.shuffle(questions);
        Question question=questions.get(0);
        List <Answer> answers = getTestService().getAllAnswersByQuestionId(question.getQuestionId());
        Collections.shuffle(answers);

        String image=new String();
        if(question.getPictures() != null) {
            image = "<img src=\"" + getURLWithContextPath(request) + "/tests/image/" + question.getPictures().getPictureId() + "\" alt=\"questionImage\" name=\"questionImage\" id=\"questionImage\"/></label>";
        } else {
            image = "";
        }

        List<String> questionIDs=new ArrayList<String>();
        for (Question q : questions){
            questionIDs.add(String.valueOf(q.getQuestionId()));
        }

        if(getTestService().getTimeByReference(currentStudentId,groupId)==null) {
            Time currenttime = Time.valueOf(LocalTime.now());
            LocalTime localtime = currenttime.toLocalTime();
            localtime = localtime.plusMinutes(test.getTimeForTest());
            String output = localtime.toString();
            getTestService().saveEmergencyTimeLeftForStudent(currentStudentId, groupId, output);
        }

        model.put("currentTestModel",test);
        model.put("currentGroupModel",group);
        model.put("question",question);
        model.put("questionIDsModel",questionIDs);
        model.put("answersModel", answers);
        model.put("URLwithContextPath",getURLWithContextPath(request));
        model.put("currentStudentModel", currentStudent);
        model.put("image", image);
        model.put("time", getTestService().getTimeByReference(currentStudentId,groupId));


        return "exampage";
    }

    @RequestMapping(value = "/nextQuestion", method = RequestMethod.POST)
    public @ResponseBody JsonResponse nextQuestion(@RequestParam (value = "nextQuestionReference") Integer questionNumber,
                                                  @RequestParam (value = "groupReference") Integer groupId,
                                                   @RequestParam (value="questionIDsReference") List <String> questions,
                                                   HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();



        //List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        Integer lastquestionId=Integer.parseInt(questions.get(questions.size()-1).replace("[","").replace("]",""));
        Boolean isQuestionLast=false;
        Question question=getTestService().getQuestionByQuestionId(Integer.parseInt(questions.get(questionNumber+1).replace("[","").replace("]","")));

        if(question.getQuestionId()==lastquestionId){
            isQuestionLast=true;
        }

        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(Integer.parseInt(questions.get(questionNumber).replace("[","").replace("]","")));

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
                                                       @RequestParam (value="questionIDsReference") List <String> questions,
                                                   HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();






        //List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        Integer firstquestionId=Integer.parseInt(questions.get(0).replace("[","").replace("]",""));
        Boolean isQuestionFirst=false;
        Question question=getTestService().getQuestionByQuestionId(Integer.parseInt(questions.get(questionNumber-1).replace("[","").replace("]","")));

        if(question.getQuestionId()==firstquestionId){
                    isQuestionFirst=true;
        }

        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(Integer.parseInt(questions.get(questionNumber).replace("[","").replace("]","")));

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
                                                     @RequestParam (value="questionIDsReference") List <String> questions,
                                                       HttpServletRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        Integer currentStudentId = getStudentService().getStudentByEmail(currentUserEmail).getStudentId();
        Integer currentExamId = getUserService().findByEmail(currentUserEmail).getConductedExamId();


        //List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);
        String [] choosedAnswers=request.getParameterValues("answer");

        List <Answer> previousAnswers=getTestService().getAllAnswersByQuestionId(Integer.parseInt(questions.get(questionNumber).replace("[","").replace("]","")));

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
        GroupOfQuestions groupOfQuestions=getTestService().getGroupByGroupId(groupId);
        Test test=getTestService().getTestByTestId(testId);

        List <Question> questions=getTestService().getAllQuestionsByGroupId(groupId);

        double result=0;

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

        double mark=2;
        double percentage=(result/groupOfQuestions.getNumberOfQuestions())*100;
        int percentageResult=(int)percentage;

        SetOfRating setOfRating=getTestService().getSetOfRatingBySetId(test.getSetsOfRating().getSetId());

        if(percentageResult>=setOfRating.getMark5()){
            mark=5;
        }
        else if(percentageResult < setOfRating.getMark5() && percentageResult >= setOfRating.getMark4_5()){
            mark=4.5;
        }
        else if(percentageResult < setOfRating.getMark4_5() && percentageResult >= setOfRating.getMark4()){
            mark=4;
        }
        else if(percentageResult <  setOfRating.getMark4() && percentageResult >= setOfRating.getMark3_5()){
            mark=3.5;
        }
        else if(percentageResult < setOfRating.getMark3_5() && percentageResult >=  setOfRating.getMark3()){
            mark=3;
        }
        else{
            mark=2;
        }

        getHistoryService().addExamResult(currentStudentId, mark, (int)result, currentExamId);

        model.put("result",(int)result);
        model.put("test",test);
        model.put("group", groupOfQuestions);
        model.put("percentageResult",percentageResult);
        model.put("mark",mark);

        getTestService().deleteReferenceStudentToGroupOfQuestions(currentStudentId, groupId);


        User currentUser=getUserService().findByEmail(currentUserEmail);
        getUserService().delete(currentUserEmail);


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

    public HistoryService getHistoryService(){return historyService;}
    public void  setHistoryService(HistoryService historyService){this.historyService=historyService;}
}
