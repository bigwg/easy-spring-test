## 一、背景

最近公司研发团队对于业务系统单元测试的准确率与通过率提出了更高的要求，越来越多的工程开始了提高单元测试覆盖率与准确率的升级改造，但是效果却有些不尽人意，同时也收到了很多同学的抱怨，“单元测试太不稳定了，时好时坏，准确率又很低，写单测不就是浪费时间么，还不如不写呢！”、“我费了很大精力写的单测，从 DEV 环境换到 TEST 环境就不能用了，哎，白写了”、“我的单测刚才运行还成功了呢，但是不知道谁把我的测试数据搞乱了，现在怎么执行都不成功了，真麻烦，属实不想写单测了”，“业务系统的依赖太复杂了，依赖各种各样的中间件，导致写单测的成本太高了，然后写完的单测还不一定有用，收益实在是太低了”，上面那些对单元测试抱怨的话想必每个业务开发同学多多少少都体会过或者抱怨过，那单元测试有这么多的问题，我们到底应不应该继续写呢？这些问题是不是能够找到合适的解决方案呢？

## 二、问题和方案

### 2.1 现有单测方案

在分析问题和寻找解决方案之前我们先看看现有的单测方案有哪些

<table data-diff-id="ct-diff-id-327f15b3-9988-4b94-9e43-e8495285eb2b" data-borderwidth="1" data-table-id="7470238" data-version="1.0"><colgroup><col><col><col><col></colgroup><tbody><tr data-row-diff-id="ct-tr-diff-id-f665ad83-b56c-4f43-b9cb-1579b2536a5c"><th data-cell-diff-id="ct-cell-diff-id-4c029ee0-b0d1-41fe-898b-4c6b138a456f" data-colwidth="209"><p data-diff-id="ct-diff-id-dcc10ef8-44ad-41f9-80db-8a7b78a0aead">单测方案</p></th><th data-cell-diff-id="ct-cell-diff-id-0e692161-8ee8-42fd-a9bf-d6e5863dcfea" data-colwidth="192"><p data-diff-id="ct-diff-id-d4a4cebb-8a4f-4c91-92b0-b2b37ad817e1">测试方式</p></th><th data-cell-diff-id="ct-cell-diff-id-cb0192c8-9591-4dc8-9c55-baf59b9476f4" data-colwidth="264"><p data-diff-id="ct-diff-id-5f407473-dc20-46f8-be5d-a537488f8a74">优点</p></th><th data-cell-diff-id="ct-cell-diff-id-0cb0b6d7-7cf9-491e-b5f3-6ba0ffd70241" data-colwidth="300"><p data-diff-id="ct-diff-id-9ee684b9-ad5e-4f8e-8731-fbd3fb169139">缺点</p></th></tr><tr data-row-diff-id="ct-tr-diff-id-e29db820-6742-44a9-9c6b-43bb1457f22f"><td data-cell-diff-id="ct-cell-diff-id-0c7646b3-bdc9-42a0-b790-f221fb9a24db" data-colwidth="209"><p data-diff-id="ct-diff-id-e7fbd831-0895-45f2-ac0b-dabe0a2234e7">方案一：基于 Spring/Spring Boot 的单元测试</p></td><td data-cell-diff-id="ct-cell-diff-id-27f8ece1-2a31-4a85-b731-650c203d0be9" data-colwidth="192"><p data-diff-id="ct-diff-id-7b7a7e4c-2d84-4a77-abd4-a4bedb104bcd">使用 SpringRunner 或者@SpringBootTest 注解的方式，启动整个 Spring 容器，然后在测试类中注入待测试类，并对类中的方法进行测试</p></td><td data-cell-diff-id="ct-cell-diff-id-5dcbf5d5-f2a5-46bf-8f91-1083af8ff6f2" data-colwidth="264"><p data-diff-id="ct-diff-id-785b6065-b374-4550-9152-998760b83da2">①测试代码编写简单</p><p data-diff-id="ct-diff-id-e479cf09-3090-4262-be21-09df36983eb6">②不需要关心 Spring Bean 代码依赖关系</p></td><td data-cell-diff-id="ct-cell-diff-id-c07a8e2d-0908-4029-aa16-e317dc63c47c" data-colwidth="300"><p data-diff-id="ct-diff-id-3e6387df-f7e3-4483-bb2e-26ec8187eeae">①单测执行速度非常慢</p><p data-diff-id="ct-diff-id-5f3c6245-0055-427a-abda-5ae3e0d89488">②依赖复杂，加载全部组件，稳定性差</p><p data-diff-id="ct-diff-id-48e4ffb9-48be-44ef-8144-2823c6118c76">③环境相关性高，准确率低</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-7c14ecdb-f818-48ed-a3be-83c6fefeadb0"><td data-cell-diff-id="ct-cell-diff-id-1f77528e-09ac-48b3-ac4e-c9f48fa02c64" data-colwidth="209"><p data-diff-id="ct-diff-id-9d69ebac-1830-4dfd-96f6-4994e90f44a9">方案二：脱离 Spring 及其他中间件的单元测试</p></td><td data-cell-diff-id="ct-cell-diff-id-6bca9e20-68a0-4c65-94f7-850297122b1b" data-colwidth="192"><p data-diff-id="ct-diff-id-0eda000f-064d-4e70-881b-c5b3cc04cc87">不启动 Spring 容器，在单测的 Before 方法中直接 new 一个待测试的类的对象实例，然后分析待测试类的依赖关系把所有依赖的本地类都 new 一遍并手动注入</p></td><td data-cell-diff-id="ct-cell-diff-id-d8fb097b-7deb-4a19-9c2e-dbd370a560f3" data-colwidth="264"><p data-diff-id="ct-diff-id-0274e476-ed63-46fa-8202-f6d7ee6bff43">①单测执行速度快</p><p data-diff-id="ct-diff-id-42ce5285-b8ae-49be-8a96-a6d37698d327">②无复杂依赖，稳定性好</p><p data-diff-id="ct-diff-id-9d040afe-c3ed-4f68-a4e5-d2e2764f88e1">③环境无关，准确率高</p></td><td data-cell-diff-id="ct-cell-diff-id-42e17b7a-8cbf-4f24-95e8-4003e8f24a4a" data-colwidth="300"><p data-diff-id="ct-diff-id-09c32f30-2064-4822-9325-c17fd2d97b40">①没有成熟的落地方案</p><p data-diff-id="ct-diff-id-911ce3c8-ee8c-4c5f-9e01-e1a41e3fd2ce">②无法进行SQL测试</p><p data-diff-id="ct-diff-id-902e3101-4fac-4c1f-9bd4-72e3f6e998c0">③测试代码编写非常复杂，需要人工梳理并注入依赖</p></td></tr></tbody></table>

