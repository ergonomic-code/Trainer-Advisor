package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateQuestionnaireAnswerViewTest : QYogaAppTestBase() {
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
    fun `QYoga can add answer to question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            patch("/therapist/questionnaires/edit/question/0/add-answer")
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
            delete("/therapist/questionnaires/edit/question/0/answer/0")
            patch("/therapist/questionnaires/edit/question/0/add-answer")
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
            patch("/therapist/questionnaires/edit/question/0/add-answer")
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
            patch("/therapist/questionnaires/edit/question/-1/add-answer")
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
    fun `QYoga ignore update answer if it not exists in the session's questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0/answer/0")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/answer/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't change answer if questionnaire not exists in session`() {
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
                    hasText("Выбранный ответ не найден Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can't change answer if it not exists in question`() {
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
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/set-scores")
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
            post("/therapist/questionnaires/edit/question/0/set-scores")
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
            post("/therapist/questionnaires/edit/question/-1/set-scores")
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
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/set-answers")
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
            post("/therapist/questionnaires/edit/question/0/set-answers")
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
    fun `QYoga can't get answer fragment to change answer fields if it not in question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/set-scores")
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
    fun `QYoga can't get answer fragment to set answer score if question not exist in session's questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/set-scores")
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
    fun `QYoga can't get answer fragment to set answer if question not exist in session's questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/set-answers")
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
