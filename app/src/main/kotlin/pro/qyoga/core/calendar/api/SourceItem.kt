package pro.qyoga.core.calendar.api

data class SourceItem(
    val type: String,
    val id: String
) {

    constructor(eventId: CalendarItemId) : this(eventId.type.name, eventId.toQueryParamStr())

}
