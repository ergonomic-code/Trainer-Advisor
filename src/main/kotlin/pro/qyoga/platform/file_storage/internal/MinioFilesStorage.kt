package pro.qyoga.platform.file_storage.internal

import io.minio.*
import jakarta.annotation.PostConstruct
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile
import java.io.InputStream

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
        val storedFile = filesMetaDataRepo.save(file.metaData.atBucket(bucket))
        val putCommand = PutObjectArgs.builder()
            .bucket(bucket)
            .`object`(storedFile.id.toString())
            .contentType(file.metaData.mediaType)
            .stream(file.content.inputStream(), file.metaData.size, -1)
            .build()

        minioClient.putObject(putCommand)

        return storedFile
    }

    override fun uploadAll(files: Iterable<StoredFile>): Iterable<FileMetaData> {
        return files.map { uploadFile(it) }
    }

    override fun findByIdOrNull(fileId: Long): InputStream? {
        val getCommand = GetObjectArgs.builder()
            .bucket(bucket)
            .`object`(fileId.toString())
            .build()

        return minioClient.getObject(getCommand)
    }

}