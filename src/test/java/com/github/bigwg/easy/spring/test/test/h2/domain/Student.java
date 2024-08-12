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
public class Student {

    private Long id;

    private String name;

    private Integer sex;

    private LocalDateTime lastExamTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
