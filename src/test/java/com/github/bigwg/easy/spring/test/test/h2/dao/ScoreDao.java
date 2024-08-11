package com.github.bigwg.easy.spring.test.test.h2.dao;

import com.github.bigwg.easy.spring.test.test.h2.domain.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreDao {

    List<Score> queryByStudentId(Long studentId);

    Score queryById(Long id);

    int updateById(Score record);

    int batchInsert(@Param("scores") List<Score> scores);

}