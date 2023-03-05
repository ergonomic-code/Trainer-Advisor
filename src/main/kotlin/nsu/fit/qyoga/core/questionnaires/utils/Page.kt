package nsu.fit.qyoga.core.questionnaires.utils

import java.lang.Math.ceil

data class Page(
    val pageNum: Int = 1,
    val pageSize: Int = 10,
    val totalElements: Long = 0,
    val orderType: OrderType = OrderType.ASK
) {
    val totalPages: Long = (totalElements+pageSize-1)/pageSize
}
