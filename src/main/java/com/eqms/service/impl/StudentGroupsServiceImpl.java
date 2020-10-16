package com.eqms.service.impl;
import java.util.List;

import com.eqms.dao.StudentGroupsDao;
import com.eqms.model.GroupsOfStudents;
import com.eqms.service.StudentGroupsService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class StudentGroupsServiceImpl implements StudentGroupsService {

    @Autowired
    private StudentGroupsDao studentGroupsDao;
    @Override
    public void addStudentGroup(GroupsOfStudents groupsOfStudents) {
        studentGroupsDao.addStudentGroup(groupsOfStudents);
    }

    @Override
    public void updateStudentGroup(GroupsOfStudents groupsOfStudents) {
        studentGroupsDao.updateStudentGroup(groupsOfStudents);
    }

    @Override
    public void deleteStudentGroup(Integer studentgroupId) {
        studentGroupsDao.deleteStudentGroup(studentgroupId);
    }

    @Override
    public GroupsOfStudents getStudentGroupByStudentGroupId(Integer studentgroupId) {

        return studentGroupsDao.getStudentGroupByStudentGroupId(studentgroupId);
    }

    @Override
    public Boolean checkStudentGroupName(String studentgroupName) {

        return studentGroupsDao.checkStudentGroupName(studentgroupName);
    }

    @Override
    public List<GroupsOfStudents> getAllStudentGroups(Order order, Integer maxResults) {

        return studentGroupsDao.getAllStudentGroups(order, maxResults);
    }
}
