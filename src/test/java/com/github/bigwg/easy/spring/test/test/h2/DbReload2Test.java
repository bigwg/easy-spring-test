package com.github.bigwg.easy.spring.test.test.h2;

import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.h2.dao.StudentDao;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * DbReloadTest
 *
 * @author zhaozhiwei
 * @since 2023/5/16
 */
@DBRider(dataSourceBeanName = "h2TestDataSource")
@ContextConfiguration("classpath:dataSource.xml")
public class DbReload2Test extends BaseTest {

    @Autowired
    private StudentDao studentDao;

    @Test
    @DataSet(value = "/datasets/dbReloadTest1.json", cleanBefore = true)
    public void testDbReload2_1() {

    }

    @Test
    @DataSet(value = "/datasets/dbReloadTest1.json")
    public void testDbReload2_2() {

    }

}
