package pro.qyoga.tests.platform.selenide

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.TypeOptions
import pro.qyoga.tests.platform.html.Component
import java.time.Duration


fun SelenideElement.fastType(value: String) {
    this.type(TypeOptions.text(value).withDelay(Duration.ofMillis(10)))
}

fun `$`(component: Component): SelenideElement =
    Selenide.`$`(component.selector())
