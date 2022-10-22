package com.project.repository;

import com.project.entity.ExamEntity;
import com.project.entity.QuestionEntity;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ExamRepositoryCustom {
    @Autowired
    private EntityManager entityManager;

    public ExamEntity findExamById(Long examId) {
        ExamEntity exam = entityManager
                .createQuery(
                        "select distinct e " +
                                "from ExamEntity e " +
                                "left join fetch e.questions as questions " +
                                "where e.id = :examId " +
                                "and e.deleted = false " +
                                "and questions.deleted = false ", ExamEntity.class)
                .setParameter("examId", examId)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getSingleResult();

        List<QuestionEntity> questions = exam.getQuestions();

        exam.setQuestions(
                entityManager.createQuery("select distinct q " +
                "from QuestionEntity q " +
                "left join fetch q.answers a " +
                "where q in :questions " +
                "and a.deleted = false ", QuestionEntity.class)
                .setParameter("questions", questions)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getResultList()
        );

        return exam;
    }

    public ExamEntity findExamByIdAndCodeNull(Long examId) {
        ExamEntity exam = entityManager
                .createQuery(
                        "select distinct e " +
                                "from ExamEntity e " +
                                "left join fetch e.questions as questions " +
                                "where e.id = :examId " +
                                "and e.code is null " +
                                "and e.deleted = false " +
                                "and questions.deleted = false ", ExamEntity.class)
                .setParameter("examId", examId)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getSingleResult();

        List<QuestionEntity> questions = exam.getQuestions();

        exam.setQuestions(
                entityManager.createQuery("select distinct q " +
                                "from QuestionEntity q " +
                                "left join fetch q.answers a " +
                                "where q in :questions " +
                                "and a.deleted = false ", QuestionEntity.class)
                        .setParameter("questions", questions)
                        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                        .getResultList()
        );

        return exam;
    }
}
