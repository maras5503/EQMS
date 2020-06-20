package com.eqms.model;
// default package
// Generated 2015-11-14 00:29:40 by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Subjects generated by hbm2java
 */
@Entity
@Table(name = "subjects", catalog = "exam_questions_5", uniqueConstraints = @UniqueConstraint(columnNames = "SUBJECT_NAME") )
public class Subject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer subjectId;
	private String subjectName;
	private Date creationDate;
	private String createdBy;
	private Set<Test> testses = new HashSet<Test>(0);
	private Set<User> userses = new HashSet<User>(0);

	public Subject() {
	}

	public Subject(String subjectName, Date creationDate, String createdBy) {
		this.subjectName = subjectName;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}

	public Subject(String subjectName, Date creationDate, String createdBy, Set<Test> testses, Set<User> userses) {
		this.subjectName = subjectName;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.testses = testses;
		this.userses = userses;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SUBJECT_ID", unique = true, nullable = false)
	public Integer getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	@Column(name = "SUBJECT_NAME", unique = true, nullable = false, length = 100)
	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATION_DATE", nullable = false, length = 10)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 70)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subjects")
	public Set<Test> getTestses() {
		return this.testses;
	}

	public void setTestses(Set<Test> testses) {
		this.testses = testses;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "subjectses")
	public Set<User> getUserses() {
		return this.userses;
	}

	public void setUserses(Set<User> userses) {
		this.userses = userses;
	}

}
