package pro.qyoga.app.publc.register

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapFailure
import pro.azhidkov.platform.kotlin.mapSuccess
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.users.auth.errors.DuplicatedEmailException
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
        val res = runCatching {
            registerTherapist(registerTherapistRequest)
        }

        val modelAndView = res
            .mapSuccess {
                successMessageFragment()
            }
            .mapFailure { _: DuplicatedEmailException ->
                formWithValidationErrorFragment(registerTherapistRequest)
            }
            .getOrThrow()

        return modelAndView
    }

    private fun formWithValidationErrorFragment(registerTherapistRequest: RegisterTherapistRequest) =
        modelAndView("public/register :: registerForm") {
            "userAlreadyExists" bindTo true
            "adminEmail" bindTo adminEmail
            "requestForm" bindTo registerTherapistRequest
        }

    private fun successMessageFragment() =
        modelAndView("public/register-success-fragment") {
            "adminEmail" bindTo adminEmail
        }

}