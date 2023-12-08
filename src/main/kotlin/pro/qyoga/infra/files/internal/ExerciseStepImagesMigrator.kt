package pro.qyoga.infra.files.internal

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import org.springframework.stereotype.Service

@Service
class ExerciseStepImagesMigrator(
    private val minioClient: MinioClient,
    private val filesRepo: FilesRepo
) {
    private val buckedName: String = "images"
    fun migrateFiles() {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(buckedName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(buckedName).build())
        }

        for (file in filesRepo.findAll()) {
            minioClient.putObject(
                PutObjectArgs.builder().bucket(buckedName).`object`(file.name).contentType("image")
                    .stream(file.data.inputStream(), file.size, -1).build()
            )
        }

    }

}
