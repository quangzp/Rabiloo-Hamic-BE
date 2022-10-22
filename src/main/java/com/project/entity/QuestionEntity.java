package com.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`questions`")
@Getter
@Setter
@Where(clause = "deleted = false")
//@EqualsAndHashCode(callSuper = false)
public class QuestionEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*@Column
	private String title;*/
	@Column(columnDefinition = "TEXT")
	private String content;
	@Column
	@Enumerated(EnumType.STRING)
	private QuestionType type;
	@Column
	private Integer level;
	@Column
	private Integer maxPoint;

	@LazyCollection(LazyCollectionOption.TRUE)
	@OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
	private Set<AnswerEntity> answers;

	@OneToMany(mappedBy = "question")
	private Set<MediaEntity> images;

	@OneToMany(mappedBy = "question")
	private List<QuestionResultEntity> questionResult;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_id")
	@JsonIgnore
	private ExamEntity exam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryEntity category;

	private String answerWithTextsResult; // Lien Xo|Nga|Lien Bang Nga

	private boolean requireAdminGiveGrade; // default values false
}
