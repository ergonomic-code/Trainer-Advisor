package pro.qyoga.infra.html

class Link(
    val url: String,
    val text: String
) : Component {

    constructor(page: QYogaPage, text: String) : this(page.path, text)

    override fun selector(): String =
        "a[href=$url]:contains($text)"

}