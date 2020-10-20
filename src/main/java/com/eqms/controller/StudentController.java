package com.eqms.controller;

import com.eqms.model.GroupsOfStudents;
import com.eqms.model.Students;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("students")
public class StudentController {

    /** Static logger */
    protected static Logger logger = Logger.getLogger("controller");

    public static String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentGroupsService studentGroupsService;

    /**
     * Gets main groups page.
     *
     * @param map the map
     * @param model the model
     * @return logic view name represented by String type
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getStudentGroupsPage(Map<String, List<GroupsOfStudents>> map, ModelMap model) {
        logger.debug("Received request to show groups page");

        model.put("allStudentGroupsModel", getStudentGroupsService().getAllStudentGroups(Order.asc("studentgroupId"), null));

        return "studentspage";
    }

    /**
     * Adds new groups to database and creates HTML code for added group, that will be directly inserted in proper place on the JSP.
     *
     * @param studentFirstname the name of group
     * @return JSON response which contains HTML code for last added group
     */
    @RequestMapping(value = "/doAddStudentAjax", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse doAddStudent(@RequestParam(value="studentFirstnameModal") String studentFirstname, @RequestParam(value = "studentLastnameModal")
            String studentLastname, @RequestParam(value = "studentEmailModal") String studentEmail
                                ,@RequestParam(value="studentgroupId") Integer studentgroupId) {

        logger.debug("Start adding new group to database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentGroupName = " + studentFirstname);


        // Create new student
        Students students = new Students();
        students.setStudentFirstname(studentFirstname);
        students.setStudentLastname(studentLastname);
        students.setStudentEmail(studentEmail);
        GroupsOfStudents groupsOfStudents=getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId);
        students.setGroupsOfStudents(groupsOfStudents);
        // Add new student to database
        getStudentService().addStudent(students);

        // Get last student from database
        Students lastStudent = getStudentService().getAllStudents(Order.desc("studentId"), 1).get(0);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        Map<String, Object> addedStudentRow = new HashMap<String, Object>();
        addedStudentRow.put("studentFirstname", lastStudent.getStudentFirstname());
        addedStudentRow.put("studentLastname", lastStudent.getStudentLastname());
        addedStudentRow.put("studentEmail", lastStudent.getStudentEmail());
        addedStudentRow.put("editStudent", "<button type=\"button\" class=\"btn btn-info btn-block btn-sm\" id=\"editStudentBtn\" name=\"editStudentBtn\" data-toggle=\"modal\" data-target=\"#editStudentModal\" data-student-name=\"" + lastStudent.getStudentFirstname() + "\" data-student-reference=\"" + lastStudent.getStudentId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
        addedStudentRow.put("deleteStudent", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteStudentBtn\" name=\"deleteStudentBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudent\" data-title=\"Delete Student\" data-message=\"Are you sure you want to delete student '" + lastStudent.getStudentFirstname() + "'?\" data-student-reference=\"" + lastStudent.getStudentId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
        addedStudentRow.put("studentgroupId", lastStudent.getGroupsOfStudents().getStudentgroupId());

        response.setStatus("SUCCESS");
        response.setResult(addedStudentRow);

        return response;
    }

    /**
     * Gets all test by specific subject.
     *
     * @param studentgroupId the subject identifier
     * @param csrfToken the CSRF token
     * @param request the HttpServletRequest
     * @return JSON response which contains table with tests as HTML code
     */
    @RequestMapping(value = "/getStudentsByStudentGroup", method = RequestMethod.POST)
    public @ResponseBody JsonResponse getStudentsByStudentGroup(@RequestParam(value="studentgroupId") Integer studentgroupId,
                                                                @RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {

        JsonResponse response = new JsonResponse();

        List<Students> students = getStudentService().getStudentsByStudentGroupId(studentgroupId);

        if(students.isEmpty()) {	// If list is empty returns comment "There are no tests", otherwise returns table with retrieved data

            String resultEmptyList = "<div class=\"well\">"
                    + "There is no students."
                    + "</div>";

            response.setStatus("SUCCESS");
            response.setResult(resultEmptyList);
        } else {

            String resultSuccess = "<div class=\"table-responsive\" style=\"margin-bottom: 0px\">"
                    + "<table class=\"table table-bordered\" id=\"students_table\">"
                    + "<thead>"
                    + "<tr class=\"success\">"
                    + "<th style=\"width: 30%;\">FIRSTNAME</th>"
                    + "<th>LASTNAME</th>"
                    + "<th>E-MAIL</th>"
                    + "<th style=\"border-right-width: 0px;\"></th>"
                    + "<th style=\"border-right-width: 0px;\"></th>"
                    + "</tr>"
                    + "</thead>"
                    + "<tbody>";

            for(Students student : students) {

                resultSuccess += "<tr class=\"success\" data-key=\"" + student.getStudentId() + "\">";
                resultSuccess += "<td>" + student.getStudentFirstname() + "</td>"
                        + "<td>" + student.getStudentLastname() + "</td>"
                        + "<td>" + student.getStudentEmail() + "</td>"

                        + "<td>"
                        + "<form action=\"" + getURLWithContextPath(request) + "/students/editStudent\" method=\"post\" id=\"editStudentForm\">"
                        + "<input type=\"hidden\" name=\"studentId\" value=\"" + student.getStudentId() + "\" />"
                        + "<input type=\"hidden\" name=\"studentgroupId\" value=\"" + studentgroupId + "\" />"
                        + "<button type=\"submit\" id=\"editStudentBtn\" name=\"editStudentBtn\" class=\"btn btn-info btn-block btn-sm\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>"
                        + "<input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken + "\" />"
                        + "</form>"
                        + "</td>"

                        + "<td>"
                        + "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteStudentBtn\" name=\"deleteStudentBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudent\" data-title=\"Delete Student\" data-message=\"Are you sure you want to delete student '" + student.getStudentFirstname() + " " + student.getStudentLastname() + "'?\" data-student-reference=\"" + student.getStudentId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
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


    public StudentService getStudentService() {
        return studentService;
    }
    public void setStudentService(StudentService studentService){
        this.studentService=studentService;
    }

    public StudentGroupsService getStudentGroupsService(){
        return studentGroupsService;
    }
    public void setStudentGroupsService(StudentGroupsService studentGroupsService){
        this.studentGroupsService=studentGroupsService;
    }
}
