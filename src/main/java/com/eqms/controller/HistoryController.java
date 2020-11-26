package com.eqms.controller;

import com.eqms.model.ConductedExams;
import com.eqms.model.GroupsOfStudents;
import com.eqms.model.Students;
import com.eqms.service.HistoryService;
import com.eqms.service.StudentGroupsService;
import com.eqms.service.StudentService;
import com.eqms.web.JsonResponse;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("history")
public class HistoryController {
    /** Static logger */
    protected static Logger logger = Logger.getLogger("controller");

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @Autowired
    private HistoryService historyService;

    @Autowired
    private StudentGroupsService studentGroupsService;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getHistoryPage(Map<String, List<ConductedExams>> map, ModelMap model) {
        logger.debug("Received request to show history page");

        model.put("allConductedExams", getHistoryService().getAllConductedExams(Order.asc("conductedExamId"), null));

        return "conductedexamspage";
    }

    @RequestMapping(value = "/deleteConductedExam", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse doDeleteStudentGroup(@RequestParam(value="conductedExamReference") Integer conductedExamId) {

        logger.debug("Start deleting exam from database ...");
        logger.debug("Received request with:");
        logger.debug("\tconductedExamId = " + conductedExamId);

        // Delete group from database
        getHistoryService().deleteConductedExam(conductedExamId);

        getHistoryService().deleteExamResult(conductedExamId);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        //Map<String, Object> deleteSubjectParameters = new HashMap<String, Object>();

        response.setStatus("SUCCESS");

        return response;
    }

    @RequestMapping(value = "/examResults", method = {RequestMethod.POST, RequestMethod.GET})
    public String getExamResultsPage(@RequestParam(value="conductedExamReference") Integer conductedExamId,
                                  @RequestParam(value="studentGroupReference") Integer studentgroupId, ModelMap model) {


        GroupsOfStudents groupsOfStudents=getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId);
        List<Students> students = getStudentService().getStudentsByStudentGroupId(studentgroupId);

        model.put("studentgroup",groupsOfStudents);
        model.put("students",students);
        model.put("conductedexam", getHistoryService().getConductedExamByConductedExamId(conductedExamId));
        model.put("examresults",getHistoryService().getExamResultsByConductedExamId(conductedExamId));
        model.put("exammarks",getHistoryService().getExamMarksByConductedExamId(conductedExamId));




        return "examresultspage";
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService){
        this.historyService=historyService;
    }

    public StudentGroupsService getStudentGroupsService() {
        return studentGroupsService;
    }

    public void setStudentGroupsService(StudentGroupsService studentGroupsService){this.studentGroupsService=studentGroupsService; }

    public StudentService getStudentService() { return studentService; }

    public void setStudentService(StudentService studentService) {  this.studentService = studentService; }

}
