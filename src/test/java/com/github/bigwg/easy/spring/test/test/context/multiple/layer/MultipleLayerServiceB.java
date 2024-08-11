package com.github.bigwg.easy.spring.test.test.context.multiple.layer;

import com.github.bigwg.easy.spring.test.test.context.multiple.MultipleBeanServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * MultipleLayerServiceB
 *
 * @author zhaozhiwei
 * @since 2023/4/26
 */
@Service("multipleLayerServiceB")
public class MultipleLayerServiceB implements MultipleLayerBeanServiceI {

    @Autowired
    private Map<String, MultipleBeanServiceI> multipleBeanServiceIMap;

    @Override
    public String hello() {
        return "MultipleLayerServiceB";
    }

    public String helloMultipleServiceA(){
        MultipleBeanServiceI multipleBeanServiceA = multipleBeanServiceIMap.get("multipleServiceA");
        return "invoke MultipleLayerServiceB.helloMultipleServiceA: " + multipleBeanServiceA.hello();
    }

    public String helloMultipleServiceB(){
        MultipleBeanServiceI multipleBeanServiceB = multipleBeanServiceIMap.get("multipleServiceB");
        return "invoke MultipleLayerServiceB.helloMultipleServiceB: " + multipleBeanServiceB.hello();
    }

}
