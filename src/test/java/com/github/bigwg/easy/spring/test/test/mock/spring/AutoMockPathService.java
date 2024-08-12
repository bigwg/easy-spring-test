package com.github.bigwg.easy.spring.test.test.mock.spring;

import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.AutoMockServiceA;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.AutoMockServiceB;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.one.child.AutoMockServiceE;
import com.github.bigwg.easy.spring.test.test.mock.spring.auto.mock.wildcard.two.AutoMockServiceC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * AutoMockPathService
 *
 * @author zhaozhiwei
 * @since 2024/3/8
 */
@Service
public class AutoMockPathService {

    @Autowired
    private AutoMockServiceA autoMockServiceA;
    @Autowired
    private AutoMockServiceB autoMockServiceB;
    @Autowired
    private AutoMockServiceC autoMockServiceC;
    @Autowired
    private AutoMockServiceE autoMockServiceE;

    public boolean invokeMock() {
        return Objects.isNull(autoMockServiceA.name()) && Objects.isNull(autoMockServiceB.name())
                && Objects.isNull(autoMockServiceC.name()) && Objects.isNull(autoMockServiceE.name());
    }

}
