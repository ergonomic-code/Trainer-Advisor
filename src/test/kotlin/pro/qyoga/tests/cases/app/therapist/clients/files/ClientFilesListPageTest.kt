package pro.qyoga.tests.cases.app.therapist.clients.files

import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpStatus
import pro.qyoga.core.clients.files.impl.ClientFilesRepo
import pro.qyoga.platform.spring.sdj.erpo.hydration.resolveOrThrow
import pro.qyoga.tests.assertions.shouldBeElement
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.clients.pages.therapist.clients.files.ClientFilesPage
import pro.qyoga.tests.fixture.FilesObjectMother
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class ClientFilesListPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Request of client files page for not existing client should return 404`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clientFiles.getClientFilesPage(-1, expectedStatus = HttpStatus.NOT_FOUND)

        // Then
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Client files page should be rendered correctly when no files exist`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clientFiles.getClientFilesPage(client.id)

        // Then
        document shouldBePage ClientFilesPage
    }

    @Test
    fun `When file of valid size is uploaded then its fragment should be returned and it should be in list of all client files`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile(size = 1024)

        // When
        val document = therapist.clientFiles.upload(client.id, file)!!

        // Then
        val files = backgrounds.clientFiles.findAll(client.id).content
        files shouldHaveAtLeastSize 1
        val persistedFile = backgrounds.clientFiles.findFile(client.id, file)
        persistedFile shouldNotBe null
        document shouldBeElement ClientFilesPage.rowFor(persistedFile!!.fileRef.resolveOrThrow())
    }

    @Test
    fun `Client files page should contain rows for at most ten files`() {
        // Given
        val pageSize = ClientFilesRepo.Page.tenNewest.pageSize
        val filesCount = pageSize + 1
        val client = backgrounds.clients.createClients(1).single()
        val files = backgrounds.clientFiles.createFiles(client.id, filesCount)
        val filesPage = files.sortedByDescending { it.createdAt }
            .take(pageSize)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clientFiles.getClientFilesPage(client.id)

        // Then
        document shouldHave ClientFilesPage.rowsFor(filesPage)
    }

    @Test
    fun `Request for not existing client file should return 404`() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1).single()

        // When
        val response = therapist.clientFiles.download(client.id, -1)

        response.Then {
            statusCode(HttpStatus.NOT_FOUND.value())
        } Extract {
            Jsoup.parse(body().asString()) shouldBePage NotFoundErrorPage
        }
    }

    @Test
    fun `Downloaded client file should be the same as uploaded`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile()
        val clientFile = backgrounds.clientFiles.createFile(1, file)

        // When
        val result = therapist.clientFiles.download(client.id, clientFile.id)

        result.Then {
            statusCode(HttpStatus.OK.value())
            contentType(clientFile.fileRef.resolveOrThrow().mediaType)
            header("Content-Length", clientFile.fileRef.resolveOrThrow().size.toString())
        } Extract {
            val body = asByteArray()
            body shouldBe file.content
        }
    }

    @Test
    fun `Downloaded client file with cyrillic name should be the same as uploaded`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile(name = randomCyrillicWord())
        val clientFile = backgrounds.clientFiles.createFile(1, file)

        // When
        val result = therapist.clientFiles.download(client.id, clientFile.id)

        result.Then {
            statusCode(HttpStatus.OK.value())
            contentType(clientFile.fileRef.resolveOrThrow().mediaType)
            header("Content-Length", clientFile.fileRef.resolveOrThrow().size.toString())
        } Extract {
            val contentDisposition = header("Content-Disposition")
            ContentDisposition.parse(contentDisposition).filename shouldBe file.metaData.name
            val body = asByteArray()
            body shouldBe file.content
        }
    }

    @Test
    fun `After deletion of client file it should disappear from client files response list`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile(name = randomCyrillicWord())
        val clientFile = backgrounds.clientFiles.createFile(1, file)

        // When
        val document = therapist.clientFiles.deleteFile(client.id, clientFile.id)

        // Then
        document shouldBe null
        val persistedFile = backgrounds.clientFiles.findFile(client.id, file)
        persistedFile shouldBe null

    }

    @Test
    fun `Uploading of file of 10 MB should complete successfully`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile(size = 1024 * 1024 * 10 - 1024)

        // When
        val document = therapist.clientFiles.upload(client.id, file, expectedStatus = HttpStatus.OK)!!

        // Then
        val persistedFile = backgrounds.clientFiles.findFile(client.id, file)!!
        document shouldBeElement ClientFilesPage.rowFor(persistedFile.fileRef.resolveOrThrow())
    }

    @Test
    fun `Uploading of file of 10 MB + 1 byte should fail with 413 status code`() {
        // Given
        val client = backgrounds.clients.createClients(1).single()
        val therapist = TherapistClient.loginAsTheTherapist()
        val file = FilesObjectMother.randomFile(size = 1024 * 1024 * 10 + 1)

        // When
        val document = therapist.clientFiles.upload(client.id, file, expectedStatus = HttpStatus.PAYLOAD_TOO_LARGE)

        // Then
        document shouldBe null
    }

}