# Easy String Test

“单元测试太不稳定了，时好时坏，准确率又很低，写单测不就是浪费时间么，还不如不写呢！”、“我费了很大精力写的单测，从 DEV 环境换到 TEST 环境就不能用了，哎，白写了”、“我的单测刚才运行还成功了呢，但是不知道谁把我的测试数据搞乱了，现在怎么执行都不成功了，真麻烦，属实不想写单测了”，“业务系统的依赖太复杂了，依赖各种各样的中间件，导致写单测的成本太高了，然后写完的单测还不一定有用，收益实在是太低了”，上面那些对单元测试抱怨的话想必每个业务开发同学多多少少都体会过或者抱怨过，那单元测试有这么多的问题，我们到底应不应该继续写呢？这些问题是不是能够找到合适的解决方案呢？

## 简介

easy-spring-test提出了一种全新的基于spring环境的最小化环境测试方案，该方案可以解决传统spring/spring boot单元测试带来的臃肿、启动慢、不稳定等问题，具有如下特点：

- 单测执行速度快：最小化启动spring容器，降低bean加载时间，只依赖核心待测试bean，极致降低单测启动时间。
- 无复杂依赖，稳定性高：不依赖如rpc、mq、缓存、数据库等中间件，提升单测执行的稳定性，避免单测执行因外部组件导致的错误。
- 环境无关，准确率高：不依赖具体的单测执行环境，不管是在公司、家里、地铁站，甚至火车上、飞机上、外太空（如果能去的话）等场所均可以准确执行单测。

## 快速开始

### 引入maven依赖

```xml
<dependencies>
    <dependency>
        <groupId>io.github.bigwg</groupId>
        <artifactId>easy-spring-test</artifactId>
        <version>1.0.4</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 添加@EasySpringTest注解到测试基类

```java
package com.github.bigwg.easy.spring.test.test;

@EasySpringTest
@RunWith(SpringRunner.class)
public abstract class BaseTest {
    
}
```

### 编写测试类实现测试基类

```java
package com.github.bigwg.easy.spring.test.test.context;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AutoImportTest extends BaseTest {

    @Autowired
    private ServiceA serviceA;
  
    @Test
    public void autoImportInvokeTest() {
        String hello = serviceA.hello();
        MatcherAssert.assertThat(hello, Matchers.equalTo("ServiceA"));
        String invokeServiceB = serviceA.invokeServiceB();
        MatcherAssert.assertThat(invokeServiceB, Matchers.equalTo("invoke ServiceB: ServiceB"));
        String invokeServiceC = serviceA.invokeServiceC();
        MatcherAssert.assertThat(invokeServiceC, Matchers.equalTo("invoke ServiceC: ServiceC"));
    }

}
```

## 未来计划

支持更多的中间件自动mock，如Dubbo、Nacos、Sentinel，RocketMQ等。

## License

Apache License Version 2.0