在介绍这两个单测方案前，先来了解两个概念“集成测试”与“单元测试”，引用下维基百科上对这两个概念的解释，**集成测试：**又称整合测试、组装测试，即对程序模块采用一次性或增值方式组装起来，对系统的接口进行正确性检验的测试工作。整合测试一般在单元测试之后、系统测试之前进行。实践表明，有时模块虽然可以单独工作，但是并不能保证组装起来也可以同时工作。**单元测试：**在计算机编程中，单元测试（英语：Unit Testing）又称为模块测试 ，是针对程序模块（软件设计的最小单位）来进行正确性检验的测试工作。程序单元是应用的最小可测试部件。在过程化编程中，一个单元就是单个程序、函数、过程等；对于面向对象编程，最小单元就是方法，包括基类（超类）、抽象类、或者派生类（子类）中的方法。

在了解了“集成测试”与“单元测试”的概念之后，再来看一下上面的两种单测方案，首先是基于 Spring/Spring Boot 的单元测试方案，该方案是现在主流的单测方案，同时我们团队内部也是采用的这种方案，这个方案的特点是启动一个完整的、通过自动扫包机制自动加载所有 Spring Bean 的上下文，优点是不需要手动管理依赖关系，编写测试代码也比较高效，但是缺点也很明显，加载了整个正式运行的 Spring 上下文，导致了与测试单元无关的中间件、内外部依赖等全部都加载并初始化了，不仅执行速度非常慢，而且稳定性还很差，这其实导致了一个严重的问题，它违背了单元测试的独立性、可重复性原则，所以这个方案相较于单元测试，更偏向于对所有组件都进行测试的集成测试的方式。然后是脱离 Spring 及其他中间件的单元测试方案，该方案目前因为没有成熟的落地方案且无法进行 SQL 测试导致覆盖度提升困难且测试代码编写困难等原因并没有被广泛的采用，但是这个方案才是真的符合单元测试原则的。

综上所述，目前两个方案都存在一定的问题，尤其是方案二，虽然理论上头头是道，但是想要真正的应用起来却是阻碍重重，尤其是网上有大量的文章建议使用这种方案，但是却鲜有文章介绍如何落地。现在大家可以考虑下是不是可以有一个方案三，同时吸取前两个方案的优点，又能够解决它们的缺点呢？

### 2.2 问题与根因分析

在上文了解了集成测试与单元测试的区别与核心思想之后，再来看看目前这两种单元测试方案所面临的问题和解决思路：

