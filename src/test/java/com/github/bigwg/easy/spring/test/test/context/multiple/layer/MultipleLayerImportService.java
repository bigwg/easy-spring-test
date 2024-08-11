package com.github.bigwg.easy.spring.test.test.context.multiple.layer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * MultipleImportService
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
@Service
public class MultipleLayerImportService {

    @Autowired
    private Map<String, MultipleLayerBeanServiceI> multipleLayerBeanServiceIMap;

    public String invokeMultipleLayerServiceA() {
        MultipleLayerBeanServiceI multipleLayerBeanServiceA = multipleLayerBeanServiceIMap.get("multipleLayerServiceA");
        return "invoke MultipleLayerServiceA: " + multipleLayerBeanServiceA.hello();
    }

    public String invokeMultipleLayerServiceB() {
        MultipleLayerBeanServiceI multipleLayerBeanServiceB = multipleLayerBeanServiceIMap.get("multipleLayerServiceB");
        return "invoke MultipleLayerServiceB: " + multipleLayerBeanServiceB.hello();
    }

    public String invokeAMultipleServiceA() {
        MultipleLayerServiceA multipleLayerServiceA = (MultipleLayerServiceA) multipleLayerBeanServiceIMap.get("multipleLayerServiceA");
        return multipleLayerServiceA.helloMultipleServiceA();
    }

    public String invokeAMultipleServiceB() {
        MultipleLayerServiceA multipleLayerServiceA = (MultipleLayerServiceA) multipleLayerBeanServiceIMap.get("multipleLayerServiceA");
        return multipleLayerServiceA.helloMultipleServiceB();
    }

    public String invokeBMultipleServiceA() {
        MultipleLayerServiceB multipleLayerServiceB = (MultipleLayerServiceB) multipleLayerBeanServiceIMap.get("multipleLayerServiceB");
        return multipleLayerServiceB.helloMultipleServiceA();
    }

    public String invokeBMultipleServiceB() {
        MultipleLayerServiceB multipleLayerServiceB = (MultipleLayerServiceB) multipleLayerBeanServiceIMap.get("multipleLayerServiceB");
        return multipleLayerServiceB.helloMultipleServiceB();
    }

}
