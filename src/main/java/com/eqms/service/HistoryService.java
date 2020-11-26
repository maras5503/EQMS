package com.eqms.service;

import com.eqms.model.ConductedExams;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Map;

public interface HistoryService {
    public void addConductedExam(ConductedExams conductedExams);

    List<ConductedExams> getAllConductedExams(Order order, Integer maxResults);

    void deleteConductedExam(Integer conductedExamId);

    void addExamResult(Integer currentStudentId, double mark, Integer score, Integer conductedExamId);

    void deleteExamResult(Integer conductedExamId);

    ConductedExams getConductedExamByConductedExamId(Integer conductedExamId);

    Map <Integer,Integer> getExamResultsByConductedExamId(Integer conductedExamId);

    Map<Integer, Double> getExamMarksByConductedExamId(Integer conductedExamId);
}
