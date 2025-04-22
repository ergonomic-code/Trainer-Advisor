package pro.azhidkov.platform.ui

import java.time.Period


object PeriodFormatter {

    fun formatPeriodInGenitiveCase(period: Period): String {
        val normalizedPeriod = period.normalized()
        return when {
            normalizedPeriod.years > 0 -> RussianPeriodDeclensions.YEAR.formatInGenitiveCase(normalizedPeriod.years)
            normalizedPeriod.months > 0 -> RussianPeriodDeclensions.MONTH.formatInGenitiveCase(normalizedPeriod.months)
            normalizedPeriod.days >= 0 -> RussianPeriodDeclensions.DAY.formatInGenitiveCase(normalizedPeriod.days)
            else -> error("Must never happen")
        }
    }

}

private enum class RussianPeriodDeclensions(
    private val singular: String,
    private val few: String,
    private val many: String
) {

    YEAR("года", "лет", "лет"),
    MONTH("месяца", "месяцев", "месяцев"),
    DAY("дня", "дней", "дней");

    fun formatInGenitiveCase(count: Int): String {
        val mod100 = count % 100
        val mod10 = count % 10

        return when {
            count == 1 -> singular
            mod100 in 11..14 -> "$count $many"
            mod10 in 2..4 -> "$count $few"
            else -> "$count $many"
        }
    }

}