package nsu.fit.qyoga.core.exercises.utils.pages

import kotlin.math.ceil

data class Page<T>(
    val content: List<T> = emptyList(),
    val pageNum: Int = 0,
    val pageSize: Int = 0,
    val totalElements: Long = 0,
) {
    val totalPages: Int = ceil(totalElements.toDouble() / pageSize).toInt()
}
