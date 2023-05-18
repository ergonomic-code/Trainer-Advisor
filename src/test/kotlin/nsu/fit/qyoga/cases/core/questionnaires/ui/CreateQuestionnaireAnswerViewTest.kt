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

class CreateQuestionnaireAnswerViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can save answer image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
            get("/therapist/questionnaires/edit/add-question")
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
    fun `QYoga can't add image if questionnaire not in session`() {
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
    fun `QYoga can delete question image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
            get("/therapist/questionnaires/edit/add-question")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
            delete("/therapist/questionnaires/edit/question/0/answer/0/image")
        } Then {
            extract().body().asString() shouldBe ""
        }
    }

    @Test
    fun `QYoga can't delete image if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0/answer/0/image")
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
    fun `QYoga can add answer to question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0QuestionId") { exists() }
                node(".answer0Score") { exists() }
                node("#answer1") { exists() }
                node(".answer1Id") { exists() }
                node(".answer1QuestionId") { exists() }
                node(".answer1Score") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can add answer to question then it empty`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            delete("/therapist/questionnaires/edit/question/0/answer/0")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0QuestionId") { exists() }
                node(".answer0Score") { exists() }
                node("#answer1") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can't add answer if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit/question/0/addAnswer")
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
    fun `QYoga can't add answer to question if it not exists in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            get("/therapist/questionnaires/edit/question/-1/addAnswer")
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
    fun `QYoga can delete answer from question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            delete("/therapist/questionnaires/edit/question/0/answer/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { notExists() }
                node(".answer0Id") { notExists() }
                node(".answer0QuestionId") { notExists() }
                node(".answer0Score") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga ignore delete answer if it not in question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0/answer/-1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0QuestionId") { exists() }
                node(".answer0Score") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can delete answer from question answer list`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
            delete("/therapist/questionnaires/edit/question/0/answer/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { notExists() }

                node("#answer1") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0Score") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't delete answer if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0/answer/0")
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
    fun `QYoga can't delete answer to question if it not exists in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/-1/answer/0")
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
    fun `QYoga can update answer in list of answers`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can update answer`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga ignore update answer if it not exists in question from session`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            params(
                mutableMapOf(
                    "question[0].answers[0].id" to "0",
                    "question[0].answers[0].score" to "1",
                    "question[0].answers[0].title" to "answer title"
                )
            )
            post("/therapist/questionnaires/edit/question/0/answer/1/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't change answer if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/0/update")
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
    fun `QYoga can't change answer to question if it not exists in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/answer/0/update")
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
    fun `QYoga can't change answer to question if it not exists in question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/-1/update")
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
    fun `QYoga can change answer fragment to set answer score`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/add-question")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/setScores")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".answerScore") { exists() }
                node(".saveScore") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't change answer fragment to set answer score if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/setScores")
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
    fun `QYoga can't get answer fragment to set answer score if not in question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/answer/setScores")
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
    fun `QYoga can change answer fragment to change answer fields after set scores`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit/question/0/addAnswer")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/setAnswers")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }

                node("#answer0") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0QuestionId") { exists() }
                node(".answer0Score") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can't change answer fragment to change answer fields if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/answer/setAnswers")
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
    fun `QYoga can't get answer fragment to change answer fields if not in question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/answer/setScores")
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

    fun setParams(): Map<String, String> {
        return mutableMapOf(
            "id" to "1",
            "title" to "test",
            "question[0].id" to "0",
            "question[0].title" to "questions title",
            "question[0].questionType" to "SEVERAL",
            "question[0].answers[0].id" to "0",
            "question[0].answers[0].score" to "1",
            "question[0].answers[0].title" to "answer title"
        )
    }
}
