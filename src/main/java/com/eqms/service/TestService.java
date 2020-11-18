package com.eqms.service;

import java.util.List;

import org.hibernate.criterion.Order;

import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Picture;
import com.eqms.model.Question;
import com.eqms.model.SetOfRating;
import com.eqms.model.Subject;
import com.eqms.model.Test;
import com.eqms.model.User;

import javax.persistence.criteria.CriteriaBuilder;

public interface TestService {
	/**
	 * Methods for subjects
	 */
	public void addSubject(Subject subject, Integer userId);
	public void addReferenceUserToSubject(Integer userId, Integer subjectId);
	public void updateSubject(Subject subject);
	public void deleteSubject(Integer subjectId);
	Subject getSubjectBySubjectId(Integer subjectId);
	Boolean checkSubjectName(String subjectName);
	
	List<Subject> getAllSubjects(Order order, Integer maxResults);
	List<Subject> getSubjectsByUser(User user);
	List<Test> getTestsBySubjectId(Integer subjectId);
	
	/**
	 * Methods for tests
	 */
	public void addTest(Test test);
	public void updateTest(Test test);
	public void deleteTest(Integer testId);
	Test getTestByTestId(Integer testId);
	Integer getTestIdByGroupId(Integer groupId);
	Boolean checkTestName(String testName, Integer subjectId);
	
	public void addGroup(GroupOfQuestions group);
	public void updateGroup(GroupOfQuestions group);
	public void deleteGroup(Integer groupId);
	GroupOfQuestions getGroupByGroupId(Integer groupId);
	Boolean checkGroupName(String groupName, Integer testId);

	public void addReferenceStudentToGroupOfQuestions(Integer studentId, Integer groupId);
	public int getGroupOfQuestionsIdbyStudentId(Integer studentId);

	public void addReferenceStudentToAnswers(Integer studentId, Integer groupId);
	public void deleteReferenceStudentToAnswers(Integer currentStudentId, Integer answerId);
	public List<Integer> getAnswersIdByStudentId(Integer studentId);
	Boolean checkIfAnswerIsChoosedByStudent(Integer studentId, Integer answerId);
	
	public void addQuestion(Question question, Integer groupId);
	public void updateQuestion(Question question);
	public void deleteQuestion(Integer questionId);
	Question getQuestionByQuestionId(Integer questionId);
	Boolean checkContentOfQuestion(String contentOfQuestion, Integer testId);
	
	public void addAnswer(Answer answer, Integer questionId);
	public void updateAnswer(Answer answer);
	public void deleteAnswer(Integer answerId);
	Answer getAnswerByAnswerId(Integer answerId);
	Boolean checkContentOfAnswer(String contentOfAnswer, Integer questionId);
	
	public void addPicture(Picture picture);
	public void updatePicture(Picture picture);
	public void deletePicture(Integer pictureId);
	Picture getPictureByPictureId(Integer pictureId);
	Boolean checkPictureName(String pictureName);
	
	public void addSetOfRating(SetOfRating setOfRating);
	public void updateSetOfRating(SetOfRating setOfRating);
	public void deleteSetOfRating(Integer setOfRatingId);
	SetOfRating getSetOfRatingBySetId(Integer setOfRatingId);
	
	List<Test> getAllTests(Order order, Integer maxResults);
	List<GroupOfQuestions> getAllGroups(Order order, Integer maxResults);
	List<Question> getAllQuestions(Order order, Integer maxResult);
	List<Answer> getAllAnswers(Order order, Integer maxResults);
	List<SetOfRating> getAllSetsOfRating(Order order, Integer maxResults);
	List<Picture> getAllPictures(Order order, Integer maxResults);
	
	List<GroupOfQuestions> getAllGroupsByTestId(Integer testId);
	List<Question> getAllQuestionsByGroupId(Integer groupId);
	List<Answer> getAllAnswersByQuestionId(Integer questionId);
	
	GroupOfQuestions getGroupByQuestionId(Integer questionId);
	Question getQuestionByAnswerId(Integer answerId);
	String generatePassword();


}
