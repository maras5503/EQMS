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
import java.util.List;

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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
