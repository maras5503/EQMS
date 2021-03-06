package com.eqms.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "conducted_exams", catalog = "exam_questions_5")
public class ConductedExams {
    private Integer conductedExamId;
    private String testName;
    private String subjectName;
    private GroupsOfStudents groupsOfStudents;
    private  GroupOfQuestions groupOfQuestions;
    private Date examDate;

    public ConductedExams(){

    }


    public ConductedExams(Integer conductedExamId, String testName, String subjectName, GroupsOfStudents groupsOfStudents, GroupOfQuestions groupOfQuestions, Date examDate){
        this.conductedExamId=conductedExamId;
        this.testName=testName;
        this.subjectName=subjectName;
        this.groupsOfStudents=groupsOfStudents;
        this.groupOfQuestions=groupOfQuestions;
        this.examDate=examDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONDUCTED_EXAM_ID")
    public Integer getConductedExamId() {
        return conductedExamId;
    }

    public void setConductedExamId(int conductedExamId) {
        this.conductedExamId = conductedExamId;
    }

    @Basic
    @Column(name = "TEST_NAME")
    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    @Basic
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

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    public GroupOfQuestions getGroupOfQuestions() {
        return groupOfQuestions;
    }

    public void setGroupOfQuestions(GroupOfQuestions groupOfQuestions) {
        this.groupOfQuestions=groupOfQuestions;
    }

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "EXAM_DATE")
    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(java.sql.Date examDate) {
        this.examDate = examDate;
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