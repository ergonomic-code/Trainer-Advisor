package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

class ErrorViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can return a error-fragment when trying to add an image to a non-existent question`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/db/questionnaires/testLargeImage.jpg"))
            param("questionIndex", 0)
            post("/questionnaires/question/1/image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранный вопрос не найден Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can return a error-fragment when trying to add an image to a non-existent answer`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/db/questionnaires/testLargeImage.jpg"))
            param("questionIndex", 0)
            param("answerIndex", 0)
            post("/questionnaires/answer/1/image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранный ответ не найден Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can return a error-fragment when trying to get non-existent image`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/image/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранное изображение не найдено Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can return a error-page when trying to add an image to a non-existent questionnaire`() {
        Given {
            this.cookie(getAuthCookie())
        } When {
            get("/questionnaires/2/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") { exists() }
                node("#error-text") {
                    exists()
                    hasText("Выбранный опросник не найден")
                }
            }
        }
    }
}
