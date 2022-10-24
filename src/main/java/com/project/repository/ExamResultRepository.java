package com.project.repository;

import com.project.entity.ExamResultEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResultEntity, Long>{

    @Query(value = "select * from exam_result as ex" +
            "where (:examId is null or ex.exam_id = :examId) " +
            "and (:minPoint is null or ex.points >= :minPoint) " +
            "and (:maxPoint is null or ex.points <= :maxPoint) " +
            "and (:longTime is null or time_to_sec(timediff(ex.end_time,ex.start_time)) <= :longTime);"
            ,nativeQuery = true)
    List<ExamResultEntity> findExamResultsByParamNative(Long examId, Integer minPoint, Integer maxPoint, Integer longTime);

    Page<ExamResultEntity> findBySubmittedTrueAndUser_Id(Long userId, Pageable pageable);

}
