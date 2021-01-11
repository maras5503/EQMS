package com.eqms.service.impl;

import java.sql.Time;
import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eqms.dao.TestDao;
import com.eqms.model.Answer;
import com.eqms.model.GroupOfQuestions;
import com.eqms.model.Picture;
import com.eqms.model.Question;
import com.eqms.model.SetOfRating;
import com.eqms.model.Subject;
import com.eqms.model.Test;
import com.eqms.model.User;
import com.eqms.service.TestService;

@Service
@Transactional
public class TestServiceImpl implements TestService {

	@Autowired
	private TestDao testDao;
	
	/**
	 * Methods for subjects
	 */
	@Override
	public void addSubject(Subject subject, Integer userId) {
		testDao.addSubject(subject, userId);
	}

	@Override
	public void addReferenceUserToSubject(Integer userId, Integer subjectId) {
		testDao.addReferenceUserToSubject(userId, subjectId);
	}
	
	@Override
	public void updateSubject(Subject subject) {
		testDao.updateSubject(subject);
	}

	@Override
	public void deleteSubject(Integer subjectId) {
		testDao.deleteSubject(subjectId);
	}
	
	@Override
	public Subject getSubjectBySubjectId(Integer subjectId) {
		return testDao.getSubjectBySubjectId(subjectId);
	}

	@Override
	public Boolean checkSubjectName(String subjectName) {
		return testDao.checkSubjectName(subjectName);
	}

	@Override
	public List<Subject> getAllSubjects(Order order, Integer maxResults) {
		return testDao.getAllSubjects(order, maxResults);
	}
	
	@Override
	public List<Subject> getSubjectsByUser(User user) {
		return testDao.getSubjectsByUser(user);
	}
	
	@Override
	public List<Test> getTestsBySubjectId(Integer subjectId) {
		return testDao.getTestsBySubjectId(subjectId);
	}
	
	/**
	 * Methods for tests
	 */
	@Override
	public void addTest(Test test) {
		testDao.addTest(test);
	}

	@Override
	public void updateTest(Test test) {
		testDao.updateTest(test);
	}

	@Override
	public void deleteTest(Integer testId) {
		testDao.deleteTest(testId);
	}

	@Override
	public Test getTestByTestId(Integer testId) {
		return testDao.getTestByTestId(testId);
	}

	@Override
	public Integer getTestIdByGroupId(Integer groupId){return testDao.getTestIdByGroupId(groupId);}
	
	@Override
	public Boolean checkTestName(String testName, Integer subjectId) {
		return testDao.checkTestName(testName, subjectId);
	}

	@Override
	public void addGroup(GroupOfQuestions group) {
		testDao.addGroup(group);
	}

	@Override
	public void updateGroup(GroupOfQuestions group) {
		testDao.updateGroup(group);
	}

	@Override
	public void deleteGroup(Integer groupId) {
		testDao.deleteGroup(groupId);
	}

	@Override
	public GroupOfQuestions getGroupByGroupId(Integer groupId) {
		return testDao.getGroupByGroupId(groupId);
	}
	
	@Override
	public Boolean checkGroupName(String groupName, Integer testId) {
		return testDao.checkGroupName(groupName, testId);
	}

	@Override
	public void addReferenceStudentToGroupOfQuestions(Integer studentId, Integer groupId, Time time){testDao.addReferenceStudentToGroupOfQuestions(studentId,groupId,time);}

    @Override
    public void saveFinishExamTime(Integer studentId, Integer groupId, String time) {testDao.saveFinishExamTime(studentId,groupId,time);}

    @Override
	public void deleteReferenceStudentToGroupOfQuestions(Integer studentId, Integer groupId) {testDao.deleteReferenceStudentToGroupOfQuestions(studentId,groupId);}

	@Override
	public int getGroupOfQuestionsIdbyStudentId(Integer studentId) { return testDao.getGroupOfQuestionsIdbyStudentId(studentId);}

	@Override
	public  void addReferenceStudentToAnswers(Integer studentId, Integer answerId, Integer examId){testDao.addReferenceStudentToAnswers(studentId,answerId, examId);}

	@Override
	public void deleteReferenceStudentToAnswers(Integer currentStudentId, Integer answerId, Integer examId) {testDao.deleteReferenceStudentToAnswers(currentStudentId,answerId,examId);}

    @Override
    public List<Integer> getStudentIdsByReferences(Integer groupId) {return testDao.getStudentIdsByReferences(groupId);}

    @Override
    public Time getTimeByReference(Integer currentStudentId, Integer groupId) {return testDao.getTimeByReference(currentStudentId,groupId);}

    @Override
	public void deleteReferenceStudentToGroupOfQuestionsByGroupId(Integer groupId) {testDao.deleteReferenceStudentToGroupOfQuestionsByGroupId(groupId);}

