package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.servlet.client.RestTestClient
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.clients.files.ClientFilesPage
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import java.util.*


class TherapistClientFilesApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getClientFilesPage(clientId: UUID, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(ClientFilesPage.path, clientId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun upload(clientId: UUID, file: StoredFile, expectedStatus: HttpStatus = HttpStatus.OK): Document? {
        val multipart = MultipartBodyBuilder()
        multipart.part(ClientFilesPage.UploadFileForm.fileInput.name, file.content)
            .filename(file.metaData.name)
            .contentType(MediaType.parseMediaType(file.metaData.mediaType))
        val response = restTestClient.post()
            .uri(ClientFilesPage.UploadFileForm.action.url, clientId)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(multipart.build())
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
        return response.takeIf { it.isNotEmpty() }?.let { Jsoup.parse(it) }
    }

    fun download(clientId: UUID, fileId: Long): RestTestClient.ResponseSpec {
        return restTestClient.get()
            .uri(ClientFilesPage.filePath, clientId, fileId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
    }

    fun deleteFile(clientId: UUID, fileId: Long): Document? {
        val response = restTestClient.delete()
            .uri(ClientFilesPage.filePath, clientId, fileId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isOk
            .getBodyAsString()
        return response.takeIf { it.isNotEmpty() }?.let { Jsoup.parse(it) }
    }

}
