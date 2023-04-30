package nsu.fit.qyoga.app.therapist

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainPageController {

    @GetMapping("/")
    fun getMainPage(): String {
        return "redirect:/clients"
    }
}
