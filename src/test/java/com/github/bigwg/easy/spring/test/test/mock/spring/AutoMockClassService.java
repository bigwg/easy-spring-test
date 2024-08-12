package com.github.bigwg.easy.spring.test.test.mock.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * AutoMockClassService
 *
 * @author zhaozhiwei
 * @since 2024/3/8
 */
@Service
public class AutoMockClassService {

    @Autowired
    private AutoMockClassServiceA autoMockClassServiceA;
    @Autowired
    private AutoMockClassServiceB autoMockClassServiceB;

    public boolean invokeMock() {
        return Objects.isNull(autoMockClassServiceA.name())
                && Objects.isNull(autoMockClassServiceB.name());
    }

}
