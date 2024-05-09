package pro.qyoga.tests.platform.selenide

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.TypeOptions
import pro.qyoga.tests.platform.html.ComboBox
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.QYogaPage
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
    Selenide.`$`(component.selector())

fun open(page: QYogaPage) {
    Selenide.open(page.path)
}

fun click(component: Component) {
    Selenide.`$`(component.selector()).click()
}