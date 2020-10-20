package com.eqms.service.impl;

import com.eqms.dao.StudentDao;
import com.eqms.model.Students;
import com.eqms.service.StudentService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;

    @Override
    public void addStudent(Students students) {
        studentDao.addStudent(students);
    }

    @Override
    public void updateStudent(Students students) {
        studentDao.updateStudent(students);
    }

    @Override
    public void deleteStudent(Integer studentId) {
        studentDao.deleteStudent(studentId);
    }

    @Override
    public Students getStudentByStudentId(Integer studentId) {
        return studentDao.getStudentByStudentId(studentId);
    }

    @Override
    public Boolean checkStudentEmail(String studentEmail) {
        return studentDao.checkStudentEmail(studentEmail);
    }

    @Override
    public List<Students> getAllStudents(Order order, Integer maxResults) {
        return studentDao.getAllStudents(order,maxResults);
    }

    @Override
    public List<Students> getStudentsByStudentGroupId(Integer studentgroupId) {
        return studentDao.getStudentsByStudentGroupId(studentgroupId);
    }
}
