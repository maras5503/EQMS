package com.eqms.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.eqms.dao.StudentGroupsDao;
import com.eqms.model.Students;
import com.eqms.service.StudentService;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eqms.model.GroupsOfStudents;

@Repository
public class StudentGroupsDaoImpl implements StudentGroupsDao {

    @Autowired
    private SessionFactory sessionFactory;
    protected static Logger logger = Logger.getLogger("dao");
    @Autowired
    private StudentService studentService;

    @Override
    public void addStudentGroup(GroupsOfStudents groupsOfStudents) {
        // Adding new group to database
        groupsOfStudents.setStudentgroupId(0);
        getSessionFactory().getCurrentSession().save(groupsOfStudents);

        // Finding added group
        GroupsOfStudents addedGroup = getAllStudentGroups(Order.desc("studentgroupId"), 1).get(0);


        //int numberDeletedUpdatedEntities = query.executeUpdate();
        //logger.debug("The number of entities updated or deleted = " + String.valueOf(numberDeletedUpdatedEntities));
    }

    @Override
    public void updateStudentGroup(GroupsOfStudents groupsOfStudents) {
        getSessionFactory().getCurrentSession().update(groupsOfStudents);
    }

    @Override
    public void deleteStudentGroup(Integer studentgroupId) {

        // Deleting all students from the group
        List<Students> students = getStudentService().getStudentsByStudentGroupId(studentgroupId);

        logger.debug("Loop with tests to deleting");
        for(Students student : students) {
            getStudentService().deleteStudent(student.getStudentId());
        }

        String queryStringDeleteConductedExam = "DELETE FROM CONDUCTED_EXAMS WHERE STUDENTGROUP_ID = ?";
        SQLQuery queryDeleteConductedExam = getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteConductedExam);
        queryDeleteConductedExam.setParameter(0, studentgroupId);
        queryDeleteConductedExam.executeUpdate();

        // Finally removing studentgroup
        String queryStringDeleteSubject = "DELETE FROM GROUPS_OF_STUDENTS WHERE STUDENTGROUP_ID = ?";
        SQLQuery queryDeleteStudentGroup = getSessionFactory().getCurrentSession().createSQLQuery(queryStringDeleteSubject);
        queryDeleteStudentGroup.setParameter(0, studentgroupId);

        int numberDeletedUpdatedEntities = queryDeleteStudentGroup.executeUpdate();
        logger.debug("The number of entities updated or deleted (subjects): " + String.valueOf(numberDeletedUpdatedEntities));
    }


    @Override
    public GroupsOfStudents getStudentGroupByStudentGroupId(Integer studentgroupId) {

        List<GroupsOfStudents> studentgroups = new ArrayList<GroupsOfStudents>();

        String queryString = "SELECT * FROM GROUPS_OF_STUDENTS WHERE STUDENTGROUP_ID = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString).addEntity(GroupsOfStudents.class);
        query.setParameter(0, studentgroupId);
        studentgroups = query.list();

        return studentgroups.get(0);
    }

    @Override
    public Boolean checkStudentGroupName(String studentgroupName) {

        String queryString = "SELECT COUNT(*) FROM GROUPS_OF_STUDENTS WHERE STUDENTGROUP_NAME = ?";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(queryString);
        query.setParameter(0, studentgroupName);

        int studentGroupNameExists = Integer.valueOf(query.list().get(0).toString());
        logger.debug("studentGroupNameExists value = " + studentGroupNameExists);

        if(studentGroupNameExists == 0) {
            return false; 	// studentGroupName doesn't exist
        } else {
            return true;	// studentGroupName exists
        }
    }

    @Override
    public List<GroupsOfStudents> getAllStudentGroups(Order order, Integer maxResults) {

        List<GroupsOfStudents> studentgroups = new ArrayList<GroupsOfStudents>();

        if (maxResults != null) {
            studentgroups = getSessionFactory().getCurrentSession().createCriteria(GroupsOfStudents.class).addOrder(order).setMaxResults(maxResults).list();
        } else {
            studentgroups = getSessionFactory().getCurrentSession().createCriteria(GroupsOfStudents.class).addOrder(order).list();
        }

        return studentgroups;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public StudentService getStudentService() {
        return studentService;
    }

}


