package pro.qyoga.tests.platform


fun String.pathToRegex(): String {
    val pathAndQuery = this.split("?")
    val pathRegEx = pathAndQuery[0]
        .replace("\\{.*?}".toRegex(), "(.+)")
        .replace("/", "\\/")
    val queryRegEx = pathRegEx +
            (pathAndQuery.getOrNull(1)?.replace("\\{.*?}".toRegex(), "(.*)")?.let { "\\?$it" } ?: "")
    return queryRegEx
}
