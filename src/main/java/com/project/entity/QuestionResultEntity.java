package com.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`question_result`")
@Getter
@Setter
public class QuestionResultEntity extends BaseEntity{
    @Column
    private String content; // trac nghiem hoac dien text

    @Column
    private String type;

    @Column
    private Integer point;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

   /* @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;*/

    @ManyToOne
    @JsonIgnore
    private ExamResultEntity examResult;

    @OneToMany(fetch = FetchType.LAZY)
    private List<AnswerEntity> answers;
}
