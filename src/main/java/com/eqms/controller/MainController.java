package com.eqms.controller;

import javax.servlet.http.HttpServletRequest;

import com.eqms.model.User;
import com.eqms.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main")
public class MainController {

	@Autowired
	UserService userService;
	protected static Logger logger = Logger.getLogger("controller");
	
	/**
	 * Gets common page.
	 * 
	 * @return logic view name represented by String type
	 */
    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public String getCommonPage() {
    	logger.debug("Received request to show common page");
    
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	logger.debug(userDetails.getUsername());
    	logger.debug(userDetails.getPassword());
    	logger.debug(userDetails.getAuthorities());
    	
    	// This will resolve to /WEB-INF/jsp/commonpage.jsp
    	return "commonpage";
	}
    
    /**
     * Gets main administrator page.
     * 
     * @return logic view name represented by String type
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage() {
    	logger.debug("Received request to show admin page");
    
    	return "adminpage";
	}
    
    /**
     * Gets welcome page.
     * 
     * @return logic view name represented by String type
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String getWelcomePage() {
    	logger.debug("Received request to show welcome page");
    	
    	return "welcomepage";
    }


    /**
     * Gets default page.
     * 
     * @param request the HttpServletRequest
     * @return logic view name represented by String type
     */
    @RequestMapping(value = "/main/default", method = RequestMethod.GET)
    public String getDefaultPage(HttpServletRequest request) {
    	String userEmail=request.getUserPrincipal().getName();
		User user=getUserService().findByEmail(userEmail);
		Integer userRole=user.getUserRoles().getRoleId();
    	if (userRole==1) {
            return "redirect:/main/admin";
        }

    	if(userRole==2){
			return "redirect:/main/common";
		}
    	if(userRole==3){
    	    return "redirect:/exam/index";
        }
    	return "redirect:/auth/denied";
    }

	public UserService getUserService() {
		return userService;
	}
}
