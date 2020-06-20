package com.eqms.service;

import com.eqms.model.User;
import com.eqms.model.UserRole;

public interface UserService {
	
	public void add(User user);
	User findByEmail(String e_mail);
	
	boolean isEmailExists(String e_mail);
	UserRole getUserRolebyNazwa(String nazwa);
	
	public void addVerificationToken(User user);	
	public void updateVerificationToken(User user);
	User getUserByVerificationToken(String token);
	String getVerificationTokenbyUser(User user);
	
	public void updateEnabledVariableForUser(User user);
}
