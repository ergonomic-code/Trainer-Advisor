package pro.qyoga.tests.platform.html


fun elementDescr(
    tag: String,
    idValue: String?,
    elClassValue: String?,
    nameValue: String?,
    text: String?
): String {
    val id = idValue?.takeIf { it.isNotBlank() }
        ?.let { "id=\"$it\"" } ?: ""
    val elClass = elClassValue?.takeIf { it.isNotBlank() }
        ?.let { "class=\"${it.split(" ").first()} ...\"" } ?: ""
    val name = nameValue?.takeIf { it.isNotBlank() }
        ?.let { "name=\"$it\"" } ?: ""

    return "<$tag $id $elClass $name>" +
            (text?.take(16)?.let { "$it ..." } ?: "") +
            "</$tag>"
}
