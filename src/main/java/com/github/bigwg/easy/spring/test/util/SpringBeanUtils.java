package com.github.bigwg.easy.spring.test.util;

import com.github.bigwg.easy.spring.test.context.FieldClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * SpringBeanUtils
 *
 * @author zhaozhiwei
 * @since 2024/3/21
 */
public class SpringBeanUtils {

    /**
     * 获取被注入的 Spring Bean 名称
     *
     * @param fieldClass
     * @return
     */
    public static String getInjectedBeanName(FieldClass fieldClass) {
        Field field = fieldClass.getField();
        if (Objects.isNull(field)) {
            return "";
        }
        Resource resource = field.getAnnotation(Resource.class);
        if (Objects.nonNull(resource)) {
            return StringUtils.isNotBlank(resource.name()) ? resource.name() : field.getName();
        }
        Autowired autowired = field.getAnnotation(Autowired.class);
        Qualifier qualifier = field.getAnnotation(Qualifier.class);
        if (Objects.nonNull(autowired) && Objects.nonNull(qualifier)) {
            return qualifier.value();
        }
        return "";
    }

}
