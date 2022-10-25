package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`exam`")
@Getter
@Setter
@Accessors(chain = true)
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
public class ExamEntity extends BaseEntity {
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

    @Column(name = "total_time")
    private Integer totalTime = 60;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<QuestionEntity> questions;

    @OneToMany(mappedBy = "exam")
    private List<ExamResultEntity> examResults;

    @ManyToMany(mappedBy = "exams")
    private List<UserEntity> user;

    @Transient
    private Long totalExamResults;

	public ExamEntity(Long id, String title, String description, String type, String code, Date startFrom, Date endTo) {
		this.setId(id);
		this.title = title;
		this.description = description;
		this.type = type;
		this.code = code;
		this.startFrom = startFrom;
		this.endTo = endTo;
        //this.totalExamResults = totalExamResults;
	}

	public ExamEntity() {

	}
}
