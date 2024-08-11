package com.github.bigwg.easy.spring.test.test.h2.service;

import com.github.bigwg.easy.spring.test.test.h2.dao.ScoreDao;
import com.github.bigwg.easy.spring.test.test.h2.dao.StudentDao;
import com.github.bigwg.easy.spring.test.test.h2.domain.Score;
import com.github.bigwg.easy.spring.test.test.h2.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * StudentScoreService
 *
 * @author zhaozhiwei
 * @since 2023/5/17
 */
@Service
public class StudentScoreService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ScoreDao scoreDao;

    /**
     * 考试
     */
    @Transactional
    public void exam(Long studentId, LocalDateTime examTime, Integer course, String scoreNum) {
        Student student = new Student();
        student.setId(studentId);
        student.setLastExamTime(examTime);
        studentDao.updateById(student);
        Score score = new Score();
        score.setStudentId(studentId);
        score.setCourse(course);
        score.setScore(scoreNum);
        scoreDao.batchInsert(Collections.singletonList(score));
    }

}
