package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`exam_result`")
@Getter
@Setter
public class ExamResultEntity extends  BaseEntity{
    @Column
    private Integer points;
    @Column
    private Date start_time;
    @Column
    private Date end_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private ExamEntity exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany
    private List<QuestionResultEntity> questionResults;
}
