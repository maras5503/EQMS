package com.eqms.dao;

import com.eqms.model.Students;
import org.hibernate.criterion.Order;

import java.util.List;

public interface StudentDao {
    public void addStudent(Students students);
    public void updateStudentGroup(Students students);
    public void deleteStudentGroup(Integer studentId);
    Students getStudentByStudentId(Integer studentId);
    Boolean checkStudentEmail(String studentEmail);

    List<Students> getAllStudents(Order order, Integer maxResults);
}
