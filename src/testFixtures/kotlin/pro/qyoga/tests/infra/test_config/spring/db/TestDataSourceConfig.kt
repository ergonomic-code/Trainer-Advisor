package pro.qyoga.tests.infra.test_config.spring.db

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pro.qyoga.tests.infra.db.testDataSource
import javax.sql.DataSource


@TestConfiguration
class TestDataSourceConfig {

    @Bean
    fun dataSource(): DataSource = testDataSource

}