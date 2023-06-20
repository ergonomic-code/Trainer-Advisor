package nsu.fit.qyoga.cases.core.generateLink.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.platform.lang.toHexString
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.HMac
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.KeyParameter
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.SHA256Digest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class GenerateLinkViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @Value("\${qyoga.hash.secret-key}")
    lateinit var key: String

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/generate-link-init-script.sql" to "dataSource",
            "db/migration/demo/V23050904__insert_clients.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga return generate link page with list of clients`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/generate-link/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#searchClientsFilterForm") { exists() }
                node("#lastnameFilter") { exists() }
                node("#firstnameFilter") { exists() }
                node("#patronymicFilter") { exists() }
                node("#generate-link-content") { exists() }
                node("#client1") {
                    exists()
                    containsText("Иванов Иван Иванович")
                }
                node("#client2") {
                    exists()
                    containsText("Петров Пётр Петрович")
                }
                node("#client3") {
                    exists()
                    containsText("Сергеев Сергей Иванович")
                }
                node("#tablePagination") { exists() }
                node("#questionnaire-url") { exists() }
                node("#close-mw") { exists() }
            }
        }
    }

    @Test
    fun `QYoga return generate link page fragment with list of clients when user set params`() {
        Given {
            authorized()
        } When {
            contentType(ContentType.JSON)
            param("lastName", "п")
            param("firstName", "п")
            param("patronymic", "п")
            get("/therapist/questionnaires/generate-link/1/action")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#searchClientsFilterForm") { notExists() }
                node("#lastnameFilter") { notExists() }
                node("#firstnameFilter") { notExists() }
                node("#patronymicFilter") { notExists() }
                node("#generate-link-content") { exists() }
                node("#client1") { notExists() }
                node("#client2") {
                    exists()
                    containsText("Петров Пётр Петрович")
                }
                node("#client3") { notExists() }
                node("#tablePagination") { exists() }
                node("#questionnaire-url") { notExists() }
                node("#close-mw") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga return generate link page fragment with link to complete when user selects client`() {
        Given {
            authorized()
        } When {
            contentType(ContentType.JSON)
            param("lastName", "п")
            param("firstName", "п")
            param("patronymic", "п")
            get("/therapist/questionnaires/generate-link/1/1/generate")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            val hash = hashGenerator("questionnaireId:1clientId:1therapist:2")
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questionnaire-url-text") {
                    exists()
                    attribute("value") {
                        containsText("questionnaireId=1")
                        containsText("clientId=1")
                        containsText("therapistId=")
                        containsText(hash)
                    }
                }
            }
        }
    }

    fun hashGenerator(value: String): String {
        val hMac = HMac(SHA256Digest())
        hMac.init(KeyParameter(key.toByteArray()))
        val hmacIn = value.toByteArray()
        hMac.update(hmacIn, 0, hmacIn.size)
        val hmacOut = ByteArray(hMac.macSize)
        hMac.doFinal(hmacOut, 0)
        return hmacOut.toHexString()
    }
}