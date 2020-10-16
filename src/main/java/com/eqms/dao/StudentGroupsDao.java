package com.eqms.dao;

import java.util.List;

import org.hibernate.criterion.Order;
import com.eqms.model.GroupsOfStudents;
public interface StudentGroupsDao {
    public void addStudentGroup(GroupsOfStudents groupsOfStudents);
    public void updateStudentGroup(GroupsOfStudents groupsOfStudents);
    public void deleteStudentGroup(Integer studentgroupId);
    GroupsOfStudents getStudentGroupByStudentGroupId(Integer studentgroupId);
    Boolean checkStudentGroupName(String studentgroupName);

    List<GroupsOfStudents> getAllStudentGroups(Order order, Integer maxResults);
}
