package com.eqms.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students", catalog = "exam_questions_5", uniqueConstraints = @UniqueConstraint(columnNames = "E_MAIL"))
public class Students {
    private int studentId;
    private String studentFirstname;
    private String studentLastname;
    private String studentEmail;
    private GroupsOfStudents groupsOfStudents;
    private Set<GroupOfQuestions> groupOfQuestionses;

    public Students(){}

    public Students(String studentFirstname, String studentLastname, String studentEmail, GroupsOfStudents groupsOfStudents){
        this.studentFirstname=studentFirstname;
        this.studentLastname=studentLastname;
        this.studentEmail=studentEmail;
        this.groupsOfStudents=groupsOfStudents;
    }

    public Students(String studentFirstname, String studentLastname, String studentEmail, GroupsOfStudents groupsOfStudents, Set<GroupOfQuestions> groupOfQuestionses){
        this.studentFirstname=studentFirstname;
        this.studentLastname=studentLastname;
        this.studentEmail=studentEmail;
        this.groupsOfStudents=groupsOfStudents;
        this.groupOfQuestionses=groupOfQuestionses;
    }

    @Id
    @Column(name = "STUDENT_ID")
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Column(name = "FIRSTNAME")
    public String getStudentFirstname() {
        return studentFirstname;
    }

    public void setStudentFirstname(String studentFirstname) {
        this.studentFirstname = studentFirstname;
    }


    @Column(name = "LASTNAME")
    public String getStudentLastname() {
        return studentLastname;
    }

    public void setStudentLastname(String studentLastname) {
        this.studentLastname = studentLastname;
    }


    @Column(name = "E_MAIL")
    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    @ManyToOne
    @JoinColumn(name = "STUDENTGROUP_ID")
    public GroupsOfStudents getGroupsOfStudents() {
        return this.groupsOfStudents;
    }

    public void setGroupsOfStudents(GroupsOfStudents groupsOfStudents) {
        this.groupsOfStudents = groupsOfStudents;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_groups_of_questions", catalog = "exam_questions_5", joinColumns = {
            @JoinColumn(name = "STUDENT_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false) })
    public Set<GroupOfQuestions> getGroupOfQuestionses() {
        return this.groupOfQuestionses;
    }

    public void setGroupOfQuestionses(Set<GroupOfQuestions> groupOfQuestionses) {
        this.groupOfQuestionses=groupOfQuestionses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Students students = (Students) o;
        return studentId == students.studentId &&
                groupsOfStudents == students.groupsOfStudents &&
                Objects.equals(studentFirstname, students.studentFirstname) &&
                Objects.equals(studentLastname, students.studentLastname) &&
                Objects.equals(studentEmail, students.studentEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentFirstname, studentLastname, studentEmail, groupsOfStudents);
    }
}
