package com.eqms.controller;

import com.eqms.model.ConductedExams;
import com.eqms.service.HistoryService;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getHistoryPage(Map<String, List<ConductedExams>> map, ModelMap model) {
        logger.debug("Received request to show history page");

        model.put("allStudentGroupsModel", getHistoryService().getAllConductedExams(Order.asc("conductedExamId"), null));

        return "conductedexamspage";
    }

    public HistoryService getHistoryService() {
        return historyService;
    }

    public void setHistoryService(HistoryService historyService){
        this.historyService=historyService;
    }


}
