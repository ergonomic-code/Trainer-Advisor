package nsu.fit.qyoga.app.anonymous

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

private const val AUTH = "auth"

@Controller
@RequestMapping("/users")
class LoginPageController {

    @GetMapping("/login")
    fun getLoginPage(): String {
        return AUTH
    }

}
