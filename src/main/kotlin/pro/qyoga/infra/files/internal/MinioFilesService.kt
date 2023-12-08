package pro.qyoga.infra.files.internal

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import pro.qyoga.infra.files.api.File
import pro.qyoga.infra.files.api.FilesService

class MinioFilesService
    (
    private val filesRepo: FilesRepo,
    private val minioClient: MinioClient
) : FilesService {

    override fun uploadFile(file: File): Long {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket("images").build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket("images").build())
        }
        minioClient.putObject(
            PutObjectArgs.builder().bucket("images").`object`(file.name).contentType("image")
                .stream(file.data.inputStream(), file.size, -1).build()
        )
        return filesRepo.save(file).id
    }
}