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
import com.eqms.model.GroupsOfStudents;
import com.eqms.service.EmailService;
import com.eqms.service.TestService;
import com.eqms.service.UserService;
import com.eqms.service.StudentGroupsService;
import com.eqms.web.JsonResponse;

@Controller
@RequestMapping("groups_of_students")
public class StudentGroupController {

    /** Static logger */
    protected static Logger logger = Logger.getLogger("controller");

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

        return "studentgroupspage";
    }

    /**
     * Adds new groups to database and creates HTML code for added group, that will be directly inserted in proper place on the JSP.
     *
     * @param studentgroupName the name of group
     * @return JSON response which contains HTML code for last added group
     */
    @RequestMapping(value = "/doAddStudentGroupAjax", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doAddStudentGroup(@RequestParam(value="studentGroupNameModal") String studentgroupName) {

        logger.debug("Start adding new group to database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentGroupName = " + studentgroupName);


        // Create new group
        GroupsOfStudents groupsOfStudents = new GroupsOfStudents();
        groupsOfStudents.setStudentgroupName(studentgroupName);
        // Add new group to database
        getStudentGroupsService().addStudentGroup(groupsOfStudents);

        // Get last group from database
        GroupsOfStudents lastGroupOfStudents = getStudentGroupsService().getAllStudentGroups(Order.desc("studentgroupId"), 1).get(0);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        Map<String, Object> addedStudentGroupRow = new HashMap<String, Object>();
        addedStudentGroupRow.put("studentgroupName", lastGroupOfStudents.getStudentgroupName());
        addedStudentGroupRow.put("editStudentGroup", "<button type=\"button\" class=\"btn btn-info btn-block btn-sm\" id=\"editStudentGroupBtn\" name=\"editStudentGroupBtn\" data-toggle=\"modal\" data-target=\"#editStudentGroupModal\" data-studentgroup-name=\"" + lastGroupOfStudents.getStudentgroupName() + "\" data-studentgroup-reference=\"" + lastGroupOfStudents.getStudentgroupId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
        addedStudentGroupRow.put("deleteStudentGroup", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteStudentGroupBtn\" name=\"deleteStudentGroupBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudentGroup\" data-title=\"Delete Group\" data-message=\"Are you sure you want to delete group '" + lastGroupOfStudents.getStudentgroupName() + "'?\" data-studentgroup-reference=\"" + lastGroupOfStudents.getStudentgroupId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");
        addedStudentGroupRow.put("studentgroupId", lastGroupOfStudents.getStudentgroupId());

        response.setStatus("SUCCESS");
        response.setResult(addedStudentGroupRow);

        return response;
    }

    /**
     * Updates existing group from database and creates HTML code for updated group, that will be directly inserted in proper place on the JSP.
     *
     * @param studentgroupName the name of group
     * @param studentgroupId the group identifier
     * @return JSON response which contains HTML code for updated group
     */
    @RequestMapping(value = "/doEditStudentGroupAjax", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doEditStudentGroup(@RequestParam(value="studentGroupNameModal") String studentgroupName,
                                                    @RequestParam(value="studentGroupReference") Integer studentgroupId) {

        logger.debug("Start editing group from database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentgroupName = " + studentgroupName);
        logger.debug("\tstudentgroupId = " + studentgroupId);

        // Get edited group with default information from database
        GroupsOfStudents groupsOfStudents = getStudentGroupsService().getStudentGroupByStudentGroupId(studentgroupId);
        // Change group name
        groupsOfStudents.setStudentgroupName(studentgroupName);

        // Update group to database
        getStudentGroupsService().updateStudentGroup(groupsOfStudents);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        Map<String, Object> updateStudentGroupParameters = new HashMap<String, Object>();
        updateStudentGroupParameters.put("studentgroupName", groupsOfStudents.getStudentgroupName());
        updateStudentGroupParameters.put("editStudentGroup", "<button type=\"button\" class=\"btn btn-info btn-block btn-sm\" id=\"editStudentGroupBtn\" name=\"editStudentGroupBtn\" data-toggle=\"modal\" data-target=\"#editStudentGroupModal\" data-studentgroup-name=\"" + groupsOfStudents.getStudentgroupName() + "\" data-studentgroup-reference=\"" + groupsOfStudents.getStudentgroupId() + "\"><span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span> Edit</button>");
        updateStudentGroupParameters.put("deleteStudentGroup", "<button type=\"button\" class=\"btn btn-danger btn-block btn-sm\" id=\"deleteStudentGroupBtn\" name=\"deleteStudentGroupBtn\" data-toggle=\"modal\" data-target=\"#confirmDeleteStudentGroup\" data-title=\"Delete Group\" data-message=\"Are you sure you want to delete group '" + groupsOfStudents.getStudentgroupName() + "'?\" data-studentgroup-reference=\"" + groupsOfStudents.getStudentgroupId() + "\"><span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span> Delete</button>");

        response.setStatus("SUCCESS");
        response.setResult(updateStudentGroupParameters);

        return response;
    }

    /**
     * Removes group from database and creates HTML code which updates JSP after removing group.
     *
     * @param studentgroupId the group identifier
     * @return JSON response which contains HTML code which updates JSP after removing group
     */
    @RequestMapping(value = "/doDeleteStudentGroupAjax", method = RequestMethod.POST)
    public @ResponseBody JsonResponse doDeleteStudentGroup(@RequestParam(value="studentGroupReference") Integer studentgroupId) {

        logger.debug("Start deleting group from database ...");
        logger.debug("Received request with:");
        logger.debug("\tstudentgroupId = " + studentgroupId);

        // Delete group from database
        getStudentGroupsService().deleteStudentGroup(studentgroupId);

        // Create JSON response
        JsonResponse response = new JsonResponse();

        //Map<String, Object> deleteSubjectParameters = new HashMap<String, Object>();

        response.setStatus("SUCCESS");

        return response;
    }



    /**
     * Checks if name of group is unique.
     *
     * @param studentgroupName the name of group
     * @param model the model
     * @return ResponseBody which contains success message when group doesn't exists, or fail message when exists
     */
    @RequestMapping(value = "/checkStudentGroupName", method = RequestMethod.POST)
    public @ResponseBody String checkStudentGroupName(@RequestParam(value="studentgroupName") String studentgroupName, ModelMap model) {

        String response = null;

        if(getStudentGroupsService().checkStudentGroupName(studentgroupName) == false) { // group doesn't exist
            response = "SUCCESS";
        } else { // group exist
            response = "FAIL";
        }

        return response;
    }



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

}
