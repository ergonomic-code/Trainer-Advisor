package nsu.fit.qyoga.cases.core.users.ui

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoginViewTest {
    private var webClient: WebClient = WebClient()

    @Before
    @Throws(Exception::class)
    fun init() {
        webClient = WebClient()
    }

    @After
    @Throws(Exception::class)
    fun close() {
        webClient.close()
    }

    @Test
    @Throws(Exception::class)
    fun check_that_title_is_correct() {
        val page: HtmlPage = webClient.getPage("http://localhost/users/login")
        Assert.assertEquals(
            "Аутентификация",
            page.getTitleText()
        )
    }
}
