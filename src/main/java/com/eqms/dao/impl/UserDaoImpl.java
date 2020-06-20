package com.eqms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.eqms.dao.UserDao;
import com.eqms.model.User;
import com.eqms.model.UserRole;
import com.eqms.model.VerificationToken;

@Repository
public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;
	protected static Logger logger = Logger.getLogger("dao");

	@SuppressWarnings("unchecked")
	@Override
	public User findByEmail(String e_mail) {
		
		List<User> users = new ArrayList<User>();
		User user = null;
		
		users = getSessionFactory().getCurrentSession().createQuery("from User where e_mail=?")
				.setParameter(0, e_mail).list();
		
		if(users.size() > 0) {
			logger.debug("User found");
			System.out.println("User found");
			logger.debug("Email = " + e_mail);
			logger.debug("Lastname = " + users.get(0).getLastname());
			logger.debug("Password = " + users.get(0).getPassword());
			
			user = users.get(0);
			
		} else if (users.size() == 0) {
			logger.error("User does not exist!");
			System.out.println("User does not exist!");
		}
		
		return user;
	}
	
	@Override
	public boolean isEmailExists(String e_mail) {
		
		String queryString = "SELECT COUNT(*) FROM USERS WHERE E_MAIL=?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, e_mail);
		
		int param = Integer.valueOf(query.list().get(0).toString());
		logger.debug("Param value = " + param);
		
		if(param == 0) {
			return false; 	// email nie istnieje
		} else {
			return true;	// email istnieje
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserRole getUserRolebyNazwa(String nazwa) {
		
		List<UserRole> userRoles = new ArrayList<UserRole>();
		UserRole userRole = null;
		
		userRoles = getSessionFactory().getCurrentSession().createQuery("from UserRole where name=?")
				.setParameter(0, nazwa).list();
		
		if(userRoles.size() > 0 ) {
			userRole = userRoles.get(0);
		} else if (userRoles.size() == 0) {
			
		}
	
		return userRole;
	}

	@Override
	public void add(User user) {
		getSessionFactory().getCurrentSession().save(user);
	}
	
	@Override
	public String getVerificationTokenbyUser(User user) {	
		String queryString = "SELECT TOKEN FROM VERIFICATION_TOKENS WHERE USER_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, user.getUserId());
		return query.list().get(0).toString();
	}
	
	
	@Override
	public void addVerificationToken(User user) {
		
		String token = DigestUtils.sha256Hex(user.getEmail() + "." + user.getPassword());
		VerificationToken verificationToken = new VerificationToken(user, token);
		
		getSessionFactory().getCurrentSession().save(verificationToken); 
	}

	
	@Override
	public void updateVerificationToken(User user) {
		
		String token = DigestUtils.sha256Hex(user.getEmail() + "." + user.getPassword());
		VerificationToken verificationToken = new VerificationToken(user, token);
		
		getSessionFactory().getCurrentSession().update(verificationToken);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User getUserByVerificationToken(String token) {
		
		List<User> users = new ArrayList<User>();
		User user = null;
		
		String queryString = "SELECT USER_ID FROM VERIFICATION_TOKENS WHERE TOKEN = ?";
		int user_id = Integer.valueOf(getSessionFactory().getCurrentSession().createSQLQuery(queryString).setParameter(0, token).list().get(0).toString());
		
		users = getSessionFactory().getCurrentSession().createQuery("FROM User WHERE USER_ID = ?").setParameter(0, user_id).list();
		
		if(users.size() > 0) {
			user = users.get(0);
		} else if (users.size() == 0) {
			
		}

		return user;
	}
	
	@Override
	public void updateEnabledVariableForUser(User user) {
		getSessionFactory().getCurrentSession().update(user);
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
