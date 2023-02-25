package nsu.fit.qyoga.cases.core.exercises.ui

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

class ExerciseViewTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should return initial page`() {
        When {
            get("/exercises/all")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

    @Test
    fun `QYoga should return pagination view with default params`() {
        When {
            get("/exercises/")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

    @Test
    fun `QYoga should return pagination view using params`() {
        Given {
            param("pageSize", 20)
            param("pageNumber", 1)
        } When {
            get("/exercises/")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

    @Test
    fun `QYoga should return filtered view for collection with default params`() {
        When {
            get("/exercises/search")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

    @Test
    fun `QYoga should return filtered view for collection using params`() {
        Given {
            param("pageSize", 20)
            param("pageNumber", 1)
        } When {
            get("/exercises/search")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

}
