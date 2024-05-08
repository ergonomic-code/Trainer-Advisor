package pro.azhidkov.platform.kotlin

fun <A, B> Map<A, B>.unzip(): Pair<List<A>, List<B>> {
    val keys = this.keys.toList()
    val values = this.values.toList()
    return keys to values
}

fun <T> List<T>.mapElement(idx: Int, map: (T) -> T): List<T> =
    this.mapIndexed { itemIdx, item ->
        if (itemIdx == idx) map(item)
        else item
    }
