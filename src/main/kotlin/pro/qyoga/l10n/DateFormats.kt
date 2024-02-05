package pro.qyoga.l10n

import java.time.format.DateTimeFormatter

const val RUSSIAN_DAY_OF_MONTH_PATTERN = "dd.MM"

const val RUSSIAN_DATE_FORMAT_PATTERN = "dd.MM.yyyy"
val russianDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(RUSSIAN_DATE_FORMAT_PATTERN)

const val RUSSIAN_TIME_FORMAT_PATTERN = "HH:mm"
val russianTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(RUSSIAN_TIME_FORMAT_PATTERN)

const val RUSSIAN_DATE_TIME_PATTERN = "$RUSSIAN_DATE_FORMAT_PATTERN $RUSSIAN_TIME_FORMAT_PATTERN"