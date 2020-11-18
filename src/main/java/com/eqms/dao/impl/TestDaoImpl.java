package com.eqms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.eqms.dao.TestDao;
import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Picture;
import com.eqms.model.Question;
import com.eqms.model.SetOfRating;
import com.eqms.model.Subject;
import com.eqms.model.Test;
import com.eqms.model.User;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public class TestDaoImpl implements TestDao {

	private SessionFactory sessionFactory;
	protected static Logger logger = Logger.getLogger("dao");
	
	/**
	 * Methods for subjects
	 */
	@Override
	public void addSubject(Subject subject, Integer userId) {
		
		// Adding new subject to database
		getSessionFactory().getCurrentSession().save(subject);
		
		// Finding added subject
		Subject addedSubject = getAllSubjects(Order.desc("subjectId"), 1).get(0);

		// Adding reference of new added subject to user
		String queryString = "INSERT INTO USERS_SUBJECTS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, userId);
		query.setParameter(1, addedSubject.getSubjectId());
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted = " + String.valueOf(numberDeletedUpdatedEntities));
	}
	
	@Override
	public void addReferenceUserToSubject(Integer userId, Integer subjectId) {
		
		// Adding reference of new added subject to user
		String queryString = "INSERT INTO USERS_SUBJECTS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, userId);
		query.setParameter(1, subjectId);

		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted = " + String.valueOf(numberDeletedUpdatedEntities));
	}

	@Override
	public void updateSubject(Subject subject) {
		getSessionFactory().getCurrentSession().update(subject);
	}

	@Override
	public void deleteSubject(Integer subjectId) {
		
		// Deleting all test, that was created into this subject
		List<Test> tests = getTestsBySubjectId(subjectId);
		
		logger.debug("Loop with tests to deleting");
		for(Test test : tests) {
			deleteTest(test.getTestId());
		}
		
		// Finally removing subject
		String queryStringDeleteSubject = "DELETE FROM SUBJECTS WHERE SUBJECT_ID = ?";
		SQLQuery queryDeleteSubject = getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteSubject);
		queryDeleteSubject.setParameter(0, subjectId);
		
		int numberDeletedUpdatedEntities = queryDeleteSubject.executeUpdate(); 
		logger.debug("The number of entities updated or deleted (subjects): " + String.valueOf(numberDeletedUpdatedEntities));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Subject getSubjectBySubjectId(Integer subjectId) {
		
		List<Subject> subject = new ArrayList<Subject>();
		
		String queryString = "SELECT * FROM SUBJECTS WHERE SUBJECT_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Subject.class);
		query.setParameter(0, subjectId);
		subject = query.list();
				
		return subject.get(0);
	}
	
	@Override
	public Boolean checkSubjectName(String subjectName) {
		
		String queryString = "SELECT COUNT(*) FROM SUBJECTS WHERE SUBJECT_NAME = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, subjectName);
		
		int subjectNameExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("subjectNameExists value = " + subjectNameExists);
		
		if(subjectNameExists == 0) {
			return false; 	// subjectName doesn't exist
		} else {
			return true;	// subjectName exists
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Subject> getAllSubjects(Order order, Integer maxResults) {
		
		List<Subject> subjects = new ArrayList<Subject>();
		
		if (maxResults != null) {
			subjects = getSessionFactory().getCurrentSession().createCriteria(Subject.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			subjects = getSessionFactory().getCurrentSession().createCriteria(Subject.class).addOrder(order).list();
		}
		
		return subjects;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Subject> getSubjectsByUser(User user) {
		
		List<Subject> subjects = new ArrayList<Subject>();
		
		String queryString = "SELECT * FROM SUBJECTS WHERE SUBJECT_ID IN (SELECT SUBJECT_ID FROM USERS_SUBJECTS WHERE USER_ID = ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Subject.class);
		query.setParameter(0, user.getUserId());
		subjects = query.list();
		
		return subjects;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Test> getTestsBySubjectId(Integer subjectId) {
		
		List<Test> tests = new ArrayList<Test>();
		
		String queryString = "SELECT * FROM TESTS WHERE SUBJECT_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Test.class);
		query.setParameter(0, subjectId);
		tests = query.list();
		
		return tests;
	}
	
	/**
	 * Methods for tests
	 */
	@Override
	public void addTest(Test test) {
		getSessionFactory().getCurrentSession().save(test);
	}

	@Override
	public void updateTest(Test test) {
		getSessionFactory().getCurrentSession().update(test);
	}

	@Override
	public void deleteTest(Integer testId) {
		// By removing the test should also remove all the groups for this test.
		
		// Getting reference to this test
		Test currentTest = getTestByTestId(testId);
		
		// List of groups for this test
		List<GroupOfQuestions> groups = getAllGroupsByTestId(testId);
		
		logger.debug("Loop with groups to deleting");
		// Removing all groups for this test
		for(GroupOfQuestions group : groups) {
			deleteGroup(group.getGroupId());
		}
		
		logger.debug("Start removing test with set of rating");
		// Finally removing test with set of rating
		logger.debug(currentTest.getSetsOfRating().getSetId());
		deleteSetOfRating(currentTest.getSetsOfRating().getSetId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Test getTestByTestId(Integer testId) {
		
		List<Test> test = new ArrayList<Test>();
		
		String queryString = "SELECT * FROM TESTS WHERE TEST_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Test.class);
		query.setParameter(0, testId);
		test = query.list();
		
		return test.get(0);
	}

	@Override
	public Integer getTestIdByGroupId(Integer groupId) {

		List<Integer> test = new ArrayList<Integer>();
		Integer testId;

		String queryString = "SELECT TEST_ID FROM GROUPS_OF_QUESTIONS WHERE GROUP_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, groupId);
		test = query.list();
		testId=test.get(0);

		return testId;
	}

	@Override
	public Boolean checkTestName(String testName, Integer subjectId) {
		
		String queryString = "SELECT COUNT(*) FROM TESTS WHERE TEST_NAME = ? AND SUBJECT_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, testName);
		query.setParameter(1, subjectId);
		
		int testNameExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("testNameExists value = " + testNameExists);
		
		if(testNameExists == 0) {
			return false; 		// testName doesn't exist
		} else {
			return true;		// testName exists
		}
	}
	
	@Override
	public void addGroup(GroupOfQuestions group) {
		getSessionFactory().getCurrentSession().save(group);
		
		// Finding last added group
		GroupOfQuestions addedGroup = getAllGroups(Order.desc("groupId"), 1).get(0);
		
		// Update test
		Test test = getTestByTestId(addedGroup.getTests().getTestId());
		Integer newNumberOfGroups = test.getNumberOfGroups() + 1;
		test.setNumberOfGroups(newNumberOfGroups);
		updateTest(test);		
	}

	@Override
	public void updateGroup(GroupOfQuestions group) {
		getSessionFactory().getCurrentSession().update(group);		
	}

	@Override
	public void deleteGroup(Integer groupId) {
		// By removing the group should also remove all the questions for this group. 
		
		// List of questions for this group
		List<Question> questions = getAllQuestionsByGroupId(groupId);
		
		logger.debug("Loop with questions to deleting");
		// Removing all questions for this group
		for(Question question : questions) {
			deleteQuestion(question.getQuestionId());
		}
		
		logger.debug("Start removing group");
		// Finally removing group
		String queryString = "DELETE FROM GROUPS_OF_QUESTIONS WHERE GROUP_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, groupId);
		logger.debug("SQLQuery = " + query.getQueryString());
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (groups): " + String.valueOf(numberDeletedUpdatedEntities));
	}
	
	/*@Override
	public void deleteReferenceGroupToQuestions(Integer groupId) {
		
		String queryString = "DELETE FROM GROUPS_QUESTIONS WHERE GROUP_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, groupId);
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (groups_questions): " + String.valueOf(numberDeletedUpdatedEntities));
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public GroupOfQuestions getGroupByGroupId(Integer groupId) {

		List<GroupOfQuestions> group = new ArrayList<GroupOfQuestions>();
		
		String queryString = "SELECT * FROM GROUPS_OF_QUESTIONS WHERE GROUP_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(GroupOfQuestions.class);
		query.setParameter(0, groupId);
		group = query.list();
		
		return group.get(0);
	}
	
	@Override
	public Boolean checkGroupName(String groupName, Integer testId) {
		
		String queryString = "SELECT COUNT(*) FROM GROUPS_OF_QUESTIONS WHERE GROUP_NAME = ? AND TEST_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, groupName);
		query.setParameter(1, testId);
		
		int groupNameExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("groupNameExists value = " + groupNameExists);
		
		if(groupNameExists == 0) {
			return false; 		// groupName doesn't exist
		} else {
			return true;		// groupName exists
		}
	}

	@Override
	public void addReferenceStudentToGroupOfQuestions(Integer studentId, Integer groupId){
		String queryString = "INSERT INTO STUDENTS_GROUPS_OF_QUESTIONS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		query.setParameter(1, groupId);
		query.executeUpdate();
	}

	@Override
	public int getGroupOfQuestionsIdbyStudentId(Integer studentId){
		String queryString = "SELECT GROUP_ID FROM STUDENTS_GROUPS_OF_QUESTIONS WHERE STUDENT_ID = (?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		List<Integer> list = query.list();
		int groupId=list.get(0);
		return groupId;
	}

	@Override
	public void addReferenceStudentToAnswers(Integer studentId, Integer answerId) {
		String queryString = "INSERT INTO STUDENTS_ANSWERS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		query.setParameter(1, answerId);
		query.executeUpdate();
	}

	@Override
	public void deleteReferenceStudentToAnswers(Integer studentId, Integer answerId) {
		String queryString = "DELETE FROM STUDENTS_ANSWERS WHERE STUDENT_ID = ? AND ANSWER_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		query.setParameter(1, answerId);
		query.executeUpdate();
	}

	@Override
	public List<Integer> getAnswersIdByStudentId(Integer studentId) {
		List<Integer> answers = new ArrayList<Integer>();

		String queryString = "SELECT ANSWER_ID FROM STUDENTS_ANSWERS WHERE STUDENT_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		answers = query.list();

		return answers;
	}

	@Override
	public Boolean checkIfAnswerIsChoosedByStudent(Integer studentId, Integer answerId) {
		String queryString = "SELECT COUNT(*) FROM STUDENTS_ANSWERS WHERE STUDENT_ID = (?) AND ANSWER_ID = (?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, studentId);
		query.setParameter(1,answerId);
		int choosedAnswerExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("choosedAnswerExists value = " + choosedAnswerExists);

		if(choosedAnswerExists == 0) {
			return false; 		// contentOfQuestion doesn't exist
		} else {
			return true;		// contentOfQuestion exists
		}
	}


	@Override
	public void addQuestion(Question question, Integer groupId) {
		
		// Adding new question to database
		getSessionFactory().getCurrentSession().save(question);	
		
		// Finding last added question
		List<Question> addedQuestion = getAllQuestions(Order.desc("questionId"), 1);
		
		// Adding reference of new question to group
		String queryString = "INSERT INTO GROUPS_QUESTIONS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, groupId);
		query.setParameter(1, addedQuestion.get(0).getQuestionId());
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted = " + String.valueOf(numberDeletedUpdatedEntities));
		
		// Update group
		GroupOfQuestions group = getGroupByGroupId(groupId);
		Integer newNumberOfQuestions = group.getNumberOfQuestions() + 1;
		group.setNumberOfQuestions(newNumberOfQuestions);
		updateGroup(group);
		
		// Update test
		Test test = group.getTests();
		Integer newGeneralNumberOfQuestions = test.getNumberOfQuestions() + 1;
		test.setNumberOfQuestions(newGeneralNumberOfQuestions);
		updateTest(test);
	}

	@Override
	public void updateQuestion(Question question) {
		getSessionFactory().getCurrentSession().update(question);		
	}

	@Override
	public void deleteQuestion(Integer questionId) {
		// By removing the question should also remove all the answers for this question.
		
		// Getting reference to this question
		Question currentQuestion = getQuestionByQuestionId(questionId);
				
		// List of answers for this question
		List<Answer> answers = getAllAnswersByQuestionId(questionId);
		
		logger.debug("Loop with answers to deleting");
		// Removing all answers for this question
		for(Answer answer : answers) {
			deleteAnswer(answer.getAnswerId());
		}
		
		// Removing picture assigned to this question
		if(currentQuestion.getPictures() != null) {
			logger.debug("Removing picture assigned to current question");
			deletePicture(currentQuestion.getPictures().getPictureId());
		} else {
			logger.debug("Start removing question");
			// Finally removing question
			String queryString = "DELETE FROM QUESTIONS WHERE QUESTION_ID = ?";
			SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
			query.setParameter(0, questionId);
			logger.debug("SQLQuery = " + query.getQueryString());
			
			int numberDeletedUpdatedEntities = query.executeUpdate();
			logger.debug("The number of entities updated or deleted (questions): " + String.valueOf(numberDeletedUpdatedEntities));
		}
	}


	/*@Override
	public void deleteReferenceQuestionToGroup(Integer questionId) {
		
		String queryString = "DELETE FROM GROUPS_QUESTIONS WHERE QUESTION_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, questionId);
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (groups_questions): " + String.valueOf(numberDeletedUpdatedEntities));
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public Question getQuestionByQuestionId(Integer questionId) {
		
		List<Question> question = new ArrayList<Question>();
		
		String queryString = "SELECT * FROM QUESTIONS WHERE QUESTION_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Question.class);
		query.setParameter(0, questionId);
		question = query.list();
		
		return question.get(0);
	}
	
	@Override
	public Boolean checkContentOfQuestion(String contentOfQuestion, Integer testId) {
		
		String queryString = "SELECT COUNT(*) FROM QUESTIONS q, GROUPS_QUESTIONS g_q, GROUPS_OF_QUESTIONS g "
				+ "WHERE q.QUESTION_ID = g_q.QUESTION_ID "
				+ "AND g_q.GROUP_ID = g.GROUP_ID "
				+ "AND q.CONTENT_OF_QUESTION = ?"
				+ "AND g.TEST_ID = ?";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, contentOfQuestion);
		query.setParameter(1, testId);
		
		int contentOfQuestionExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("contentOfQuestionExists value = " + contentOfQuestionExists);
		
		if(contentOfQuestionExists == 0) {
			return false; 		// contentOfQuestion doesn't exist
		} else {
			return true;		// contentOfQuestion exists
		}
	}

	@Override
	public void addAnswer(Answer answer, Integer questionId) {
		// Adding new answer to database
		getSessionFactory().getCurrentSession().save(answer);	
		
		// Finding last added answer
		List<Answer> addedAnswer = getAllAnswers(Order.desc("answerId"), 1);
		
		// Adding reference of new answer to question
		String queryString = "INSERT INTO QUESTIONS_ANSWERS VALUES (?, ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, questionId);
		query.setParameter(1, addedAnswer.get(0).getAnswerId());
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (questions_answers) = " + String.valueOf(numberDeletedUpdatedEntities));
		
		logger.debug("Start updating question");
		// Update question
		Question question = getQuestionByQuestionId(questionId);
		Integer newNumberOfAnswers = question.getNumberOfAnswers() + 1;
		question.setNumberOfAnswers(newNumberOfAnswers);
		if (addedAnswer.get(0).isWhetherCorrect()) {	// whetherCorrect == true
			Integer newNumberOfCorrectAnswers = question.getNumberOfCorrectAnswers() + 1;
			question.setNumberOfCorrectAnswers(newNumberOfCorrectAnswers);
		}
		updateQuestion(question);
	}

	@Override
	public void updateAnswer(Answer answer) {
		getSessionFactory().getCurrentSession().update(answer);	
	}

	@Override
	public void deleteAnswer(Integer answerId) {
		
		// Getting reference to this answer
		Answer currentAnswer = getAnswerByAnswerId(answerId);
		
		// Removing picture assigned to this answer
		if(currentAnswer.getPictures() != null) {
			logger.debug("Removing picture assigned to current answer");
			deletePicture(currentAnswer.getPictures().getPictureId());
		} else {
			logger.debug("Start removing answer");
			String queryString = "DELETE FROM ANSWERS WHERE ANSWER_ID = ?";
			SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
			query.setParameter(0, answerId);
			logger.debug("SQLQuery = " + query.getQueryString());
			
			int numberDeletedUpdatedEntities = query.executeUpdate();
			logger.debug("The number of entities updated or deleted (answers): " + String.valueOf(numberDeletedUpdatedEntities));
		}
	}
	
	/*@Override
	public void deleteReferenceAnswerToQuestion(Integer answerId) {
		
		String queryString = "DELETE FROM QUESTIONS_ANSWERS WHERE ANSWER_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, answerId);
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (questions_answers): " + String.valueOf(numberDeletedUpdatedEntities));
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public Answer getAnswerByAnswerId(Integer answerId) {
		
		List<Answer> answer = new ArrayList<Answer>();
		
		String queryString = "SELECT * FROM ANSWERS WHERE ANSWER_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Answer.class);
		query.setParameter(0, answerId);
		answer = query.list();
		
		return answer.get(0);
	}
	
	@Override
	public Boolean checkContentOfAnswer(String contentOfAnswer, Integer questionId) {
		
		String queryString = "SELECT COUNT(*) FROM ANSWERS a, QUESTIONS_ANSWERS q_a, QUESTIONS q "
				+ "WHERE a.ANSWER_ID = q_a.ANSWER_ID "
				+ "AND q_a.QUESTION_ID = q.QUESTION_ID "
				+ "AND a.CONTENT_OF_ANSWER = ? "
				+ "AND q.QUESTION_ID = ?";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, contentOfAnswer);
		query.setParameter(1, questionId);
		
		int contentOfAnswerExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("contentOfAnswerExists value = " + contentOfAnswerExists);
		
		if(contentOfAnswerExists == 0) {
			return false; 		// contentOfAnswer doesn't exist
		} else {
			return true;		// contentOfAnswer exists
		}
	}

	@Override
	public void addPicture(Picture picture) {
		getSessionFactory().getCurrentSession().save(picture);	
	}

	@Override
	public void updatePicture(Picture picture) {
		getSessionFactory().getCurrentSession().update(picture);	
	}

	@Override
	public void deletePicture(Integer pictureId) {
		
		String queryString = "DELETE FROM PICTURES WHERE PICTURE_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, pictureId);
		logger.debug("SQLQuery = " + query.getQueryString());
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (pictures): " + String.valueOf(numberDeletedUpdatedEntities));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Picture getPictureByPictureId(Integer pictureId) {
		
		List<Picture> picture = new ArrayList<Picture>();
		
		String queryString = "SELECT * FROM PICTURES WHERE PICTURE_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Picture.class);
		query.setParameter(0, pictureId);
		picture = query.list();
		
		return picture.get(0);
	}
	
	@Override
	public Boolean checkPictureName(String pictureName) {
		
		String queryString = "SELECT COUNT(*) FROM PICTURES WHERE PICTURE_NAME = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, pictureName);
		
		int pictureNameExists = Integer.valueOf(query.list().get(0).toString());
		logger.debug("pictureNameExists value = " + pictureNameExists);
		
		if(pictureNameExists == 0) {
			return false; 		// pictureName doesn't exist
		} else {
			return true;		// pictureName exists
		}
	}
	
	@Override
	public void addSetOfRating(SetOfRating setOfRating) {
		getSessionFactory().getCurrentSession().save(setOfRating);	
	}

	@Override
	public void updateSetOfRating(SetOfRating setOfRating) {
		getSessionFactory().getCurrentSession().update(setOfRating);
	}

	@Override
	public void deleteSetOfRating(Integer setOfRatingId) {
		
		String queryString = "DELETE FROM SETS_OF_RATING WHERE SET_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
		query.setParameter(0, setOfRatingId);
		
		int numberDeletedUpdatedEntities = query.executeUpdate();
		logger.debug("The number of entities updated or deleted (setsOfRating): " + String.valueOf(numberDeletedUpdatedEntities));
	}

	@SuppressWarnings("unchecked")
	@Override
	public SetOfRating getSetOfRatingBySetId(Integer setOfRatingId) {
		
		List<SetOfRating> setOfRating = new ArrayList<SetOfRating>();
		
		String queryString = "SELECT * FROM SETS_OF_RATING WHERE SET_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(SetOfRating.class);
		query.setParameter(0, setOfRatingId);
		setOfRating = query.list();
		
		return setOfRating.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Test> getAllTests(Order order, Integer maxResults) {
		
		List<Test> tests = new ArrayList<Test>();
		
		if (maxResults != null) {
			tests = getSessionFactory().getCurrentSession().createCriteria(Test.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			tests = getSessionFactory().getCurrentSession().createCriteria(Test.class).addOrder(order).list();
		}
		
		return tests;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupOfQuestions> getAllGroups(Order order, Integer maxResults) {
		
		List<GroupOfQuestions> groups = new ArrayList<GroupOfQuestions>();
		
		if (maxResults != null) {
			groups = getSessionFactory().getCurrentSession().createCriteria(GroupOfQuestions.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			groups = getSessionFactory().getCurrentSession().createCriteria(GroupOfQuestions.class).addOrder(order).list();
		}
		
		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getAllQuestions(Order order, Integer maxResults) {
		
		List<Question> questions = new ArrayList<Question>();
	
		if (maxResults != null) {
			questions = getSessionFactory().getCurrentSession().createCriteria(Question.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			questions = getSessionFactory().getCurrentSession().createCriteria(Question.class).addOrder(order).list();
		}
			
		return questions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Answer> getAllAnswers(Order order, Integer maxResults) {
		
		List<Answer> answers = new ArrayList<Answer>();
		
		if (maxResults != null) {
			answers = getSessionFactory().getCurrentSession().createCriteria(Answer.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			answers = getSessionFactory().getCurrentSession().createCriteria(Answer.class).addOrder(order).list();
		}
		
		return answers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SetOfRating> getAllSetsOfRating(Order order, Integer maxResults) {
		
		List<SetOfRating> sets = new ArrayList<SetOfRating>();
		
		if (maxResults != null) {
			sets = getSessionFactory().getCurrentSession().createCriteria(SetOfRating.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			sets = getSessionFactory().getCurrentSession().createCriteria(SetOfRating.class).addOrder(order).list();
		}
		
		return sets;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Picture> getAllPictures(Order order, Integer maxResults) {

		List<Picture> pictures = new ArrayList<Picture>();
		
		if (maxResults != null) {
			pictures = getSessionFactory().getCurrentSession().createCriteria(Picture.class).addOrder(order).setMaxResults(maxResults).list();
		} else {
			pictures = getSessionFactory().getCurrentSession().createCriteria(Picture.class).addOrder(order).list();
		}
		
		return pictures;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GroupOfQuestions> getAllGroupsByTestId(Integer testId) {
		
		List<GroupOfQuestions> groups = new ArrayList<GroupOfQuestions>();
		
		String queryString = "SELECT * FROM GROUPS_OF_QUESTIONS WHERE TEST_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(GroupOfQuestions.class);
		query.setParameter(0, testId);
		groups = query.list();
		
		return groups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Question> getAllQuestionsByGroupId(Integer groupId) {
		
		List<Question> questions = new ArrayList<Question>();
		
		String queryString = "SELECT * FROM QUESTIONS q, GROUPS_QUESTIONS g_q WHERE q.QUESTION_ID = g_q.QUESTION_ID AND g_q.GROUP_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Question.class);
		query.setParameter(0, groupId);
		questions = query.list();
		
		return questions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Answer> getAllAnswersByQuestionId(Integer questionId) {
		
		List<Answer> answers = new ArrayList<Answer>();
		
		String queryString = "SELECT * FROM ANSWERS a, QUESTIONS_ANSWERS q_a WHERE a.ANSWER_ID = q_a.ANSWER_ID AND q_a.QUESTION_ID = ?";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Answer.class);
		query.setParameter(0, questionId);
		answers = query.list();
		
		return answers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public GroupOfQuestions getGroupByQuestionId(Integer questionId) {
		
		List<GroupOfQuestions> group = new ArrayList<GroupOfQuestions>();
		
		String queryString = "SELECT * FROM GROUPS_OF_QUESTIONS WHERE GROUP_ID = (SELECT GROUP_ID FROM GROUPS_QUESTIONS WHERE QUESTION_ID = ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(GroupOfQuestions.class);
		query.setParameter(0, questionId);
		group = query.list();
		
		return group.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Question getQuestionByAnswerId(Integer answerId) {
		
		List<Question> question = new ArrayList<Question>();
		
		String queryString = "SELECT * FROM QUESTIONS WHERE QUESTION_ID = (SELECT QUESTION_ID FROM QUESTIONS_ANSWERS WHERE ANSWER_ID = ?)";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Question.class);
		query.setParameter(0, answerId);
		question = query.list();
		
		return question.get(0);
	}

	@Override
	public String generatePassword(){
		String generatedPassword = RandomStringUtils.randomAlphanumeric(10);
		logger.debug("Generated password is:" +generatedPassword);
		return generatedPassword;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
