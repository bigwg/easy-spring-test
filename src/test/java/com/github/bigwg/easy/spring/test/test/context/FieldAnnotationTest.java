package com.github.bigwg.easy.spring.test.test.context;

import com.github.bigwg.easy.spring.test.context.AutoImportsContextCustomizer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 属性注解测试
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
public class FieldAnnotationTest {

    @Autowired
    private AutoImportsContextCustomizer autoImportsContextCustomizer;

    @Test
    public void getFieldAnnotationTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getDeclaredFields = ReflectionUtils.class.getDeclaredMethod("getDeclaredFields", Class.class);
        getDeclaredFields.setAccessible(true);
        Field[] fields = (Field[])getDeclaredFields.invoke(ReflectionUtils.class, FieldAnnotationTest.class);
        for (Field field : fields) {
            Class<?> type = field.getType();
            Autowired annotation = field.getAnnotation(Autowired.class);
            System.out.println(annotation == null);
        }
    }

}
