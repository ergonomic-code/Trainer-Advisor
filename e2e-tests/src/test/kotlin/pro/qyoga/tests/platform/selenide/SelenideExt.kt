package pro.qyoga.tests.platform.selenide

import com.codeborne.selenide.Condition.attribute
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.executeJavaScript
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.TypeOptions
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.HtmlPageCompat
import pro.qyoga.tests.platform.html.Select
import java.time.Duration


fun SelenideElement.fastType(value: String?) {
    if (value == null) {
        return
    }
    this.type(TypeOptions.text(value).withDelay(Duration.ofMillis(10)))
}

fun typeInto(component: Component, value: String?) {
    if (value == null) {
        return
    }

    val el = find(component)
    el.type(TypeOptions.text(value).withDelay(Duration.ofMillis(1)))
}

fun selectIn(component: Select, option: Enum<*>?) {
    if (option == null) {
        return
    }

    val el = find(component)
    el.options.first { it.value == option.name }
        .click()
}

fun selectIn(comboBox: ComboBox, option: String?) {
    if (option == null) {
        return
    }

    val inputEl = find(comboBox.titleInput)
    inputEl.fastType(option)
    find(comboBox).`$`(ComboBox.itemsSelector).click()
}

fun find(component: Component): SelenideElement =
    `$`(component.selector())

fun open(page: HtmlPageCompat) {
    Selenide.open(page.path)
    if (page.title != null) {
        await(page)
    } else {
        `$`("#sidebarToggle").shouldBe(visible)
    }
}

fun click(component: Component) {
    `$`(component.selector())
        .scrollToCenter()
        .click()
}

fun SelenideElement.click(selector: String) {
    `$`(selector)
        .scrollToCenter()
        .click()
}

private fun SelenideElement.scrollToCenter(): SelenideElement {
    executeJavaScript<Any>(
        "arguments[0].scrollIntoView({behavior: 'instant', block: 'center', inline: 'center'})",
        this
    )
    return this
}

fun await(page: HtmlPageCompat) {
    requireNotNull(page.title) { "Невозможно дождаться страницы без названия" }
    `$`("title").shouldHave(attribute("text", page.title!!))
}

@Suppress("unused", "Идея глючит - функция активно используется")
fun `$`(component: Component): SelenideElement =
    `$`(component.selector())
