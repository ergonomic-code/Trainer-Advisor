package nsu.fit.qyoga.app.anonymous

import nsu.fit.qyoga.core.users.api.User
import nsu.fit.qyoga.core.users.api.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

private const val AUTH = "auth"

@Controller
@RequestMapping()
class LoginPageController(
    private val userService: UserService
) {

    @GetMapping("/login")
    fun getLoginPage(): String {
        return AUTH
    }

    @PostMapping("/error-p")
    fun loginError(
        @ModelAttribute("username") username: String,
        model: Model
    ): String? {
        val user: User? = userService.findByUsername(username)
        if (user != null) {
            model.addAttribute("passwordError", "Неверный пароль")
        } else {
            model.addAttribute("loginError", "Неверный логин")
        }
        return AUTH
    }

}
