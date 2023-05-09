package nsu.fit.platform.spring

import org.springframework.http.ResponseEntity


fun hxRedirect(path: String): ResponseEntity<Unit> {
    return ResponseEntity.ok()
        .header("HX-Redirect", path)
        .build()
}

