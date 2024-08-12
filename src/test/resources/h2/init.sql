CREATE TABLE IF NOT EXISTS `student` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
      `name` varchar(50) NOT NULL COMMENT '姓名',
      `sex` int(2) NOT NULL COMMENT '性别：1男 2女',
      `last_exam_time` datetime DEFAULT NULL COMMENT '最后一次考试时间',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

CREATE TABLE IF NOT EXISTS `score` (
      `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
      `student_id` int(11) DEFAULT NULL COMMENT '学生id',
      `course` int(11) DEFAULT NULL COMMENT '课程：1语文 2数学 3英语 4生物 5物理 6化学',
      `score` varchar(50) DEFAULT NULL COMMENT '分数',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分数表';

CREATE TABLE IF NOT EXISTS `config` (
    `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
    `config_value` varchar(500) DEFAULT NULL COMMENT '配置详情',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='配置表';