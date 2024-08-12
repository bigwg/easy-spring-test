package com.github.bigwg.easy.spring.test.test.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

/**
 * ApplicationContextUtil
 *
 * @author zhaozhiwei04
 * @since 2024/06/09
 */
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        setContext(context);
    }

    /**
     * 静态成员赋值
     *
     * @param applicationContext
     * @return
     */
    private static void setContext(ApplicationContext applicationContext) {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    /**
     * 可以使用静态导入，以避免使用ApplicationContextHelper的前置引用。即： ApplicationContextHelper.popBean。<br/>
     * 使用静态导入更加输入的直接： MyService service = popBean(MyService.class); 更加简洁明了
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T popBean(Class<T> clazz) {
        if (Objects.isNull(applicationContext)) {
            return null;
        }

        return applicationContext.getBean(clazz);
    }

    /**
     * 当通过接口多个实现时，需要用 beanName区分
     *
     * @param name  注册的 bean名称
     * @param clazz 注册的 bean类型
     * @param <T>
     * @return
     */
    public static <T> T popBean(String name, Class<T> clazz) {
        if (Objects.isNull(applicationContext)) {
            return null;
        }

        return applicationContext.getBean(name, clazz);
    }
}
