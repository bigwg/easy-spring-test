package com.github.bigwg.easy.spring.test.test.h2.dao;

import com.github.bigwg.easy.spring.test.test.h2.domain.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao {

    Student queryById(Long id);

    int updateById(Student record);

    int batchInsert(@Param("students") List<Student> students);

}