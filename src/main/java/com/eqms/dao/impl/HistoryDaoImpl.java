package com.eqms.dao.impl;

import com.eqms.dao.HistoryDao;
import com.eqms.model.ConductedExams;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HistoryDaoImpl implements HistoryDao {

    @Autowired
    private SessionFactory sessionFactory;
    protected static Logger logger = Logger.getLogger("dao");


    @Override
    public void addConductedExam(ConductedExams conductedExams) {getSessionFactory().getCurrentSession().save(conductedExams); }

    @Override
    public List<ConductedExams> getAllConductedExams(Order order, Integer maxResults) {
        List<ConductedExams> conductedExams = new ArrayList<ConductedExams>();

        if (maxResults != null) {
            conductedExams = getSessionFactory().getCurrentSession().createCriteria(ConductedExams.class).addOrder(order).setMaxResults(maxResults).list();
        } else {
            conductedExams = getSessionFactory().getCurrentSession().createCriteria(ConductedExams.class).addOrder(order).list();
        }

        return conductedExams;
    }

    @Override
    public void deleteConductedExam(Integer conductedExamId) {

        String query = "DELETE FROM CONDUCTED_EXAMS WHERE CONDUCTED_EXAM_ID = ?";
        SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
        sqlQuery.setParameter(0, conductedExamId);

        int numberDeletedUpdatedEntities = sqlQuery.executeUpdate();
        logger.debug("The number of entities updated or deleted (conducted exams): " + String.valueOf(numberDeletedUpdatedEntities));
    }

    @Override
    public void addExamResult(Integer currentStudentId, double mark, Integer score, Integer conductedExamId) {
        String query = "INSERT INTO EXAM_RESULTS VALUES (?, ? ,? ,?)";
        SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
        sqlQuery.setParameter(0, currentStudentId);
        sqlQuery.setParameter(1, mark);
        sqlQuery.setParameter(2, score);
        sqlQuery.setParameter(3, conductedExamId);

        int numberDeletedUpdatedEntities = sqlQuery.executeUpdate();
        logger.debug("The number of entities updated or deleted (conducted exams): " + String.valueOf(numberDeletedUpdatedEntities));
    }

    @Override
    public void deleteExamResult(Integer conductedExamId) {
        String query = "DELETE FROM EXAM_RESULTS WHERE CONDUCTED_EXAM_ID = ?";
        SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(query);
        sqlQuery.setParameter(0, conductedExamId);

        int numberDeletedUpdatedEntities = sqlQuery.executeUpdate();
        logger.debug("The number of entities updated or deleted (conducted exams): " + String.valueOf(numberDeletedUpdatedEntities));
    }

    @Override
    public ConductedExams getConductedExamByConductedExamId(Integer conductedExamId) {
        List<ConductedExams> conductedExams = new ArrayList<ConductedExams>();

        String queryString = "SELECT * FROM CONDUCTED_EXAMS WHERE CONDUCTED_EXAM_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(ConductedExams.class);
        query.setParameter(0, conductedExamId);
        conductedExams = query.list();

        return conductedExams.get(0);
    }

    @Override
    public Map<Integer, Integer> getExamResultsByConductedExamId(Integer conductedExamId) {
        Map<Integer, Integer> examResults= new HashMap<Integer, Integer>();

        String queryString = "SELECT STUDENT_ID FROM EXAM_RESULTS WHERE CONDUCTED_EXAM_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0, conductedExamId);
        List<Integer> studentIds = query.list();

        queryString = "SELECT SCORE FROM EXAM_RESULTS WHERE CONDUCTED_EXAM_ID = ?";
        query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0,conductedExamId);
        List<Integer> scores = query.list();

        int i=0;
        for(Integer s:studentIds){
            examResults.put(s,scores.get(i));
            i++;
        }

        return examResults;
    }

    @Override
    public Map<Integer, Double> getExamMarksByConductedExamId(Integer conductedExamId) {
        Map<Integer, Double> examMarks= new HashMap<Integer, Double>();

        String queryString = "SELECT STUDENT_ID FROM EXAM_RESULTS WHERE CONDUCTED_EXAM_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0, conductedExamId);
        List<Integer> studentIds = query.list();

        queryString = "SELECT MARK FROM EXAM_RESULTS WHERE CONDUCTED_EXAM_ID = ?";
        query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0,conductedExamId);
        List<Double> marks = query.list();

        int i=0;
        for(Integer s:studentIds){
            examMarks.put(s,marks.get(i));
            i++;
        }

        return examMarks;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
