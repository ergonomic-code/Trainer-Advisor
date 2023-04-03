package nsu.fit.qyoga.app.anonymous

import nsu.fit.qyoga.core.users.api.User
import nsu.fit.qyoga.core.users.internal.UsersRepo
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
    private val userRepo: UsersRepo
) {

    @GetMapping("/users/login")
    fun getLoginPage(): String {
        return AUTH
    }

    @PostMapping("/error-p")
    fun loginError(
        @ModelAttribute("username") username: String,
        model: Model
    ): String? {
        val user: User? = userRepo.findByUsername(username)
        if (user != null) {
            model.addAttribute("loginError", "Неверный пароль")
        } else {
            model.addAttribute("loginError", "Неверный логин")
        }
        return AUTH
    }

}
