package com.eqms.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "groups_of_students", schema = "exam_questions_5", catalog = "")
public class GroupsOfStudents implements java.io.Serializable {
    private Integer studentgroupId;
    private String studentgroupName;

    public  GroupsOfStudents(){

    }

    public GroupsOfStudents(String studentgroupName){
        this.studentgroupName=studentgroupName;
    }

    @Id
    @Column(name = "STUDENTGROUP_ID")
    public int getStudentgroupId() {
        return studentgroupId;
    }

    public void setStudentgroupId(int studentgroupId) {
        this.studentgroupId = studentgroupId;
    }

    @Basic
    @Column(name = "STUDENTGROUP_NAME")
    public String getStudentgroupName() {
        return studentgroupName;
    }

    public void setStudentgroupName(String studentgroupName) {
        this.studentgroupName = studentgroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsOfStudents that = (GroupsOfStudents) o;
        return studentgroupId == that.studentgroupId &&
                Objects.equals(studentgroupName, that.studentgroupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentgroupId, studentgroupName);
    }
}