	@Override
	public List<Integer> getAnswersIdByStudentId(Integer studentId,Integer examId) {return testDao.getAnswersIdByStudentId(studentId,examId);}

	@Override
	public Boolean checkIfAnswerIsChoosedByStudent(Integer studentId, Integer answerId, Integer examId) {return testDao.checkIfAnswerIsChoosedByStudent(studentId, answerId, examId);}

	@Override
	public void addQuestion(Question question, Integer groupId) {
		testDao.addQuestion(question, groupId);
	}

	@Override
	public void updateQuestion(Question question) {
		testDao.updateQuestion(question);
	}

	@Override
	public void deleteQuestion(Integer questionId) {
		testDao.deleteQuestion(questionId);
	}

	@Override
	public Question getQuestionByQuestionId(Integer questionId) {
		return testDao.getQuestionByQuestionId(questionId);
	}
	
	@Override
	public Boolean checkContentOfQuestion(String contentOfQuestion, Integer testId) {
		return testDao.checkContentOfQuestion(contentOfQuestion, testId);
	}

	@Override
	public void addAnswer(Answer answer, Integer questionId) {
		testDao.addAnswer(answer, questionId);
	}

	@Override
	public void updateAnswer(Answer answer) {
		testDao.updateAnswer(answer);
	}

	@Override
	public void deleteAnswer(Integer answerId) {
		testDao.deleteAnswer(answerId);
	}

	@Override
	public Answer getAnswerByAnswerId(Integer answerId) {
		return testDao.getAnswerByAnswerId(answerId);
	}

	@Override
	public Boolean checkContentOfAnswer(String contentOfAnswer, Integer questionId) {
		return testDao.checkContentOfAnswer(contentOfAnswer, questionId);
	}
	
	@Override
	public void addPicture(Picture picture) {
		testDao.addPicture(picture);
	}

	@Override
	public void updatePicture(Picture picture) {
		testDao.updatePicture(picture);
	}

	@Override
	public void deletePicture(Integer pictureId) {
		testDao.deletePicture(pictureId);
	}

	@Override
	public Picture getPictureByPictureId(Integer pictureId) {
		return testDao.getPictureByPictureId(pictureId);
	}

	@Override
	public Boolean checkPictureName(String pictureName) {
		return testDao.checkPictureName(pictureName);
	}

	@Override
	public void addSetOfRating(SetOfRating setOfRating) {
		testDao.addSetOfRating(setOfRating);
	}

	@Override
	public void updateSetOfRating(SetOfRating setOfRating) {
		testDao.updateSetOfRating(setOfRating);
	}

	@Override
	public void deleteSetOfRating(Integer setOfRatingId) {
		testDao.deleteSetOfRating(setOfRatingId);
	}

	@Override
	public SetOfRating getSetOfRatingBySetId(Integer setOfRatingId) {
		return testDao.getSetOfRatingBySetId(setOfRatingId);
	}

	@Override
	public List<Test> getAllTests(Order order, Integer maxResults) {
		return testDao.getAllTests(order, maxResults);
	}

	@Override
	public List<GroupOfQuestions> getAllGroups(Order order, Integer maxResults) {
		return testDao.getAllGroups(order, maxResults);
	}

	@Override
	public List<Question> getAllQuestions(Order order, Integer maxResult) {
		return testDao.getAllQuestions(order, maxResult);
	}

	@Override
	public List<Answer> getAllAnswers(Order order, Integer maxResults) {
		return testDao.getAllAnswers(order, maxResults);
	}
	
	@Override
	public List<SetOfRating> getAllSetsOfRating(Order order, Integer maxResults) {
		return testDao.getAllSetsOfRating(order, maxResults);
	}

	@Override
	public List<Picture> getAllPictures(Order order, Integer maxResults) {
		return testDao.getAllPictures(order, maxResults);
	}

	@Override
	public List<GroupOfQuestions> getAllGroupsByTestId(Integer testId) {
		return testDao.getAllGroupsByTestId(testId);
	}

	@Override
	public List<Question> getAllQuestionsByGroupId(Integer groupId) {
		return testDao.getAllQuestionsByGroupId(groupId);
	}

	@Override
	public List<Answer> getAllAnswersByQuestionId(Integer questionId) {
		return testDao.getAllAnswersByQuestionId(questionId);
	}

	@Override
	public GroupOfQuestions getGroupByQuestionId(Integer questionId) {
		return testDao.getGroupByQuestionId(questionId);
	}

	@Override
	public Question getQuestionByAnswerId(Integer answerId) {
		return testDao.getQuestionByAnswerId(answerId);
	}

	@Override
	public String generatePassword(){return testDao.generatePassword();}

}
