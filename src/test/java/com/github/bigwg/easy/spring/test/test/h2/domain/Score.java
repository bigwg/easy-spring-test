package com.github.bigwg.easy.spring.test.test.h2.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Student
 *
 * @author zhaozhiwei
 * @since 2023/5/16
 */
@Getter
@Setter
@ToString
public class Score {

    private Long id;

    private Long studentId;

    private Integer course;

    private String score;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
