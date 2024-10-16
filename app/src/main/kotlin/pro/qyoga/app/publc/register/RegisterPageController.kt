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
import pro.qyoga.tech.captcha.CaptchaAnswer
import pro.qyoga.tech.captcha.CaptchaService
import pro.qyoga.tech.captcha.IncorrectCaptchaCodeException
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Controller
class RegisterPageController(
    private val registerTherapist: RegisterTherapistWorkflow,
    private val captchaService: CaptchaService,
    @Value("\${trainer-advisor.admin.email}") private val adminEmail: String
) {

    @GetMapping("/register")
    fun getRegisterPage(model: Model): String {
        val (captchaHash, captchaImage) = captchaService.generateCaptcha()
        model.addAttribute("requestForm", RegisterTherapistRequest("", "", "", CaptchaAnswer(captchaHash, "", bufferedImageToBase64Png(captchaImage))))
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
                formWithValidationErrorFragment(registerTherapistRequest, false)
            }
                .mapFailure { _: IncorrectCaptchaCodeException ->
                    formWithValidationErrorFragment(registerTherapistRequest, true)
            }
            .getOrThrow()

        return modelAndView
    }

    private fun formWithValidationErrorFragment(registerTherapistRequest: RegisterTherapistRequest, incorrectCaptchaCode: Boolean): ModelAndView {
        val (captchaHash, captchaImage) = captchaService.generateCaptcha()
        return modelAndView("public/register :: registerForm") {
            "userAlreadyExists" bindTo !incorrectCaptchaCode
            "incorrectCaptchaCode" bindTo incorrectCaptchaCode
            "adminEmail" bindTo adminEmail
            "requestForm" bindTo RegisterTherapistRequest(
                    registerTherapistRequest.firstName,
                    registerTherapistRequest.lastName,
                    registerTherapistRequest.email,
                    CaptchaAnswer(captchaHash, "", bufferedImageToBase64Png(captchaImage))
            )
        }
    }

    private fun successMessageFragment() =
        modelAndView("public/register-success-fragment") {
            "adminEmail" bindTo adminEmail
        }

    private fun bufferedImageToBase64Png(captchaImage: BufferedImage): String {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(captchaImage, "png", outputStream)
        val base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray())
        outputStream.close()
        return base64Image
    }
}