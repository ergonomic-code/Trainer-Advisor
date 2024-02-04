package pro.qyoga.core.clients.files

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pro.azhidkov.platform.file_storage.internal.FilesMetaDataRepo
import pro.azhidkov.platform.file_storage.internal.MinioFilesStorage


@Configuration
class ClientFilesConfig(
    private val filesMetaDataRepo: FilesMetaDataRepo,
    private val minioClient: MinioClient
) {

    @Bean
    fun clientFilesStorage() =
        MinioFilesStorage(filesMetaDataRepo, minioClient, "clients.files")

}