package nsu.fit.qyoga

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
class StarterController {

    @GetMapping("/")
    fun getTrue() : ResponseEntity<Boolean> {
        return ResponseEntity.ok().body(true)
    }
}