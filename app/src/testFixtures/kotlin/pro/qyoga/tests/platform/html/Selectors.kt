package pro.qyoga.tests.platform.html


fun selector(
    id: String?,
    clazz: String?,
    tag: String?
): String {
    val selector = buildString {
        if (tag != null) {
            append(tag)
        }
        if (id != null) {
            append("#$id")
        }
        if (clazz != null) {
            append(".$clazz")
        }
    }
    check(selector.isNotBlank()) { "Selector must not be blank" }
    return selector
}