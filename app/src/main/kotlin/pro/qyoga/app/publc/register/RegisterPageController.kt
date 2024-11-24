package pro.qyoga.app.publc.register

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapSuccess
import pro.azhidkov.platform.kotlin.recoverFailure
import pro.azhidkov.platform.spring.mvc.ModelBuilder
import pro.azhidkov.platform.spring.mvc.model
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.tech.captcha.CaptchaAnswer
import pro.qyoga.tech.captcha.CaptchaService
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Controller
class RegisterPageController(
    private val registerTherapist: RegisterTherapistOp,
    private val captchaService: CaptchaService,
    @Value("\${trainer-advisor.admin.email}") private val adminEmail: String
) {

    @GetMapping("/register")
    fun getRegisterPage(model: Model): ModelAndView {
        val (captchaId, captchaImage) = captchaService.generateCaptcha()
        return modelAndView(
            viewName = "public/register",
            model = registerPageModel(
                RegisterTherapistRequest("", "", "", CaptchaAnswer(captchaId, "")),
                captchaImage
            )
        )
    }

    @PostMapping("/register")
    fun register(registerTherapistRequest: RegisterTherapistRequest): ModelAndView {
        val res = runCatching {
            registerTherapist(registerTherapistRequest)
        }

        val modelAndView = res
            .mapSuccess {
                successMessageFragment(adminEmail)
            }
            .recoverFailure { ex: RegistrationException ->
                formWithValidationErrorFragment(registerTherapistRequest, ex, adminEmail)
            }
            .getOrThrow()

        return modelAndView
    }

}

private fun registerPageModel(
    registerTherapistRequest: RegisterTherapistRequest,
    captchaImage: BufferedImage,
    registrationException: RegistrationException? = null,
    adminEmail: String? = null
) = model {
    "requestForm" bindTo registerTherapistRequest
    "captchaImage" bindTo captchaImage.toBase64()
    if (adminEmail != null) {
        withAdminEmail(adminEmail)
    }
    if (registrationException != null) {
        "userAlreadyExists" bindTo registrationException.isDuplicatedEmail
        "incorrectCaptchaCode" bindTo registrationException.isInvalidCaptcha
    }
}

private fun successMessageFragment(adminEmail: String) =
    modelAndView("public/register-success-fragment") {
        withAdminEmail(adminEmail)
    }

private fun formWithValidationErrorFragment(
    registerTherapistRequest: RegisterTherapistRequest,
    registrationException: RegistrationException,
    adminEmail: String
): ModelAndView {
    return modelAndView(
        viewName = "public/register :: registerForm",
        model = registerPageModel(
            registerTherapistRequest.withCaptchaId(registrationException.newCaptcha.first),
            registrationException.newCaptcha.second,
            registrationException,
            adminEmail
        )
    )
}

private fun ModelBuilder.withAdminEmail(adminEmail: String) {
    "adminEmail" bindTo adminEmail
}

private fun BufferedImage.toBase64(): String {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(this, "png", outputStream)
    val base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray())
    outputStream.close()
    return base64Image
}
