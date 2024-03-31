package pro.qyoga.app.publc.register

import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.users.therapists.RegisterTherapistRequest

@Controller
class RegisterPageController(
    private val registerTherapist: RegisterTherapistWorkflow,
    @Value("\${trainer-advisor.admin.email}") private val adminEmail: String
) {

    @GetMapping("/register")
    fun getRegisterPage(model: Model): String {
        model.addAttribute("requestForm", RegisterTherapistRequest("", "", ""))
        return "public/register"
    }

    @PostMapping("/register")
    fun register(registerTherapistRequest: RegisterTherapistRequest): ModelAndView {
        try {
            registerTherapist(registerTherapistRequest)
            return modelAndView("public/register-success-fragment") {
                "adminEmail" bindTo adminEmail
            }
        } catch (ex: DuplicateKeyException) {
            return modelAndView("public/register :: registerForm") {
                "userAlreadyExists" bindTo true
                "adminEmail" bindTo adminEmail
                "requestForm" bindTo registerTherapistRequest
            }
        }
    }
}