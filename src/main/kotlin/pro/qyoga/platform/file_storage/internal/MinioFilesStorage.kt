package pro.qyoga.platform.file_storage.internal

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import jakarta.annotation.PostConstruct
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile

class MinioFilesStorage(
    private val filesMetaDataRepo: FilesMetaDataRepo,
    private val minioClient: MinioClient,
    private val bucket: String
) : FilesStorage {

    @PostConstruct
    fun init() {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
        }
    }

    override fun uploadFile(file: StoredFile): FileMetaData {
        val putCommand = PutObjectArgs.builder()
            .bucket(bucket)
            .`object`(file.metaData.name)
            .contentType(file.metaData.mediaType)
            .stream(file.content.inputStream(), file.metaData.size, -1)
            .build()

        minioClient.putObject(putCommand)

        return filesMetaDataRepo.save(file.metaData.atBucket(bucket))
    }

    override fun uploadAll(files: Iterable<StoredFile>): Iterable<FileMetaData> {
        return files.map { uploadFile(it) }
    }

}