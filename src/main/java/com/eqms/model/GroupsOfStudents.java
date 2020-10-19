package com.eqms.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "groups_of_students", catalog = "exam_questions_5")
public class GroupsOfStudents implements java.io.Serializable {
    private Integer studentgroupId;
    private String studentgroupName;
    private Set<Students> studentses=new HashSet<Students>(0);

    public  GroupsOfStudents(){

    }

    public GroupsOfStudents(String studentgroupName){
        this.studentgroupName=studentgroupName;
    }

    public GroupsOfStudents(String studentgroupName, Set<Students> studentses){
        this.studentgroupName=studentgroupName;
        this.studentses=studentses;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "groupsOfStudents")

    public Set<Students> getStudentses(){return studentses;}

    public void setStudentses(Set<Students> studentses) {
        this.studentses = studentses;
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
