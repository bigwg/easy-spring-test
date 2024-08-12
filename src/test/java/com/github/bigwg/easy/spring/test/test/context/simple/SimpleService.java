package com.github.bigwg.easy.spring.test.test.context.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * SimpleService
 *
 * @author zhaozhiwei
 * @since 2023/4/25
 */
@Service
public class SimpleService {

    @Autowired
    private SimpleServiceA simpleServiceA;
    @Autowired(required = false)
    private SimpleServiceB simpleServiceB;
    @Autowired(required = false)
    private AbstractService abstractService;
    @Autowired(required = false)
    private InterfaceService interfaceService;

    public String hello() {
        return "SimpleService";
    }

    public String helloSimpleServiceA() {
        return "invoke SimpleService.helloSimpleServiceA: " + simpleServiceA.hello();
    }

    public String helloSimpleServiceB() {
        return "invoke SimpleService.helloSimpleServiceB: " + simpleServiceB.hello();
    }

    public String helloAbstractService() {
        return "invoke SimpleService.helloAbstractService: " + abstractService.hello();
    }

    public String helloInterfaceService() {
        return "invoke SimpleService.helloInterfaceService: " + interfaceService.hello();
    }

}
