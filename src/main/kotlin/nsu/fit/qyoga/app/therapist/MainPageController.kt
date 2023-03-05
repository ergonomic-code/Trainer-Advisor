package nsu.fit.qyoga.app.therapist

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

private const val MAIN_PAGE = "therapist/main"

@Controller
@RequestMapping("/main")
class MainPageController {

    @GetMapping
    fun getMain(): String {
        return MAIN_PAGE
    }

}
