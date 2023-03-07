package nsu.fit.qyoga.core.questionnaires.utils


data class Page(
    val pageNum: Int = 1,
    val totalElements: Long = 0,
    val orderType: OrderType = OrderType.ASK
) {
    val pageSize: Int = 10
    val totalPages: Long = (totalElements+pageSize-1)/pageSize

}
