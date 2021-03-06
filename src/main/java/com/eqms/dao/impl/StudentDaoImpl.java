package com.eqms.dao.impl;

import com.eqms.dao.StudentDao;
import org.apache.log4j.Logger;
import com.eqms.model.Students;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private SessionFactory sessionFactory;
    protected static Logger logger = Logger.getLogger("dao");

    @Override
    public void addStudent(Students students) {
        getSessionFactory().getCurrentSession().save(students);
    }

    @Override
    public void updateStudent(Students students) {
        getSessionFactory().getCurrentSession().update(students);
    }

    @Override
    public void deleteStudent(Integer studentId) {

        String queryStringDeleteReferencesToAnswers = "DELETE FROM STUDENTS_ANSWERS WHERE STUDENT_ID=?";
        SQLQuery queryDeleteReferencesToAnswers=getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteReferencesToAnswers);
        queryDeleteReferencesToAnswers.setParameter(0, studentId);
        queryDeleteReferencesToAnswers.executeUpdate();

        String queryStringDeleteReferencesToGroups = "DELETE FROM STUDENTS_GROUPS_OF_QUESTIONS WHERE STUDENT_ID=?";
        SQLQuery queryDeleteReferencesToGroups=getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteReferencesToGroups);
        queryDeleteReferencesToGroups.setParameter(0, studentId);
        queryDeleteReferencesToGroups.executeUpdate();

        String queryStringDeleteExamResult = "DELETE FROM EXAM_RESULTS WHERE STUDENT_ID=?";
        SQLQuery queryDeleteExamResult=getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteExamResult);
        queryDeleteExamResult.setParameter(0, studentId);
        queryDeleteExamResult.executeUpdate();

        String queryStringDeleteStudent = "DELETE FROM STUDENTS WHERE STUDENT_ID = ?";
        SQLQuery queryDeleteStudent = getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteStudent);
        queryDeleteStudent.setParameter(0, studentId);

        int numberDeletedUpdatedEntities = queryDeleteStudent.executeUpdate();
        logger.debug("The number of entities updated or deleted (students): " + String.valueOf(numberDeletedUpdatedEntities));
    }

    @Override
    public Students getStudentByStudentId(Integer studentId) {
        List<Students> students = new ArrayList<Students>();

        String queryString = "SELECT * FROM STUDENTS WHERE STUDENT_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Students.class);
        query.setParameter(0, studentId);
        students = query.list();

        return students.get(0);
    }

    @Override
    public Students getStudentByEmail(String email) {
        List<Students> students =new ArrayList<Students>();

        String queryString = "SELECT * FROM STUDENTS WHERE E_MAIL = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Students.class);
        query.setParameter(0,email);
        students=query.list();

        return  students.get(0);
    }

    @Override
    public Boolean checkStudentEmail(String studentEmail) {
        String queryString = "SELECT COUNT(*) FROM STUDENTS WHERE E_MAIL = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0, studentEmail);

        int studentEmailExists = Integer.valueOf(query.list().get(0).toString());
        logger.debug("studentNameExists value = " + studentEmailExists);

        if(studentEmailExists == 0) {
            return false; 	// studentName doesn't exist
        } else {
            return true;	// studentName exists
        }
    }

    @Override
    public List<Students> getAllStudents(Order order, Integer maxResults) {
        List<Students> students = new ArrayList<Students>();

        if (maxResults != null) {
            students = getSessionFactory().getCurrentSession().createCriteria(Students.class).addOrder(order).setMaxResults(maxResults).list();
        } else {
            students = getSessionFactory().getCurrentSession().createCriteria(Students.class).addOrder(order).list();
        }

        return students;
    }

    @Override
    public List<Students> getStudentsByStudentGroupId(Integer studentgroupId) {
        List<Students> students = new ArrayList<Students>();

        String queryString = "SELECT * FROM STUDENTS WHERE STUDENTGROUP_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(Students.class);
        query.setParameter(0, studentgroupId);
        students = query.list();
        return students;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}

