package pro.qyoga.tests.cases.platform.ui

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import pro.azhidkov.platform.ui.PeriodFormatter
import java.time.Period


private data class Params(
    val monthsCount: Int,
    val expectedLabel: String
)

@DisplayName("PeriodFormatter")
class PeriodFormatterTest : FreeSpec({

    "должен форматировать период из" - {
        withData(
            mapOf(
                "1 месяца в 'месяца'" to Params(1, "месяца"),
                "2 месяцев в '2 месяцев'" to Params(2, "2 месяцев"),
                "3 месяцев в '3 месяцев'" to Params(3, "3 месяцев"),
                "4 месяцев в '4 месяцев'" to Params(4, "4 месяцев"),
                "5 месяцев в '5 месяцев'" to Params(5, "5 месяцев"),
                "11 месяцев в '11 месяцев'" to Params(11, "11 месяцев"),
                "14 месяцев в 'года'" to Params(14, "года"),
                "15 месяцев в 'года'" to Params(15, "года"),
                "25 месяцев в '2 лет'" to Params(25, "2 лет"),
            )
        ) { (months, expectedLabel) ->
            PeriodFormatter.formatPeriodInGenitiveCase(Period.ofMonths(months)) shouldBe expectedLabel
        }
    }

})