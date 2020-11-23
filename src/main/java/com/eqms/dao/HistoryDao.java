package com.eqms.dao;

import com.eqms.model.ConductedExams;
import org.hibernate.criterion.Order;

import java.util.List;

public interface HistoryDao {
    public void addConductedExam(ConductedExams conductedExams);
    List<ConductedExams> getAllConductedExams(Order order, Integer maxResults);
}
