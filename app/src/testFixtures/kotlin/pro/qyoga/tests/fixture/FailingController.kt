package pro.qyoga.tests.fixture

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class FailingController {

    @GetMapping("/test/fail")
    fun fail() {
        error("Test error handling")
    }

}