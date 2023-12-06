package pro.qyoga.core.formats

import java.time.format.DateTimeFormatter

const val russianDateFormatPattern = "dd.MM.yyyy"
val russianDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(russianDateFormatPattern)