<table data-diff-id="ct-diff-id-b0c14bbf-262b-4ff5-86e8-ad6aac1df4d8" data-borderwidth="1" data-table-id="4983637" data-version="1.0"><colgroup><col><col><col><col></colgroup><tbody><tr data-row-diff-id="ct-tr-diff-id-4124b851-0d04-4b29-b651-0e0857352b25"><th data-cell-diff-id="ct-cell-diff-id-4c59696c-d2ca-499e-a9e3-cc25b8954284" data-colwidth="145"><p data-diff-id="ct-diff-id-847e7789-6516-40bd-a94a-0e5dc919fa5a">单测方案</p></th><th data-cell-diff-id="ct-cell-diff-id-fac32a27-3b00-4809-b3fe-620d68a4a3f5" data-colwidth="260"><p data-diff-id="ct-diff-id-fd52076b-806e-431c-b9ac-dfbcbfeb6feb">问题</p></th><th data-cell-diff-id="ct-cell-diff-id-aa46a7d5-6de9-467c-a6d7-d3b7d555fbd3" data-colwidth="169"><p data-diff-id="ct-diff-id-e81a74a1-c593-4f64-9fb9-7a4f9c872782">问题分类</p></th><th data-cell-diff-id="ct-cell-diff-id-2bc3d342-a641-4bba-8b3d-07229dee7f83" data-colwidth="390"><p data-diff-id="ct-diff-id-b76851d2-3568-4960-a0f1-cbcaaae0f7c5">难点&amp;解决思路</p></th></tr><tr data-row-diff-id="ct-tr-diff-id-8f11a103-262a-4ef7-95eb-b55b70531b57"><td data-cell-diff-id="ct-cell-diff-id-8d9110c8-f07b-4d50-b4f2-c6e1d2f42ff1" data-colwidth="145" rowspan="3"><p data-diff-id="ct-diff-id-a114a04d-07f6-45a9-91d7-bda0e4f9bc77"><strong>基于 Spring/Spring Boot 的单元测试</strong></p></td><td data-cell-diff-id="ct-cell-diff-id-8626e36e-4d3c-4e64-90ac-e7a8fe6e142e" data-colwidth="260"><p data-diff-id="ct-diff-id-d268248a-6041-40b5-8961-c2f0da12cbc7">只想运行一个方法的单元测试，理论上应该非常快就结束了（几秒内），但是现在一次执行却需要60至100秒的时间，甚至有的工程超过了150秒</p></td><td data-cell-diff-id="ct-cell-diff-id-2aac1a98-f35d-49c2-b8c7-bbe1a55ac007" data-colwidth="169"><p data-diff-id="ct-diff-id-371f6c92-6dd9-4330-8dba-036bab4deb61">非最小化测试环境</p></td><td data-cell-diff-id="ct-cell-diff-id-9a5d4dc8-ad55-4bc8-a459-12ef8fd57797" data-colwidth="390"><p data-diff-id="ct-diff-id-3b7e09fd-acc1-4d27-8765-5cfaf9308863"><span data-size="16">【</span><strong><span data-size="16">难点</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-c89580f7-59d0-4a69-9ce2-b43c454551a0">如何通过最少的单测配置改动或完全不改动的情况下，启动一个独立的最小化的 Spring 容器</p><p data-diff-id="ct-diff-id-8dadafeb-d357-4c4d-96de-4086d60d0124"><span data-size="16">【</span><strong><span data-size="16">解决思路</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-594bacde-6103-40bd-b07b-8fd028d4a4c1">启动一个关闭了 Spring/Spring Boot 的自动扫包、自动配置等功能的上下文，然后手动引入与待测试部分相关的内容</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-80c5fad6-adc4-428b-9d57-88e35b7a305c"><td data-cell-diff-id="ct-cell-diff-id-4838cc88-de3e-4ddc-a3a6-a3b42a39d54d" data-colwidth="260"><p data-diff-id="ct-diff-id-69d91e79-a3e2-492f-bcdd-379a346eeb6e">①单测在 DEV 环境是好使的，但是把同样的单测在 TEST 或其他环境中执行的时候却报错了</p><p data-diff-id="ct-diff-id-0cefa53e-1bb8-4e9f-bca6-48bb6175911d">②在办公网络下运行单测是好好的，但是回到家后在非办公网络情况下运行单测却怎么也不成功</p><p data-diff-id="ct-diff-id-0b14560f-0106-42d1-b67f-713dfaccb9e7">③在本地跑的单元测试都是通过的，但是用集成工具或发版工具发布编译的时候单元测试结果却都是失败的</p></td><td data-cell-diff-id="ct-cell-diff-id-eda2e30b-6ce0-4383-bb5e-1f10e263998b" data-colwidth="169"><p data-diff-id="ct-diff-id-f6c9d23b-9f85-412e-81b0-f55233d2c9d2">环境相关性高</p></td><td data-cell-diff-id="ct-cell-diff-id-1e28bfbf-e4f5-419b-9739-2560ad542593" data-colwidth="390" rowspan="2"><p data-diff-id="ct-diff-id-4a2c389b-715f-4f1b-a3f6-2104a55d5f46"><span data-size="16">【</span><strong><span data-size="16">难点</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-abe16d39-aa91-40aa-bc40-5cec17678b73">①如何使 Spring 在启动的时候完全不去连接外部的任何组件，关闭 Spring 的自动扫包机制，完全与当前运行单测的环境隔离开</p><p data-diff-id="ct-diff-id-acf218fb-9fcf-4848-b5f4-a086ac02007f">②如何通过简单的易理解的方式梳理待测试单元的所有依赖，并在当前的 Spring 上下文中引入与单测相关的内容</p><p data-diff-id="ct-diff-id-bcde0265-8fa7-4bd0-acb7-b52a76bbc874"><span data-size="16">【</span><strong><span data-size="16">解决思路</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-9226e148-5908-4107-8d38-198838ebe7f1">通过指定的配置文件启动一个只包含内存数据库的 Spring 上下文，然后梳理待测试单元相关的所有依赖并引入当前 Spring 上下文中，最终构造一个完全不包含任何外部依赖只包含待测试单元相关的本地依赖的环境</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-c9781474-f5a4-41db-acef-fa2e1fa1a0c5"><td data-cell-diff-id="ct-cell-diff-id-4be47389-e9ee-4a9e-b0e2-ba4ddd45a2be" data-colwidth="260"><p data-diff-id="ct-diff-id-fd176170-2916-414d-8f15-18c8a712210c">只想运行一个简单的测试，不依赖任何外部环境的测试，刚才还好好的，啥也没动，突然就不好用了</p></td><td data-cell-diff-id="ct-cell-diff-id-5c94d693-54ae-47ea-9e61-2a5b13cfbac9" data-colwidth="169"><p data-diff-id="ct-diff-id-ee1a6f43-1e15-4fc1-af13-4d03754ca80b">依赖复杂度高</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-52b58485-b4bb-4c74-83a0-3880eec9bda4"><td data-cell-diff-id="ct-cell-diff-id-22469a45-50a2-4796-aa25-27280faa0c3a" data-colwidth="145" rowspan="2"><p data-diff-id="ct-diff-id-9e70c081-6a4e-4b7f-8585-613d77508549"><strong>脱离 Spring 与其他中间件的单元测试</strong></p></td><td data-cell-diff-id="ct-cell-diff-id-e5a5dceb-b71d-4e55-895a-14d048ed649d" data-colwidth="260"><p data-diff-id="ct-diff-id-cb7e5689-e63d-47f1-b1ad-9c1350da7671">编写单测难度太高了，需要手动梳理依赖树并手动 new 依赖类的实例然后进行注入，编写效率太低。</p></td><td data-cell-diff-id="ct-cell-diff-id-e4c091c5-d4d5-4755-8163-845ecfb3e3d6" data-colwidth="169"><p data-diff-id="ct-diff-id-b5dd93d6-2e62-4558-a976-877e810b7403">单测编写难度高</p></td><td data-cell-diff-id="ct-cell-diff-id-1855f164-0dca-469d-b17a-40f9a170c7b4" data-colwidth="390" rowspan="2"><p data-diff-id="ct-diff-id-47105304-63ad-47f8-8672-911263a94cd4"><span data-size="16">【</span><strong><span data-size="16">难点</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-21c90440-a08e-43c5-a81b-c28b86fddc66">①脱离了 Spring 环境，在正式代码中的所有 Spring 依赖都需要重新梳理然后手动引入，成本非常高</p><p data-diff-id="ct-diff-id-91a4e7ab-3805-4d2f-a9eb-6ac1717f7e87">②现代业务工程和大多数中间件都非常依赖于 Spring 框架，如果脱离 Spring 框架进行单测会导致大部分内容都无法进行测试覆盖，在下一层测试中才能暴露出来</p><p data-diff-id="ct-diff-id-34c45920-59b0-48cb-bcb1-f368e715258c"><span data-size="16">【</span><strong><span data-size="16">解决思路</span></strong><span data-size="16">】</span></p><p data-diff-id="ct-diff-id-fb08ecfc-04fe-45ee-9812-bb3119152c44">启动一个包含 Spring 核心能力的上下文，该上下文可以自动管理并自动注入待测试单元相关的 Bean，同时屏蔽了所有与测试无关的内容，包括外部中间件依赖和内部依赖等</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-445a1cbc-e529-462a-8270-8130e6d5716a"><td data-cell-diff-id="ct-cell-diff-id-f7c90361-cd49-496d-b567-4a211bd9e70a" data-colwidth="260"><p data-diff-id="ct-diff-id-5c5a10b4-7a98-4ce1-8920-c2753317e1f3">①没有办法进行 SQL 测试，现在的 ORM 框架的使用都是依赖于 Spring 的，脱离 Spring 环境后基本没办法测试。</p><p data-diff-id="ct-diff-id-bddc4354-33eb-42b1-a6bd-ee95d3c61368">②单测时的依赖关系是好的，但是运行时却报错了，提示依赖不存在或依赖对象为空。</p></td><td data-cell-diff-id="ct-cell-diff-id-31505637-6f32-402b-a7b2-873c1f5d2afa" data-colwidth="169"><p data-diff-id="ct-diff-id-b661a92a-b81d-44d7-8ecd-d254815d57f3">测试覆盖度低</p></td></tr></tbody></table>

