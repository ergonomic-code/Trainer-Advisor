package pro.qyoga.tests.infra.test_config.spring.minio

import io.minio.MinioClient
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import pro.qyoga.infra.db.testMinioClient

@TestConfiguration
class TestMinioConfig {

    @Primary
    @Bean
    fun testMinioClient(): MinioClient = testMinioClient

}