package pro.qyoga.app.therapist.root

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import pro.qyoga.app.therapist.clients.list.ClientsListPageController


@Controller
class TherapistMainPageController {

    @GetMapping("/therapist", "/therapist/")
    fun getTherapistMainPage(): String {
        return "redirect:${ClientsListPageController.PATH}"
    }

}