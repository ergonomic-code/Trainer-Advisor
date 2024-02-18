package pro.qyoga.tests.cases.platform.file_storage

import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import pro.azhidkov.platform.file_storage.internal.FilesMetaDataRepo
import pro.azhidkov.platform.file_storage.internal.MinioFilesStorage
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother
import pro.qyoga.tests.infra.db.testMinioClient
import pro.qyoga.tests.infra.test_config.spring.context


class MinioFilesStorageTest {

    @Test
    fun `Delete by id should delete only specified files`() {
        // Given
        val filesMetaDataRepo = FilesMetaDataRepo(
            context.getBean(JdbcAggregateOperations::class.java),
            context.getBean(JdbcConverter::class.java)
        )
        val storage = MinioFilesStorage(filesMetaDataRepo, testMinioClient, "test.bucket")
        storage.init()

        val files = (1..21).map { FilesObjectMother.randomFile() }
        val filesMeta = storage.uploadAll(files)
            .toList()

        // When
        storage.deleteById(filesMeta[0].id)

        // Then
        storage.findByIdOrNull(files[0].id) shouldBe null
        filesMeta.drop(1).forAll { storage.findByIdOrNull(it.id)?.inputStream?.close() shouldNotBe null }
    }

    @Test
    fun `Delete all by id should delete only specified files`() {
        // Given
        val filesMetaDataRepo = FilesMetaDataRepo(
            context.getBean(JdbcAggregateOperations::class.java),
            context.getBean(JdbcConverter::class.java)
        )
        val storage = MinioFilesStorage(filesMetaDataRepo, testMinioClient, "test.bucket")
        storage.init()

        val files = (1..21).map { FilesObjectMother.randomFile() }
        val filesMeta = storage.uploadAll(files)
            .toList()
        val toDelete = setOf(filesMeta[0], filesMeta[1], filesMeta[2], filesMeta[10], filesMeta[11], filesMeta[20])
            .map { it.id }

        // When
        storage.deleteAllById(toDelete)

        // Then
        toDelete.forAll { storage.findByIdOrNull(it) shouldBe null }
        filesMeta.filter { it.id !in toDelete }
            .forAll { storage.findByIdOrNull(it.id)?.inputStream?.close() shouldNotBe null }
    }

}