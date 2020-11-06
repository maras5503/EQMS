package com.eqms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * A custom service for retrieving users from a custom datasource, such as a database.
 * <p>
 * This custom service must implement Spring's {@link UserDetailsService}
 */
public class CustomUserDetailsService implements UserDetailsService {
	
	protected static Logger logger = Logger.getLogger("service");
	
	private UserService userService;

	/**
	 * Retrieves a user record containing the user's credentials and access. 
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.debug("E_MAIL = " + email);
		System.out.println("E_MAIL = " + email);
		
		// Declare a null Spring User
		UserDetails springUser = null;
		
		try {
			
			// Search database for a user that matches the specified email
			// You can provide a custom DAO to access your persistence layer
			// Or use JDBC to access your database
			// DbUser is our custom domain user. This is not the same as Spring's User
			com.eqms.model.User user = getUserService().findByEmail(email);
			
			// Populate the Spring User object with details from the dbUser
			// Here we just pass the email, password, and access level
			// getAuthorities() will translate the access level to the correct role type

			springUser =  new User(
					user.getEmail(), 
					user.getPassword().toLowerCase(),
					user.isEnabled(),
					true,
					true,
					true,
					getAuthorities(user.getUserRoles().getRoleId()) );

		} catch (Exception e) {
			logger.error("Error in retrieving user");
			throw new UsernameNotFoundException("Error in retrieving user");
		}
		
		// Return user to Spring for processing.
		// Take note we're not the one evaluating whether this user is authenticated or valid
		// We just merely retrieve a user that matches the specified email
		return springUser;
	}
	

	/**
	 * Retrieves the correct ROLE type depending on the access level, where access level is an Integer.
	 * Basically, this interprets the access value whether it's for a regular user or admin.
	 * 
	 * @param access an integer value representing the access of the user
	 * @return collection of granted authorities
	 */
	 public Collection<GrantedAuthority> getAuthorities(Integer access) {
		 // Create a list of grants for this user
		 List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);

         if( access.compareTo(3) == 0) {
             //User has student role
             logger.debug("Grant ROLE_STUDENT to this user");
             authList.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
         }
         else {
             // All users are granted with ROLE_USER access
             // Therefore this user gets a ROLE_USER by default
             logger.debug("Grant ROLE_USER to this user");
             authList.add(new SimpleGrantedAuthority("ROLE_USER"));

             // Check if this user has admin access
             // We interpret Integer(1) as an admin user
             if (access.compareTo(1) == 0) {
                 // User has admin access
                 logger.debug("Grant ROLE_ADMIN to this user");
                 authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
             }
         }

	
		 // Return list of granted authorities
		 return authList;
	 }
	 
	 public UserService getUserService() {
		 return userService;
	 }

	 public void setUserService(UserService userService) {
		 this.userService = userService;
	 }

}
