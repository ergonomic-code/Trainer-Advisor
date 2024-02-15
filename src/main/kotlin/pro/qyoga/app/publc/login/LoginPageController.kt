package pro.qyoga.app.publc.login

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

private const val AUTH_VIEW = "public/auth"

@Controller
class LoginPageController {

    @GetMapping("/login")
    fun getLoginPage(): String {
        return AUTH_VIEW
    }

    @PostMapping("/error-p")
    fun loginError(
        model: Model
    ): String? {
        model.addAttribute("loginError", true)
        return AUTH_VIEW
    }

}