想必大家学习过 Spring 框架后都可以倒背如流的说出 Spring 框架两大特性之一的 **IOC（控制反转）**，控制反转的核心是什么呢？是将工程中的所有依赖都管理在 Spring 中，然后需要的时候向 Spring 容器索取，**而随着 Spring 框架的不断迭代更新，使用方式也变成了对用户来说越来越便利的注解加自动扫包的机制，而且 Spring Boot 的项目因为 Spring Boot 的 AutoConfig 机制更加剧了这种情况的产生。**这就导致了通常在单元测试启动时默认将一个完整的拥有非常庞大依赖关系的 Spring 容器给启动了，最终使得单元测试又慢又不稳定。

**由于大多数业务工程代码都是强依赖 Spring 框架的，代码如果想脱离 Spring 环境进行单元测试需要非常高的改造成本，然后是业务工程连接的组件非常之多，如注册中心、配置中心、Redis、MQ等，在工程启动的时候都要与这些组件建立连接，而本地环境的网络情况和这些外部服务的不稳定还可能会导致连接失败然后进行重试。**这就导致了启动单测也不得不启动整个工程，然后还要忍受网络和外部服务的不稳定带来的问题，这么一看是不是单测能够启动起来并且启动时间约等于工程启动时间就很不错了？

而且工程会连接很多外部服务，通常外部服务大概率也都是区分环境的，比如 DEV 环境配置中心配置的某个配置值为 2，但是在 TEST 环境这个值配置为 5 甚至不存在，这无疑是给单测带来了极大的不确定性，同时不同环境中的配置还可能随时被人修改，在这样的情况下想让单测不受影响岂不是及其困难？

### 2.3 探寻解决方案

现在知道了基于 Spring 环境单元测试低效、不稳定的根源其实是由于 Spring 环境对工程中所有的依赖都进行了自动管理导致的，所以首先要解决的问题就是最小化启动 Spring 容器，这个容器在默认情况下是不会加载任何业务组件的，同时也不能进行自动扫包和自动配置，然后在编写单测的时候根据实际情况按需引入。

对于要执行的单元测试来说可以**将整个 Spring 上下文中的内容分为三大部分：待测试部分、不相关部分、依赖但非测试部分，对于这三部分的处理是不同的，首先是加载待测试部分、不加载不相关部分，然后是 Mock 依赖但非测试部分，这样就得到了一个符合环境无关的、测试单元最小化的单元测试，同时它肯定也会表现的稳定与高效。**

那为什么要这样划分和处理呢？

**首先看待测试部分，这部分通常包括待测试的代码以及编写好的 SQL 等，特点是新编写的准确性待验证的内容，**需要用单元测试来做正确性验证，所以是必须加载的部分。

**然后是不相关部分，这部分通常包括注册中心、降级限流组件、MQ 组件及其他与单测无关的内部或外部功能，特点是与要执行的单元测试毫不相干，不加载完全不影响单元测试的执行，**但是加载过程中可能会出现各种异常问题导致 Spring 容器启动失败，最终导致单元测试失败，所以这部分是坚决不要进行加载的。

**最后是依赖但非测试部分，这部分通常包括待测试代码中的 RPC 依赖、配置中心依赖、Redis 依赖、及其他组件 API 依赖，特点是属于待测试代码中的依赖部分，但并不是进行单元测试的目标，**而且这些依赖的不同返回值还会影响单元测试的结果，所以对于这部分最好是要进行 Mock 操作的。

