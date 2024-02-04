package pro.qyoga.l10n

import java.time.format.DateTimeFormatter

const val RUSSIAN_DATE_FORMAT_PATTERN = "dd.MM.yyyy"
val russianDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(RUSSIAN_DATE_FORMAT_PATTERN)
