package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.tests.pages.therapist.clients.files.ClientFilesPage

class TherapistClientFilesApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getClientFilesPage(clientId: Long, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()

            pathParam("clientId", clientId)
        } When {
            get(ClientFilesPage.path)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun upload(clientId: Long, file: StoredFile, expectedStatus: HttpStatus = HttpStatus.OK): Document? {
        return Given {
            contentType("multipart/form-data; charset=UTF-8")
            authorized()

            pathParam("clientId", clientId)

            multiPart(
                ClientFilesPage.UploadFileForm.fileInput.name,
                file.metaData.name,
                file.content,
                file.metaData.mediaType
            )
        } When {
            post(ClientFilesPage.UploadFileForm.action.url)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            val responseString = body().asString()
            if (responseString.isNotEmpty()) {
                Jsoup.parse(responseString)
            } else {
                null
            }
        }
    }

    fun download(clientId: Long, fileId: Long): Response {
        return Given {
            authorized()

            pathParam("clientId", clientId)
            pathParam("fileId", fileId)
        } When {
            get(ClientFilesPage.filePath)
        }
    }

    fun deleteFile(clientId: Long, fileId: Long): Document? {
        return Given {
            authorized()

            pathParam("clientId", clientId)
            pathParam("fileId", fileId)
        } When {
            delete(ClientFilesPage.filePath)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            val responseString = body().asString()
            if (responseString.isNotEmpty()) {
                Jsoup.parse(responseString)
            } else {
                null
            }
        }
    }

}
