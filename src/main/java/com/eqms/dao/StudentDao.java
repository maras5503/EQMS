package com.eqms.dao;

import com.eqms.model.Students;
import org.hibernate.criterion.Order;

import java.util.List;

public interface StudentDao {
    public void addStudent(Students students);
    public void updateStudent(Students students);
    public void deleteStudent(Integer studentId);
    Students getStudentByStudentId(Integer studentId);
    Students getStudentByEmail(String email);
    Boolean checkStudentEmail(String studentEmail);

    List<Students> getAllStudents(Order order, Integer maxResults);
    List<Students> getStudentsByStudentGroupId(Integer studentgroupId);
}
