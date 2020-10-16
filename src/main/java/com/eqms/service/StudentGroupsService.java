package com.eqms.service;

import com.eqms.model.GroupsOfStudents;

import org.hibernate.criterion.Order;
import java.util.List;

public interface StudentGroupsService {

    public void addStudentGroup(GroupsOfStudents groupsOfStudents);
    public void updateStudentGroup(GroupsOfStudents groupsOfStudents);
    public void deleteStudentGroup(Integer studentgroupId);
    GroupsOfStudents getStudentGroupByStudentGroupId(Integer studentgroupId);
    Boolean checkStudentGroupName(String studentgroupName);

    List<GroupsOfStudents> getAllStudentGroups(Order order, Integer maxResults);
}
