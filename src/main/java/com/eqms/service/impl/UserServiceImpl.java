package com.eqms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eqms.dao.UserDao;
import com.eqms.model.User;
import com.eqms.model.UserRole;
import com.eqms.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	
	@Override
	public User findByEmail(String e_mail) {
		return userDao.findByEmail(e_mail);
	}

	@Override
	public void add(User user) {
		userDao.add(user);
	}

	@Override
	public void delete(String email) {userDao.delete(email);}

	@Override
	public boolean isEmailExists(String e_mail) {
		return userDao.isEmailExists(e_mail);
	}

	@Override
	public UserRole getUserRolebyNazwa(String nazwa) {
		return userDao.getUserRolebyNazwa(nazwa);
	}

	@Override
	public String getVerificationTokenbyUser(User user) {
		return userDao.getVerificationTokenbyUser(user);
	}

	@Override
	public void addVerificationToken(User user) {
		userDao.addVerificationToken(user);
	}

	@Override
	public void updateVerificationToken(User user) {
		userDao.updateVerificationToken(user);
	}

	@Override
	public User getUserByVerificationToken(String token) {
		return userDao.getUserByVerificationToken(token);
	}

	@Override
	public void updateEnabledVariableForUser(User user) {
		userDao.updateEnabledVariableForUser(user);
	}

}
