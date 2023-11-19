package pro.qyoga.infra.db

import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.RemoveObjectArgs
import org.slf4j.LoggerFactory
import pro.qyoga.tests.infra.db.minioContainer
import java.net.ConnectException

const val MINIO_URL = "http://localhost:50001"

object TestMinio

private val log = LoggerFactory.getLogger(TestMinio::class.java)

private const val MINIO_USER = "user"
private const val MINIO_PASSWORD = "password"


val minioUrl: String by lazy {
    try {
        val con = MinioClient.builder()
            .endpoint(MINIO_URL)
            .credentials(MINIO_USER, MINIO_PASSWORD)
            .build()
        con.listBuckets()
        log.info("Provided minio found, cleaning it")
        dropBuckets(con)
        log.info("Provided minio cleaned")
        MINIO_URL
    } catch (e: ConnectException) {
        log.info("minio container not found: ${e.message}")
        log.info("http://" + minioContainer.host + ":" + minioContainer.firstMappedPort)
        "http://" + minioContainer.host + ":" + minioContainer.firstMappedPort
    }
}

val testMinioClient by lazy {
    MinioClient.builder()
        .endpoint(minioUrl)
        .credentials(MINIO_USER, MINIO_PASSWORD)
        .build()
}

fun dropBuckets(client: MinioClient) {
    client.listBuckets().forEach { bucket ->
        client.listObjects(ListObjectsArgs.builder().bucket(bucket.name()).build()).forEach {
            client.removeObject(
                RemoveObjectArgs.builder().bucket(bucket.name()).`object`(it.get().objectName())
                    .build()
            )
        }
    }
}

