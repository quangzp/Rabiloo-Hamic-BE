package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`exam`")
@Getter
@Setter
@Accessors(chain = true)
public class ExamEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String title;
	@Column(columnDefinition = "TEXT")
	private String description;
	@Column
	private String type;
	@Column(unique = true)
	private String code;
	@Column
	private Date startFrom;
	@Column
	private Date endTo;

	@OneToMany(mappedBy = "exam",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<QuestionEntity> questions;

	@OneToMany(mappedBy = "exam")
	private List<ExamResultEntity> examResults;

	@ManyToMany(mappedBy = "exams")
	private List<UserEntity> user;

	public ExamEntity(Long id, String title, String description, String type, String code, Date startFrom, Date endTo) {
		this.setId(id);
		this.title = title;
		this.description = description;
		this.type = type;
		this.code = code;
		this.startFrom = startFrom;
		this.endTo = endTo;
	}

	public ExamEntity() {

	}
}
