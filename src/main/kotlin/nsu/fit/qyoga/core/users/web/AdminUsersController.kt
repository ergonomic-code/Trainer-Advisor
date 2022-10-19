package nsu.fit.qyoga.core.users.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class AdminUsersController {

    @PostMapping
    fun createUser() {

    }

}