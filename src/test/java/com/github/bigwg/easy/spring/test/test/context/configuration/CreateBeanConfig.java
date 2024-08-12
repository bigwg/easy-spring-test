package com.github.bigwg.easy.spring.test.test.context.configuration;

import com.github.bigwg.easy.spring.test.test.context.ServiceA;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CreateBeanConfig
 *
 * @author zhaozhiwei
 * @since 2024/6/8
 */
@Configuration
public class CreateBeanConfig {

    @Bean
    public ServiceA serviceA(){
        return new ServiceA();
    }

}
