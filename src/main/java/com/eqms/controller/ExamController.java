package com.eqms.controller;


import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Test;
import com.eqms.service.StudentService;
import com.eqms.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getExamPage(Map<String, List<GroupOfQuestions>> map, ModelMap model) {
        logger.debug("Received request to show exam page");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        logger.debug("Address email of current logged user = " + currentUserEmail);

        Integer currentStudentId=getStudentService().getStudentByEmail(currentUserEmail).getStudentId();

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
}
