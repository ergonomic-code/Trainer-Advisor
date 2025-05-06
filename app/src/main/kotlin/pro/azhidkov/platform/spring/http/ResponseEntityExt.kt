package pro.azhidkov.platform.spring.http

import org.springframework.http.ResponseEntity


fun hxRedirect(path: String, vararg headers: Pair<String, String>): ResponseEntity<Unit> {
    return ResponseEntity.ok()
        .header("HX-Redirect", path)
        .headers { headers.forEach { (key, value) -> it.add(key, value) } }
        .build()
}

fun hxClientSideRedirect(path: String): ResponseEntity<Unit> {
    return ResponseEntity.ok()
        .header("HX-Location", path)
        .build()
}
