package com.github.bigwg.easy.spring.test.test.context.mock;

import com.github.bigwg.easy.spring.test.annotation.MockStatic;
import com.github.bigwg.easy.spring.test.annotation.SpyStatic;
import com.github.bigwg.easy.spring.test.mock.MockitoStaticTestExecutionListener;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.TestExecutionListeners;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MockStaticTest
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@FixMethodOrder(MethodSorters.JVM)
public class MockStaticTest extends BaseTest {

    @MockStatic(RandomStringUtils.class)
    private MockedStatic<RandomStringUtils> randomStringUtils;

    @SpyStatic(value = LocalDateTime.class)
    private static MockedStatic<LocalDateTime> localDateTime;

    private static final String mockRandomStringStatic = "mockRandomStringStatic";

    private static final LocalDateTime mockNowStatic = LocalDateTime.of(2024, 2, 2, 0, 0, 0);

    @BeforeClass
    public static void beforeClass() {
        localDateTime.when(LocalDateTime::now).thenReturn(mockNowStatic);
        LocalDateTime now = LocalDateTime.now();
        MatcherAssert.assertThat(now, Matchers.equalTo(mockNowStatic));
    }

    @Before
    public void beforeMethod() {
        randomStringUtils.when(RandomStringUtils::random).thenReturn(mockRandomStringStatic);
    }

    @Test
    public void testMockInstanceFieldAndAfterReset() {
        String random = RandomStringUtils.random();
        MatcherAssert.assertThat(random, Matchers.equalTo(mockRandomStringStatic));
    }

    @Test
    public void mockStaticResetBeforeTest() {
        LocalDateTime now = LocalDateTime.now();
        MatcherAssert.assertThat(now, Matchers.not(Matchers.equalTo(mockNowStatic)));
    }

    @Test
    public void mockStaticAnnotationTest() {
        LocalDateTime mockNow = LocalDateTime.of(2023, 6, 19, 6, 0, 0);
        localDateTime.when(LocalDateTime::now).thenReturn(mockNow);
        LocalDateTime now = LocalDateTime.now();
        MatcherAssert.assertThat(now, Matchers.equalTo(mockNow));
    }

    @Test
    public void notMockStaticTest() {
        LocalDateTime mockNow = LocalDateTime.of(2023, 6, 19, 6, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        MatcherAssert.assertThat(now, Matchers.greaterThan(mockNow));
    }

    @Test
    public void nativeMockStaticTest() {
        LocalDate mockNow = LocalDate.of(2023, 6, 19);
        try (MockedStatic<LocalDate> nativeLocalDate = Mockito.mockStatic(LocalDate.class, Answers.CALLS_REAL_METHODS)) {
            nativeLocalDate.when(LocalDate::now).thenReturn(mockNow);
            LocalDate now = LocalDate.now();
            MatcherAssert.assertThat(now, Matchers.equalTo(mockNow));
        }
    }

}
