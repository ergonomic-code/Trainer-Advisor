package pro.qyoga.tests.infra.test_config.spring

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class FailingController {

    @GetMapping("/test/fail")
    fun fail() {
        error("Test error handling")
    }

}