package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "exam",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<QuestionEntity> questions;

	@OneToMany(mappedBy = "exam")
	private List<ExamResultEntity> examResults;

	@ManyToMany(mappedBy = "exams")
	private List<UserEntity> user;

}
