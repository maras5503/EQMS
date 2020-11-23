package com.eqms.service;

import com.eqms.model.ConductedExams;
import org.hibernate.criterion.Order;

import java.util.List;

public interface HistoryService {
    public void addConductedExam(ConductedExams conductedExams);
    List<ConductedExams> getAllConductedExams(Order order, Integer maxResults);
}
