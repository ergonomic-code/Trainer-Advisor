package nsu.fit.qyoga.infra.fixture

import io.restassured.http.Header
import io.restassured.specification.RequestSpecification

fun RequestSpecification.token(token: String) =
    this.header("Authorization", "Bearer $token")
