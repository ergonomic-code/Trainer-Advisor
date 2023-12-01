package pro.qyoga.clients

import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.clients.api.AuthApi


object PublicClient {

    val authApi = AuthApi

    fun getIndexPage(): Document {
        return Given {
            this
        } When {
            get()
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getFailPage(): Document {
        return Given {
            this
        } When {
            get("/fail")
        } Then {
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getNotFoundPage(): Document {
        return Given {
            this
        } When {
            get("/not-found")
        } Then {
            statusCode(HttpStatus.NOT_FOUND.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}