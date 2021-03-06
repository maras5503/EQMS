package com.eqms.controller;

import com.eqms.model.GroupsOfStudents;
import com.eqms.model.Students;
import com.eqms.service.StudentGroupsService;
import com.eqms.service.StudentService;
import com.eqms.web.JsonResponse;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
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
    public String getStudentsPage(Map<String, List<GroupsOfStudents>> map, ModelMap model) {
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
    public @ResponseBody JsonResponse doAddStudent(@RequestParam(value="studentFirstnameModal") String studentFirstname,
                                                   @RequestParam(value = "studentLastnameModal") String studentLastname,
                                                   @RequestParam(value = "studentEmailModal") String studentEmail,
                                                   @RequestParam(value = "studentGroupReferenceModal") Integer studentgroupId,
                                                   @RequestParam(value="_csrf") String csrfToken, HttpServletRequest request) {

        logger.debug("Start adding new group to database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentFirstame = " + studentFirstname);

        List<Students> studentsList = getStudentService().getStudentsByStudentGroupId(studentgroupId);
        Boolean listIsEmpty=false;

        if (studentsList.isEmpty()){
            listIsEmpty=true;
        }

        // Create new group
        Students students= new Students();
        students.setStudentFirstname(studentFirstname);
        students.setStudentLastname(studentLastname);
        students.setStudentEmail(studentEmail);
        GroupsOfStudents groupsOfStudents=getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId);
        students.setGroupsOfStudents(groupsOfStudents);
        // Add new group to database
        getStudentService().addStudent(students);

        // Get last group from database
        Students lastStudent = getStudentService().getAllStudents(Order.desc("studentId"), 1).get(0);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        Map<String, Object> addedStudentRow = new HashMap<String, Object>();
        addedStudentRow.put("studentFirstname", lastStudent.getStudentFirstname());
        addedStudentRow.put("studentLastname", lastStudent.getStudentLastname());
        addedStudentRow.put("studentEmail", lastStudent.getStudentEmail());
        addedStudentRow.put("editStudent", "<button type=\"button\" id=\"editStudentBtn\" name=\"editStudentBtn\" class=\"btn btn-info btn-block btn-sm\" style=\"width:90%\" data-target=\"#editStudentModal\" data-student-firstname=\"" + lastStudent.getStudentFirstname() + "\" data-student-lastname=\"" + lastStudent.getStudentFirstname() + "\" data-student-email=\"" + lastStudent.getStudentEmail() + "\"  data-student-reference=\"" + lastStudent.getStudentId() + "\" ><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
        addedStudentRow.put("deleteStudent", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteStudentBtn\" name=\"deleteStudentBtn\" style=\"width:90%\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudent\" data-title=\"Delete Student\" data-message=\"Are you sure you want to delete student '" + lastStudent.getStudentFirstname() + "'  '" + lastStudent.getStudentLastname() + "'?\" data-student-reference=\"" + lastStudent.getStudentId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
        addedStudentRow.put("studentgroupId", lastStudent.getGroupsOfStudents().getStudentgroupId());
        addedStudentRow.put("studentId", lastStudent.getStudentId());
        addedStudentRow.put("studentListIsEmpty", listIsEmpty);

        response.setStatus("SUCCESS");
        response.setResult(addedStudentRow);

        return response;
    }



    /**
     * Adds new groups to database and creates HTML code for added group, that will be directly inserted in proper place on the JSP.
     *
     * @param studentFirstname the name of group
     * @return JSON response which contains HTML code for last added group
     */
    @RequestMapping(value = "/doEditStudentAjax", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doEditStudent(@RequestParam(value="studentFirstnameModal") String studentFirstname,
                                                   @RequestParam(value = "studentLastnameModal") String studentLastname,
                                                   @RequestParam(value = "studentEmailModal") String studentEmail,
                                                   @RequestParam(value = "studentReference") Integer studentId) {

        logger.debug("Start adding new group to database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentFirstame = " + studentFirstname);


        // Create new group
        Students students= getStudentService().getStudentByStudentId(studentId);
        students.setStudentFirstname(studentFirstname);
        students.setStudentLastname(studentLastname);
        students.setStudentEmail(studentEmail);
        // Add new group to database
        getStudentService().updateStudent(students);

        System.out.println(students.getStudentFirstname());

        // Create JSON response
        JsonResponse response = new JsonResponse();

        Map<String, Object> updateStudentParameters = new HashMap<String, Object>();
        updateStudentParameters.put("studentFirstname", students.getStudentFirstname());
        updateStudentParameters.put("studentLastname", students.getStudentLastname());
        updateStudentParameters.put("studentEmail", students.getStudentEmail());
        updateStudentParameters.put("editStudent", "<button type=\"button\" id=\"editStudentBtn\" name=\"editStudentBtn\" class=\"btn btn-info btn-block btn-sm\" style=\"width:90%\"  data-toggle=\"modal\" data-target=\"#editStudentModal\" data-student-firstname=\"" + students.getStudentFirstname() + "\" data-student-lastname=\"" + students.getStudentLastname() + "\" data-student-email=\"" + students.getStudentEmail() + "\"  data-student-reference=\"" + students.getStudentId() + "\" ><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
        updateStudentParameters.put("deleteStudent", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteStudentBtn\" name=\"deleteStudentBtn\" style=\"width:90%\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudent\" data-title=\"Delete Student\" data-message=\"Are you sure you want to delete student '" + students.getStudentFirstname() + "'  '" + students.getStudentLastname() + "'?\" data-student-reference=\"" + students.getStudentId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");

        response.setStatus("SUCCESS");
        response.setResult(updateStudentParameters);

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

                resultSuccess += "<tr class=\"success\" id=\"" + student.getStudentId() + "\">";
                resultSuccess += "<td>" + student.getStudentFirstname() + "</td>"
                        + "<td>" + student.getStudentLastname() + "</td>"
                        + "<td>" + student.getStudentEmail() + "</td>"
                        + "<td>"
                        + "<button type=\"button\" id=\"editStudentBtn\" name=\"editStudentBtn\" class=\"btn btn-info btn-block btn-sm\" style=\"width:90%\" data-toggle=\"modal\" data-target=\"#editStudentModal\" data-student-firstname=\"" + student.getStudentFirstname() + "\" data-student-lastname=\"" + student.getStudentFirstname() + "\" data-student-email=\"" + student.getStudentEmail() + "\"  data-student-reference=\"" + student.getStudentId() + "\" ><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>"
                        + "</td>"
                        + "<td>"
                        + "<button type=\"button\" class=\"btn btn-danger btn-sm\" id=\"deleteStudentBtn\" name=\"deleteStudentBtn\" style=\"width:90%\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudent\" data-title=\"Delete Student\" data-message=\"Are you sure you want to delete student '" + student.getStudentFirstname() + " " + student.getStudentLastname() + "'?\" data-student-reference=\"" + student.getStudentId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>"
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
     * Removes group from database and creates HTML code which updates JSP after removing group.
     *
     * @param studentId the group identifier
     * @return JSON response which contains HTML code which updates JSP after removing group
     */
    @RequestMapping(value = "/doDeleteStudentAjax", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doDeleteStudent(@RequestParam(value="studentReference") Integer studentId) {

        logger.debug("Start deleting student from database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentId = " + studentId);

        // Delete student from database
        getStudentService().deleteStudent(studentId);

        // Create JSON response
        JsonResponse response = new JsonResponse();


        response.setStatus("SUCCESS");

        return response;
    }


    /**
     * Checks if name of group is unique.
     *
     * @param studentEmail the name of group
     * @param model the model
     * @return ResponseBody which contains success message when group doesn't exists, or fail message when exists
     */
    @RequestMapping(value = "/checkStudentEmail", method = RequestMethod.POST)
    public @ResponseBody String checkStudentEmail(@RequestParam(value="studentEmail") String studentEmail,
                                                  @RequestParam(value = "studentId",required = false) Integer studentId,
                                                  ModelMap model) {

        String response = null;

        if(studentId != null && getStudentService().getStudentByStudentId(studentId).getStudentEmail().toString().toLowerCase().equals(studentEmail.toLowerCase())) {
            response = "SUCCESS";
        }

        if(response==null) {
            if (getStudentService().checkStudentEmail(studentEmail) == false) { // group doesn't exist
                response = "SUCCESS";
            } else { // group exist
                response = "FAIL";
            }
        }


        return response;
    }

    @RequestMapping(value="/importStudents", method=RequestMethod.POST)
    public @ResponseBody JsonResponse importStudents(@RequestParam MultipartFile file,
                              @RequestParam(value = "studentGroupReference") Integer studentgroupId) throws IOException {


        Reader reader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).build();


        try {
            String[] lineInArray;
            while ((lineInArray = csvReader.readNext()) != null) {
                Students student= new Students();
                student.setStudentFirstname(lineInArray[2]);
                student.setStudentLastname(lineInArray[1]);
                student.setStudentEmail(lineInArray[3]);
                GroupsOfStudents groupsOfStudents=getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId);
                student.setGroupsOfStudents(groupsOfStudents);
                getStudentService().addStudent(student);
            }
        } catch (CsvException e) {
            e.printStackTrace();
        }

        JsonResponse response = new JsonResponse();


        response.setStatus("SUCCESS");
        response.setResult(studentgroupId);

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
