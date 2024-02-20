package pro.qyoga.app.publc.register

import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import pro.qyoga.core.users.therapists.RegisterTherapistRequest

@Controller
class RegisterPageController(
    private val registerTherapist: RegisterTherapistWorkflow,
    @Value("\${qyoga.admin.email}") private val adminEmail: String
) {

    @GetMapping("/register")
    fun getRegisterPage(model: Model): String {
        model.addAttribute("requestForm", RegisterTherapistRequest("", "", ""))
        return "public/register"
    }

    @PostMapping("/register")
    fun register(registerTherapistRequest: RegisterTherapistRequest, model: Model): String {
        try {
            registerTherapist(registerTherapistRequest)
            return "public/register-success-fragment"
        } catch (ex: DuplicateKeyException) {
            model.addAttribute("userAlreadyExists", true)
            model.addAttribute("adminEmail", adminEmail)
            model.addAttribute("requestForm", registerTherapistRequest)
            return "public/register :: registerForm"
        }
    }

}