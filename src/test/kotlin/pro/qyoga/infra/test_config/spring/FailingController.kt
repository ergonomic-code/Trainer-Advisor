package pro.qyoga.infra.test_config.spring

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class FailingController {

    @GetMapping("/fail")
    fun fail() {
        error("Test error handling")
    }

}