## 三、收益

那么，在解决了上面的问题后可以得到什么样的收益呢？

（1）**大幅减少单测执行等待时间，极大的提升研发效率，**通常大家在编写代码和单测的时候都是依次编写并执行的，每次执行都将减少60至100秒的等待时间。

（2）**单测编写难度降低，覆盖率提升，**编写单测时可以依赖 Spring 的部分自动注入能力，降低梳理与重复编写成本，提升单测编写效率。

（3）**可以彻底从繁重的无效的单测失败原因排查中解脱出来，**无论单测运行在什么环境下都是成功的，失败的原因只可能是代码逻辑故障。

（4）**会规避很多与测试单元无关的未知因素的干扰，提升单测稳定性与准确率，**一个测试单元有问题一定是这个单元相关的依赖或者自身的问题。

如上，可以发现解决了这些问题后对研发效率、代码纠错等方面的帮助与收益还是非常明显的，无论是从短期还是长期收益来看这些问题都是非常值得去解决的。

## 四、最佳实践

在上文寻找到了一个解决方案，**启动一个最小化的 Spring 上下文，然后加载待测试部分、不加载不相关部分，最后 Mock 依赖但非测试部分。**基于这个方案，可以确定单元测试整体流程如下。

**第一步：**将工程中的所有功能（依赖）按照与单元测试的关系进行三部分的划分

**第二步：**最小化启动 Spring 单元测试环境，本步骤主要是加载部分待测试依赖（内嵌数据库H2）以及忽略不相关部分依赖的加载

**第三步：**主动引入待测试部分依赖到本次执行单元测试的 Spring 上下文中

**第四步：**将待执行单元测试的依赖但是不需要进行测试的部分进行合理 Mock

在实践部分将流程分为上述四步，下面对上述流程进行拆解，并进行实际的落地。

### 4.1 启动环境无关、最小化的 Spring 单测环境

目前的大多数工程其实都是通过 SQL 与 Mysql 等数据库进行通信的上层应用，由于编写了很多 SQL，而且这些 SQL 也是与我们的业务代码正确与否息息相关的，完全可以当成业务代码的一部分，所以可以肯定的说单测需要对这些 SQL 进行覆盖验证，但是连接一个外部的服务又违背了单测的原则，针对这种情况我们可以引入一个内嵌式的内存数据库，比如我们熟知的 H2。不过其他的**大多数中间件其实都是简单的通过 API 的形式进行交互，这部分中间件在单测阶段其实是完全没必要的外部依赖，而且对于这部分 API 的正确性也不是单元测试需要关心的，是属于集成测试的范畴，所以单测中完全可以 Mock 掉它们或者按需引入（按需引入的前提条件是不依赖任何环境即可启动，例如用内嵌数据库 H2 来代替 Mysql，如果想测试 Redis 也可以用类似的思路，引入一个内嵌的 Redis，但是绝不能引入任何的外部 Redis，但是使用 Redis 通常都是直接使用它的 API，所以直接采用 Mock 的方式更便利），最终确定了 Spring 容器启动应该包含的内容：只有数据库相关的部分与待测试依赖部分，**下面看一看具体的代码。

首先，需要引入 spring-test 和 spring-boot-test 这两个依赖包

然后就是创建单元测试基类，基于 Testng、Junit4 和 Junit5 的实现方案有些许差别，代码如下：

上面 @ContextConfiguration 注解引入的 dataSource.xml 配置文件如下（**如果大家的项目是多数据源的话只需要把所有的表都放在这一个数据源下即可，分库分表亦是如此**）：

dataSource.xml 配置文件需要放在 test/resources 目录下，在写单测的时候只需要继承上面经过改造后的 BaseTest 类即可。

最后看一下配置好的 test 目录内容如下，其中 **datasets、h2/init.sql** 是 Database-rider 组件使用的文件，在下面会介绍。

![](%E5%9F%BA%E4%BA%8ESpring%E7%8E%AF%E5%A2%83%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95%E7%9A%84%E6%8E%A2%E7%B4%A2%E4%B8%8E%E5%AE%9E%E8%B7%B5/39208679059.png)

### 4.2 使用 H2 和 Database-rider 进行 SQL 测试

上文已经提到最小化的单元测试是需要包含数据库的，但是如果引入一个外部的数据库的话，那就违背了单元测试环境无关的原则。所以，要用一个内嵌的数据库来替代外部的数据库，而 H2 就是需要的那个嵌入式数据库。Database-rider 又是什么呢？它其实是一个方便进行 SQL 测试的工具，它提供了数据初始化、数据清理、结果对比等功能。所以 H2 和 Database-rider 的搭配使用，能够使 SQL 测试更加高效便捷的进行。

首先，引入 H2 和 Database-rider 的依赖

然后引入配置

下面举一个简单的例子来看一下如何使用

### 4.3 使用 Spring 依赖管理简化单测

由于使用最小化的 Spring 环境来启动的单元测试，所以在 Spring 容器中只有与 DAO 相关的 Bean，而其他的所有 Bean 都需要手动引入，这样就得到了一个纯净的、最小化的、按需引入的 Spring 上下文。

**先来看一种简单的情况，如果要测试的服务是 ServiceA，该服务依赖关系如下：**

ServiceA 依赖 ServiceB 和 ADao

ServiceB 依赖 BDao

代码片段如下：

要对 ServiceA 进行测试，由于 ServiceA 的所有依赖和间接依赖都是本地依赖，没有任何外部依赖，所以可以将其依赖的所有服务都加载到 Spring 上下文中，测试类代码如下：

