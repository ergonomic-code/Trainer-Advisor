package pro.qyoga.platform.file_storage.internal

import io.minio.*
import io.minio.messages.DeleteObject
import jakarta.annotation.PostConstruct
import org.springframework.data.repository.findByIdOrNull
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.FilesStorage
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.platform.file_storage.api.StoredFileInputStream

open class MinioFilesStorage(
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

    override fun findByIdOrNull(fileId: Long): StoredFileInputStream? {
        val metadata = filesMetaDataRepo.findByIdOrNull(fileId)
            ?: return null

        val getCommand = GetObjectArgs.builder()
            .bucket(bucket)
            .`object`(fileId.toString())
            .build()

        return StoredFileInputStream(metadata, minioClient.getObject(getCommand))
    }

    override fun deleteAllById(fileIds: List<Long>) {
        filesMetaDataRepo.deleteAllById(fileIds)

        val objects = fileIds.map { DeleteObject(it.toString()) }
        val removeCommand = RemoveObjectsArgs.builder()
            .bucket(bucket)
            .objects(objects)
            .build()
        minioClient.removeObjects(removeCommand)
    }

}