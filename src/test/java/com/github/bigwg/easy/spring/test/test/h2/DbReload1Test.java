package com.github.bigwg.easy.spring.test.test.h2;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.h2.dao.StudentDao;
import com.github.bigwg.easy.spring.test.test.h2.domain.Student;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.spring.api.DBRider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * DbReloadTest
 *
 * @author zhaozhiwei
 * @since 2023/5/16
 */
@FixMethodOrder(MethodSorters.JVM)
@DBRider(dataSourceBeanName = "h2TestDataSource")
@ContextConfiguration("classpath:dataSource.xml")
public class DbReload1Test extends BaseTest {

    @Autowired
    private StudentDao studentDao;

    @Test
    @DataSet(value = {"/datasets/dbReloadTest1.json", "/datasets/configBase.json"}, cleanBefore = true)
    @ExpectedDataSet(value = "/datasets/dbReloadTest1Expect.json", ignoreCols = {"id"})
    public void testDbReload() {
        Student student = studentDao.queryById(1L);
        MatcherAssert.assertThat(student.getName(), Matchers.equalTo("张三"));
    }

    @Test
    @DataSet(value = {"/datasets/dbReloadTest1.json"},
            executeScriptsBefore = {"/datasets/configInit.sql"}, cleanBefore = true, strategy = SeedStrategy.INSERT)
    @ExpectedDataSet(value = {"/datasets/dbReloadTest1Expect.json", "/datasets/configBase.json"}, ignoreCols = {"id"})
    public void testExecuteScripts() {
        Student student = studentDao.queryById(1L);
        MatcherAssert.assertThat(student.getName(), Matchers.equalTo("张三"));
    }

    @Test
    @DataSet(value = "/datasets/dbReloadTest1.json", cleanBefore = true, skipCleaningFor = {"CONFIG"})
    @ExpectedDataSet(value = {"/datasets/dbReloadTest1Expect.json", "/datasets/configBase.json"}, ignoreCols = {"id"})
    public void testSkipCleaningFor() {
        Student student = studentDao.queryById(1L);
        MatcherAssert.assertThat(student.getName(), Matchers.equalTo("张三"));
    }

}