**再来看一个稍微复杂一点的情况，如果要测试的服务还是 ServiceA，该服务依赖关系如下：**

ServiceA 依赖 ServiceB 和 ADao

ServiceB 依赖 ServiceC 和 BDao

ServiceC 依赖 ServiceD

ServiceD 没有依赖

可以看到这个例子要比上面的例子的依赖层级要深一些，这里是4层依赖关系（从 ServiceA 为第一层算起），这里虽然比上面的例子要复杂一些，但是解决方案还是一样的，只需要使用 @Import 注解将所有本地依赖全部引入到当前的 Spring 上下文中即可。代码如下：

在上面这个例子中依赖关系就比较复杂了，需要人工梳理的层级也比较深了，不过通常的依赖层级都是在3到4级左右，依赖的 Bean 也是在10个以内，如果依赖层级、依赖 Bean 太多的话那就要思考一下服务拆分的是否合理，是否满足单一职责原则了。

### 4.4 使用 Mock 来屏蔽外部依赖

首先，需要引入一个 Spring 官方的 Mock 功能的依赖和 Mockito 的依赖：

然后，需要在 BaseTest 上加一个注解 **@TestExecutionListeners(..., MockitoTestExecutionListener.class)** 即可，在上文中也提到过。

Mock 主要分为两类，下面通过几个例子和大家介绍一下。

**第一类：使用 @MockBean 注解进行 Mock，该注解会创建一个所有方法都返回 NULL 的对象来 Mock 原来的对象，适合用来 Mock 外部依赖，然后配合方法打桩来改变原有方法的行为。**

举个例子，要测试的服务是 ServiceA 依赖了 ThriftServiceB 的 methodB 方法，ThriftServiceB 是一个外部的 RPC 接口，此时需要 Mock 这个接口来保证单测是环境隔离的，代码如下

**第二类：使用 @SpyBean 注解进行 Mock，该注解会创建一个所有方法都调用原方法的 Mock 对象，适合用来 Mock 内部依赖，然后配合方法打桩来改变原有方法的行为。**

举个例子，比如想要测试 ServiceA 的 methodA 的事务是否生效，就可以使用该注解来实现，因为想要某一个方法抛出异常，而其他的所有方法都保持原样正常运行，所以可以用 @SpyBean 注解来解决这个问题

被测试类 ServiceA 依赖 DaoA 与 DaoB，DaoA 的 insertA 方法与 DaoB 的 insertB 方法在 ServiceA 的 methodA 方法中，并在一个事务下，代码如下

在上面的两个例子中使用了两种打桩方式，第一种称之为 **when.then** 的方式，第二种称之为 **do.when** 的方式，这两种方式的核心区别在于两点

① **do.when** 不是类型安全的（**不会进行编译期返回类型校验，比如使用 doReturn 的时候**），这可能带来意想不到的失败

② **when.then** 的方式打桩时会调用一次原方法（**在使用 @SpyBean 注解 mock 的对象在打桩时会先执行一次并造成影响，使用 @MockBean 的方式由于没有真实的原方法所以不会造成影响**），**do.when** 的方式打桩的话不会调用原方法

举个例子：上面使用 @SpyBean 来 mock 的 daoB，如果使用 **when.then** 的方式打桩的话，在执行打桩代码时 daoB.insert() 方法还是会被真实的调用一次，所以可能会产生意想不到的错误，大家可以根据实际情况来选择 Mock 的方式。

### 4.5 常用中间件在单测中的处理方案

上文介绍了如何最小化启动 Spring 环境，引入 H2 对 SQL 进行测试，以及如何使用 Mock 框架来对单测进行优化，有的同学就有疑问了，那代码中依赖的其他组件怎么办呢？其实在上文在探索解决方案部分就进行了简单的介绍了，其他部分需要根据实际情况进行 Mock 或者忽略，**核心思路为需要进行测试的部分引入内嵌版本替代（不能依赖外部组件）、不需要测试但是单测代码依赖的组件进行 Mock，不相关部分忽略。**下表中我对常见的组件的处理方案进行了总结。

