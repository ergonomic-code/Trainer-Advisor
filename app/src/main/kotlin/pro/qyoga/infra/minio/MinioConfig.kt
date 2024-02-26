package pro.qyoga.infra.minio

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MinioConfig {

    @Value("\${minio.endpoint}")
    private lateinit var minioEndpoint: String

    @Value("\${minio.user}")
    private lateinit var minioUser: String

    @Value("\${minio.password}")
    private lateinit var minioPassword: String

    @Bean
    fun minioClient(): MinioClient {
        return MinioClient.builder()
            .endpoint(minioEndpoint)
            .credentials(minioUser, minioPassword)
            .build()
    }

}