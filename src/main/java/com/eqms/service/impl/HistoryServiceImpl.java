package com.eqms.service.impl;

import com.eqms.dao.HistoryDao;
import com.eqms.model.ConductedExams;
import com.eqms.service.HistoryService;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryDao historyDao;

    @Override
    public void addConductedExam(ConductedExams conductedExams) {historyDao.addConductedExam(conductedExams);}

    @Override
    public List<ConductedExams> getAllConductedExams(Order order, Integer maxResults) {return historyDao.getAllConductedExams(order,maxResults);}

    @Override
    public void deleteConductedExam(Integer conductedExamId) {historyDao.deleteConductedExam(conductedExamId);}

    @Override
    public void addExamResult(Integer currentStudentId, double mark, Integer score, Integer conductedExamId) {
        historyDao.addExamResult(currentStudentId, mark, score, conductedExamId);
    }

    @Override
    public void deleteExamResult(Integer conductedExamId) {
        historyDao.deleteExamResult(conductedExamId);
    }
}