<table data-diff-id="ct-diff-id-48f739b0-a34b-4faa-b66d-5269474f03e8" data-bordercolor="#cccccc" data-borderwidth="1" data-table-id="5014082" data-version="1.0"><colgroup><col><col><col></colgroup><tbody><tr data-row-diff-id="ct-tr-diff-id-9ca86e75-acf3-46e6-be16-fcc4fa4bde58"><th data-cell-diff-id="ct-cell-diff-id-19a4f675-27de-47e4-b43e-723ea23f1012" data-colwidth="155"><p data-diff-id="ct-diff-id-8008e958-7220-4f29-8739-6ab9cf0a07e7">组件</p></th><th data-cell-diff-id="ct-cell-diff-id-e7b994ec-1b3d-4baf-9b25-605057dc4bdb" data-colwidth="188"><p data-diff-id="ct-diff-id-ee96f575-6be1-4745-8d33-bad9a2d957fd">方案</p></th><th data-cell-diff-id="ct-cell-diff-id-b7ae902a-e67b-40b6-80ab-c8000177e72d" data-colwidth="694"><p data-diff-id="ct-diff-id-d3892e58-0a01-45bd-98d6-4e834c1de86d">说明</p></th></tr><tr data-row-diff-id="ct-tr-diff-id-0b670ff1-bcf3-47e6-93d6-21ad694e4a41"><td data-cell-diff-id="ct-cell-diff-id-c0dda927-8b9d-457e-9acf-7ac75663d30d" data-colwidth="155"><p data-diff-id="ct-diff-id-1d958e87-d0a1-475b-86e6-013dc82abe8a">注册中心</p></td><td data-cell-diff-id="ct-cell-diff-id-49a71484-9421-4b7e-a79b-47707ff2495f" data-colwidth="188"><p data-diff-id="ct-diff-id-501d1a5b-3e98-4c71-9dca-0095c34fe64f">不加载</p></td><td data-cell-diff-id="ct-cell-diff-id-85a59052-52bc-4984-b58b-bff1b2e857fe" data-colwidth="694"><p data-diff-id="ct-diff-id-8ee27ca5-e9de-455a-9f0f-9d94e19ebe13">单测不会真正进行 RPC 调用，所以注册中心对于单测也就没有什么意义了。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-9b35b901-b846-44c2-b305-474719bb9423"><td data-cell-diff-id="ct-cell-diff-id-3466217c-4caa-4b70-ac9d-8aa6048d9fe5" data-colwidth="155"><p data-diff-id="ct-diff-id-de1031cc-f1a0-42df-b060-84e352a05d21">配置中心</p></td><td data-cell-diff-id="ct-cell-diff-id-f5cef2c3-f238-4f56-9ab7-4ca4bb140b98" data-colwidth="188"><p data-diff-id="ct-diff-id-5dd4322b-f874-4677-8b6d-54f5a658a3c6">Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-d119b6aa-5d55-47ab-84ed-951be99ecbad" data-colwidth="694"><p data-diff-id="ct-diff-id-4b4f3b06-a658-4e80-a9f0-4b980a9c24f5">由于配置中心中的值可能因为环境的不同而不同，还可能随时被人修改，通常也是通过 API 的方式来调用，所以推荐使用 Mock 进行替代。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-0027b1a2-c035-43df-9ed6-ee50047ad3d2"><td data-cell-diff-id="ct-cell-diff-id-8c7db8a6-80da-4b27-88fc-3cb71b83362d" data-colwidth="155"><p data-diff-id="ct-diff-id-5d8d8612-1039-4eea-914b-7bc203170501">Redis</p></td><td data-cell-diff-id="ct-cell-diff-id-d4fa5685-bde2-48b0-9b84-fd128a4b8678" data-colwidth="188"><p data-diff-id="ct-diff-id-0bf8eeca-1780-4e19-ac8d-7b50e45b6500">Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-34c73243-9c85-492b-a9e4-bbd45eed69ec" data-colwidth="694"><p data-diff-id="ct-diff-id-5f21b29d-ba65-4842-b6ef-da5eae7a5713">Redis 通常的使用方案为 Java Client API 的方式，由于 API 的正确性是不需要单测代码来验证的，所以完全可以使用 Mock 来替代。</p><p data-diff-id="ct-diff-id-c8c102ed-450b-4b4e-ad0e-07ca4adc61b2">当然也存在部分 Lua 脚本的使用形式，这种情况可以使用内嵌 Redis 来解决，类似于 H2 的形式，但是由于当前没有成熟的内嵌 Redis 可用，所以不太推荐使用这种方案。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-e0dc9ed7-396f-491f-866e-28c586efb9c6"><td data-cell-diff-id="ct-cell-diff-id-aa658a83-74d8-4703-a4f7-500fc3d2abe0" data-colwidth="155"><p data-diff-id="ct-diff-id-76b5903a-6a77-42ad-95e7-3bfe0827f7e9">MQ</p></td><td data-cell-diff-id="ct-cell-diff-id-c75827d7-42df-413d-bfb3-cfaa0f2e50f8" data-colwidth="188"><p data-diff-id="ct-diff-id-3065ce70-ed8b-4dfb-bd90-80c94d6ab105">不加载/Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-4054b9b3-711e-4312-b1b8-d38b68e0599a" data-colwidth="694"><p data-diff-id="ct-diff-id-5baa74c7-c6fe-454f-acbb-45953b60d710">MQ消息是否能接收到，或者消息是否能发出去，这都数据集成测试的范畴，单元测试只关注生产者和消费者代码的逻辑正确性。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-5166fe08-7502-45bc-bc8b-ef3c9dfa4a90"><td data-cell-diff-id="ct-cell-diff-id-060e5fab-550d-422f-938c-d36e25c14e77" data-colwidth="155"><p data-diff-id="ct-diff-id-e24d1473-a977-451f-9d59-5268ec8ace45">定时任务</p></td><td data-cell-diff-id="ct-cell-diff-id-97601129-ed9d-4978-960d-5819008f1f74" data-colwidth="188"><p data-diff-id="ct-diff-id-6e0447c8-2eb8-4f0d-bf7c-f56f4ccbd7ba">不加载</p></td><td data-cell-diff-id="ct-cell-diff-id-888bf1f0-4fe0-49a6-bb42-58a1fd5df4f1" data-colwidth="694"><p data-diff-id="ct-diff-id-ea157f8b-629d-493f-bfb1-b6143cde3444">定时任务是否能够触发执行，同样是属于集成测试的范畴，单元测试只关注被触发的代码的正确性。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-c01cd39b-1cc4-4bcd-975a-e15bbc058ae8"><td data-cell-diff-id="ct-cell-diff-id-de27a594-08ba-470f-a760-7ad8dc142aa3" data-colwidth="155"><p data-diff-id="ct-diff-id-7e679216-25bb-40a1-8d08-00942701f1fa">Zookeeper</p></td><td data-cell-diff-id="ct-cell-diff-id-f034d3e4-4f37-40a1-8ed3-75c4b788569c" data-colwidth="188"><p data-diff-id="ct-diff-id-3b5a2867-3586-406e-9577-3e9dafff6060">Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-9ee40ecf-8283-4047-84c9-d798d9122ca2" data-colwidth="694"><p data-diff-id="ct-diff-id-6b272f4c-16c6-4490-8d6a-70e222279cd1">与 Redis 类似，完全可以使用 Mock 替代。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-a4ccf7cc-99cf-41ae-965d-43ac09ae51c8"><td data-cell-diff-id="ct-cell-diff-id-a54880be-701e-4c6a-8058-fd68d33158ea" data-colwidth="155"><p data-diff-id="ct-diff-id-fafa64f1-101b-4c4e-abde-bec90adfded4">分布式锁</p></td><td data-cell-diff-id="ct-cell-diff-id-db6bceec-0f07-4b9c-a97c-67fd2390094d" data-colwidth="188"><p data-diff-id="ct-diff-id-315f6566-0819-445e-8e68-f605aebe7a54">Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-522cbfd4-7c0f-4806-9e35-939f4eabfbe8" data-colwidth="694"><p data-diff-id="ct-diff-id-ac31f682-b3ba-4a1b-b608-3c6aeceafe5c">分布式锁是否可用，同样属于集成测试范畴，可以使用 Mock 进行模拟。</p></td></tr><tr data-row-diff-id="ct-tr-diff-id-57ab0b71-874c-4dbd-ada5-79111acb1283"><td data-cell-diff-id="ct-cell-diff-id-a8055258-529a-4de3-8ac7-5ea49a72f2b2" data-colwidth="155"><p data-diff-id="ct-diff-id-a40075a1-12f4-4f51-8502-c98f386f60c1">限流降级</p></td><td data-cell-diff-id="ct-cell-diff-id-bd0edc43-211b-4bd5-a4fa-750c5a058134" data-colwidth="188"><p data-diff-id="ct-diff-id-c03587c7-710f-49e9-bfc5-6750741572e5">不加载/Mock</p></td><td data-cell-diff-id="ct-cell-diff-id-cdf10358-3566-48ea-bafa-937f494cd1ce" data-colwidth="694"><p data-diff-id="ct-diff-id-7666b807-43c0-4c3c-a882-4d8c62efffe4">属于集成测试范畴，可以不进行加载，或者使用 Mock 进行模拟。</p></td></tr></tbody></table>

