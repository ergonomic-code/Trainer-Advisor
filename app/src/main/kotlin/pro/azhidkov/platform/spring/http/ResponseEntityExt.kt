package pro.azhidkov.platform.spring.http

import org.springframework.http.ResponseEntity


fun hxRedirect(path: String): ResponseEntity<Unit> {
    return ResponseEntity.ok()
        .header("HX-Redirect", path)
        .build()
}

