package com.github.bigwg.easy.spring.test.test.context.multiple;

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
public class MultipleImportService {

    @Autowired
    private Map<String, MultipleBeanServiceI> multipleBeanServiceIMap;

    public String invokeMultipleServiceA() {
        MultipleBeanServiceI multipleBeanServiceA = multipleBeanServiceIMap.get("multipleServiceA");
        return "invoke MultipleServiceA: " + multipleBeanServiceA.hello();
    }

    public String invokeMultipleServiceB() {
        MultipleBeanServiceI multipleBeanServiceB = multipleBeanServiceIMap.get("multipleServiceB");
        return "invoke MultipleServiceB: " + multipleBeanServiceB.hello();
    }

}