还有很多常用工具或者组件，就不在这里进行一一罗列了，大家可以根据实际情况进行不加载或者 Mock 模拟的抉择。

### 4.6 历史工程单测迁移至新方案

如果是一个全新的工程的话那肯定不用多说了，强烈推荐使用这种方案进行单元测试，一定可以刷新对 Java Spring 环境单元测试的认识，同时大幅提升开发效率、稳定性与准确性。

如果是历史项目呢？那其实是有一定的改造成本的，因为需要为每个单元测试划清边界，要找到他的依赖树然后将待测试依赖部分使用 @Import 引入，依赖但非测试部分使用 @MockBean 进行 Mock，所以我推荐大家慢慢的迭代迁移。比如做某个项目的时候要修改或者新增某个 Service 的单测，这时候就可以使用上文讲到的方案对该测试对象进行改造，可以增加一个新的和上面 BaseTest 代码一样的单测基类，然后逐步改造，这样成本最低，做起来也更容易完成。

## 五、优化提效

上文中的单元测试方案在本团队内部推广后大家纷纷表示在稳定性和测试效率上得到了大幅的提升，但是仍然存在编写效率较低的问题，主要问题在使用 @Import 注解需要人为梳理依赖树，稍微复杂一些的 Service 可能需要在注解中引入十几个类，既不美观效率又十分低下，这成为了大家使用这种单测方案的一大阻碍，针对该问题进行分析后发现这个编写成本是完全可以优化的，但是在现有的测试框架和 Spring 框架都没有针对这个问题的解决方案，所以为了扫除大家的使用障碍，持续提高单测效率，开发了一个名为 easy-spring-test 的工具，通过使用 @AutoImport 注解来替代 @Import，该注解会自动索引待测试类的 Spring Bean 依赖树，然后自动将对应的 Bean 注册到当前的 Spring 容器中。使用方式也十分简单：

首先，引入依赖包

然后使用 @AutoImport 注解替代 @Import 注解即可，举个例子

## 六、总结

业务系统在依赖 Spring 这种工业级框架的情况下写好真正的“单元测试”是非常困难的，但也不是没有解决方案，只要把握住在 Spring 环境下写好单元测试的几点要素即可：

**（1）遵循单元测试环境无关、测试单元最小化原则**

**（2）使用 H2 和 Database-rider 替代数据库进行 SQL 测试**

**（3）对本单元测试无关的功能禁止加载到 Spring 上下文中**

**（4）Spring 上下文中只包含待测试且依赖的部分内容**

**（5）对本单元测试依赖的但是属于不需要测试的部分进行 Mock**

单元测试是软件开发生命周期中非常重要的一环，如果能够写出高效且稳定的单元测试，那无疑是对软件质量保障的一大利器，同时写好单元测试也是开发人员必须掌握的基本功。希望我们研发团队在如何提高基于 Spring 环境单元测试的效率、稳定性与准确率的探索与实践，能够在构建高效稳定的单元测试上为大家提供帮助。

## 七、参考资料

[1][Mockito 官网](https://site.mockito.org/)

[2][H2DB Github](https://github.com/h2database/h2database)

[3][Database-rider Github](https://github.com/database-rider/database-rider)

[4][Spring Boot Test官方文档](https://docs.spring.io/spring-boot/docs/1.5.2.RELEASE/reference/html/boot-features-testing.html)

[5][Maven Surefire 插件官方文档](http://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html)

[6][Mockito: doReturn vs thenReturn](http://sangsoonam.github.io/2019/02/04/mockito-doreturn-vs-thenreturn.html)

[7][Springboot单元测试:SpyBean vs MockBean](https://juejin.cn/post/6881981078735699976)

[8][Github issue: AttachNotSupportedException: no providers installed](https://github.com/mockito/mockito/issues/978)