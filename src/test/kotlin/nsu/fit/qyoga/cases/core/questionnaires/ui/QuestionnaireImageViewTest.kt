package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.kotest.matchers.shouldBe
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

class QuestionnaireImageViewTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can save answer image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".answerImageId") { exists() }
                node(".answerImage") { exists() }
                node(".answerDeleteImage") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't add image to answer if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can't add image to answer if answer not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("therapist/questionnaires/edit/question/0/answer/-1/add-image")
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
    fun `QYoga can delete question image from answer`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
            delete("/therapist/questionnaires/edit/question/0/answer/0/image/0")
        } Then {
            extract().body().asString() shouldBe ""
        }
    }

    @Test
    fun `QYoga can't delete image from answer if answer not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
            delete("/therapist/questionnaires/edit/question/0/answer/-1/image/1")
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
    fun `QYoga can't delete image from answer if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0/answer/0/image/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can add image to question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".questionImageId") { exists() }
                node(".questionImage") { exists() }
                node(".questionDeleteImage") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't add image to question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can delete question image from question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
            delete("/therapist/questionnaires/edit/question/0/image/0")
        } Then {
            extract().body().asString() shouldBe ""
        }
    }

    @Test
    fun `QYoga can't delete image from question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0/image/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can't add image to question if it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("therapist/questionnaires/edit/question/-1/add-image")
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
    fun `QYoga can't delete image from question if it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
            delete("/therapist/questionnaires/edit/question/-1/image/1")
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
}