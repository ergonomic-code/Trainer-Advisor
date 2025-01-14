package pro.qyoga.app.therapist.account

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class SettingsPageController {

    @GetMapping(PATH)
    fun handleGetSettingsPage(): String {
        return "therapist/account/settings"
    }

    companion object {

        const val PATH = "/therapist/account/settings"

    }

}