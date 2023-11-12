package pro.qyoga.infra.html


data class FormAction(
    val attr: String,
    val url: String
) {

    companion object {

        fun classic(url: String) =
            FormAction("action", url)

        fun hxGet(url: String): FormAction =
            FormAction("hx-get", url)

        fun hxPost(url: String): FormAction =
            FormAction("hx-post", url)

    }

}