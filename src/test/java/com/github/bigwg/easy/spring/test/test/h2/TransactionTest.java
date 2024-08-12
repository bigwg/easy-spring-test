package com.github.bigwg.easy.spring.test.test.h2;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.test.BaseTest;
import com.github.bigwg.easy.spring.test.test.h2.dao.ScoreDao;
import com.github.bigwg.easy.spring.test.test.h2.service.StudentScoreService;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

/**
 * TransactionTest
 *
 * @author zhaozhiwei
 * @since 2023/5/17
 */
@DBRider(dataSourceBeanName = "h2TestDataSource")
@ContextConfiguration(value = {"classpath:dataSource.xml"})
@AutoImport({StudentScoreService.class})
public class TransactionTest extends BaseTest {

    @Autowired
    private StudentScoreService studentScoreService;
    @SpyBean
    private ScoreDao scoreDao;

    @Test
    @DataSet(value = "datasets/transaction.json")
    @ExpectedDataSet(value = "datasets/transactionCommitExpect.json", ignoreCols = {"create_time", "update_time"})
    public void testTransactionCommit() {
        LocalDateTime examTime = LocalDateTime.of(2023, 5, 17, 16, 0, 0);
        studentScoreService.exam(1L, examTime, 3, "90");
    }

    @Test
    @DataSet(value = "datasets/transaction.json")
    @ExpectedDataSet(value = "datasets/transactionRollbackExpect.json", ignoreCols = {"create_time", "update_time"})
    public void testTransactionRollback() {
        Mockito.doThrow(new RuntimeException("transaction rollback")).when(scoreDao).batchInsert(Mockito.anyList());
        LocalDateTime examTime = LocalDateTime.of(2023, 5, 17, 16, 0, 0);
        try {
            studentScoreService.exam(1L, examTime, 3, "90");
        } catch (Exception e) {
            MatcherAssert.assertThat(e.getMessage(), Matchers.equalTo("transaction rollback"));
        }
    }

}
