package pro.qyoga.platform.kotlin

fun <A, B> Map<A, B>.unzip(): Pair<List<A>, List<B>> {
    val keys = this.keys.toList()
    val values = this.values.toList()
    return keys to values
}
