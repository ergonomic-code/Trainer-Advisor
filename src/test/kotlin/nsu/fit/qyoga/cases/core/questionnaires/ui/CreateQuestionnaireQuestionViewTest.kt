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

class CreateQuestionnaireQuestionViewTest : QYogaAppTestBase() {
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
    fun `QYoga can add question to questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
                node("#question1") { exists() }
                node("#question1Header") { exists() }
                node("#question1Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can add question to questionnaire if there are no question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
            get("/therapist/questionnaires/edit/add-question")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
                node("#question1") { notExists() }
                node("#question1Header") { notExists() }
                node("#question1Body") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can't add question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit/add-question")
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
    fun `QYoga can delete question from questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { notExists() }
                node("#question0Header") { notExists() }
                node("#question0Body") { notExists() }
                node("#answer0") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can delete question from questionnaire question list`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            delete("/therapist/questionnaires/edit/question/0")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { notExists() }
                node("#question0Header") { notExists() }
                node("#question0Body") { notExists() }
                node("#question1") { exists() }
                node("#question1Header") { exists() }
                node("#question1Body") { exists() }
            }
        }
    }

    @Test
    fun `QYoga ignore delete question if it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/-1")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't delete question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0")
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
    fun `QYoga can change question type`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            param("question[0].answers[0].id", "1")
            param("question[0].answers[0].questionId", "1")
            param("question[0].answers[0].score", "1")
            param("question[0].answers[0].title", "answer title")
            param("question[1].id", "1")
            param("question[1].title", "questions title")
            param("question[1].questionType", "SEVERAL")
            param("question[1].answers[0].id", "1")
            param("question[1].answers[0].questionId", "1")
            param("question[1].answers[0].score", "1")
            param("question[1].answers[0].title", "answer title")
            post("/therapist/questionnaires/edit/question/0/change-type")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".typeSEVERAL") { hasAttribute("selected") }
                node(".typeSINGLE") { notHasAttribute("selected") }
                node(".typeTEXT") { notHasAttribute("selected") }
                node(".typeRANGE") { notHasAttribute("selected") }
            }
        }
    }

    @Test
    fun `QYoga can't change question type if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            post("/therapist/questionnaires/edit/question/0/change-type")
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
    fun `QYoga can't change question type it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            post("/therapist/questionnaires/edit/question/-1/change-type")
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
    fun `QYoga can update question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            param("question[0].answers[0].id", "0")
            param("question[0].answers[0].score", "1")
            param("question[0].answers[0].title", "answer title")
            param("question[1].id", "1")
            param("question[1].title", "questions title")
            param("question[1].questionType", "SEVERAL")
            param("question[1].answers[0].id", "0")
            param("question[1].answers[0].score", "1")
            param("question[1].answers[0].title", "answer title")
            post("/therapist/questionnaires/edit/question/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't update question type if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            post("/therapist/questionnaires/edit/question/0/update")
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
    fun `QYoga can't update question type it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            param("id", "1")
            param("title", "test")
            param("question[0].id", "0")
            param("question[0].title", "questions title")
            param("question[0].questionType", "SEVERAL")
            post("/therapist/questionnaires/edit/question/-1/update")
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
    fun `QYoga can save question image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
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
    fun `QYoga can't add image if questionnaire not in session`() {
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
    fun `QYoga can delete question image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
            delete("/therapist/questionnaires/edit/question/0/image")
        } Then {
            extract().body().asString() shouldBe ""
        }
    }

    @Test
    fun `QYoga can't delete image if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0/image")
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
}
