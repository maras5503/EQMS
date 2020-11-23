package com.eqms.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "conducted_exams", catalog = "exam_questions_5")
public class ConductedExams {
    private Integer conductedExamId;
    private String testName;
    private String subjectName;
    private GroupsOfStudents groupsOfStudents;
    private Date examDate;

    public ConductedExams(){

    }

    public ConductedExams(Integer conductedExamId, String testName, String subjectName, GroupsOfStudents groupsOfStudents, Date examDate){
        this.conductedExamId=conductedExamId;
        this.testName=testName;
        this.subjectName=subjectName;
        this.groupsOfStudents=groupsOfStudents;
        this.examDate=examDate;
    }


    @Id
    @Column(name = "CONDUCTED_EXAM_ID")
    public int getConductedExamId() {
        return conductedExamId;
    }

    public void setConductedExamId(int conductedExamId) {
        this.conductedExamId = conductedExamId;
    }

    @Column(name = "TEST_NAME")
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Column(name = "SUBJECT_NAME")
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @ManyToOne
    @JoinColumn(name = "STUDENTGROUP_ID")
    public GroupsOfStudents getGroupsOfStudents() {
        return this.groupsOfStudents;
    }

    public void setGroupsOfStudents(GroupsOfStudents groupsOfStudents) {
        this.groupsOfStudents = groupsOfStudents;
    }

    @Basic
    @Column(name = "EXAM_DATE")
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConductedExams that = (ConductedExams) o;
        return conductedExamId == that.conductedExamId &&
                Objects.equals(testName, that.testName) &&
                Objects.equals(subjectName, that.subjectName) &&
                Objects.equals(groupsOfStudents, that.groupsOfStudents) &&
                Objects.equals(examDate, that.examDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conductedExamId, testName, subjectName, groupsOfStudents, examDate);
    }
}
