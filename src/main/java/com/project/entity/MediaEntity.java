package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "`media`")
@Getter
@Setter
public class MediaEntity extends BaseEntity{
    @Column
    private String path;
    @Column
    private String type; // image or video

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
}
