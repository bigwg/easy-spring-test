package com.github.bigwg.easy.spring.test.context;

import java.lang.reflect.Field;

/**
 * 待扫描字段判断
 *
 * @author zhaozhiwei
 * @since 2024/3/3
 */
public interface FieldScanJudge {

    boolean judgeScanField(Field field);

}
