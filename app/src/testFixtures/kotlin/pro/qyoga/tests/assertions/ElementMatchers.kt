package pro.qyoga.tests.assertions

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import org.jsoup.nodes.Element
import org.springframework.web.util.UriTemplate
import pro.qyoga.tests.platform.html.Component
import pro.qyoga.tests.platform.html.HtmlPage
import pro.qyoga.tests.platform.html.Input


fun beComponent(component: Component) = Matcher<Element> { element ->
    val matchingElement = element.select(component.selector()).first()

    if (matchingElement != element) {
        val failureMessage =
            "Element ${element.descr} does not matches component $component's selector `${component.selector()}`"
        val negatedFailureMessage =
            "Element ${element.descr} should not matche component $component's selector `${component.selector()}`"

        MatcherResult(
            false,
            { failureMessage },
            { negatedFailureMessage }
        )
    } else {
        component.matcher().test(element)
    }
}

infix fun Element.shouldBeComponent(component: Component): Element {
    this should beComponent(component)
    return this
}

fun haveComponent(component: Component) = Matcher<Element> { element ->
    val components = element.select(component.selector())
    val componentsCount = components.size

    if (componentsCount != 1) {
        val failureMessage =
            "Element ${element.descr} has $componentsCount components `${component.selector()}` but single component expected"
        val negatedFailureMessage =
            "Element ${element.descr} has component `${component.selector()}` but no one expected"

        MatcherResult(
            false,
            { failureMessage },
            { negatedFailureMessage }
        )
    } else {
        component.matcher().test(components[0])
    }
}

fun haveComponents(componentTemplate: Component, count: Int) = Matcher<Element> { element ->
    val components = element.select(componentTemplate.selector())
    val componentsCount = components.size

    if (componentsCount != count) {
        val failureMessage =
            "Element ${element.descr} has $componentsCount components `${componentTemplate.selector()}` but $count components expected"
        val negatedFailureMessage =
            "Element ${element.descr} has component `${componentTemplate.selector()}` but no one expected"

        MatcherResult(
            false,
            { failureMessage },
            { negatedFailureMessage }
        )
    } else {
        val results = components.map { componentTemplate.matcher().test(it) }
        MatcherResult(
            results.all { it.passed() },
            { results.filterNot { it.passed() }.joinToString(separator = "\n") { it.failureMessage() } },
            { results.filter { it.passed() }.joinToString(separator = "\n") { it.negatedFailureMessage() } },
        )
    }
}

fun isTag(tag: String) = Matcher { element: Element ->
    MatcherResult.invoke(
        element.tag().name.equals(tag, ignoreCase = true),
        { "Element ${element.descr} is a ${element.tag().name}-tag but $tag-tag expected" },
        { "Element ${element.descr} should not be ${element.tag().name}-tag" },
    )
}

fun haveAttribute(attr: String) = Matcher { element: Element ->
    MatcherResult.invoke(
        element.hasAttr(attr),
        { "Element ${element.descr} has no expected attribute `$attr`" },
        { "Element ${element.descr} should not have attribute `$attr`" },
    )
}

fun haveAttributeValueMatching(attr: String, valueRegex: Regex) = Matcher { element: Element ->
    MatcherResult.invoke(
        element.attr(attr).matches(valueRegex),
        { "Element ${element.descr} has $attr=\"${element.attr(attr)}\" but `$attr` value matching \"$valueRegex\" is expected" },
        { "Element ${element.descr} should not have attribute `$attr` value matching \"$valueRegex\"" },
    )
}

fun haveAttributeValueMatching(attr: String, uriTemplate: UriTemplate) = Matcher { element: Element ->
    MatcherResult.invoke(
        uriTemplate.matches(element.attr(attr)),
        { "Element ${element.descr} has $attr=\"${element.attr(attr)}\" but `$attr` value matching \"${uriTemplate}}\" is expected" },
        { "Element ${element.descr} should not have attribute `$attr` value matching \"${uriTemplate}}\"" },
    )
}

fun haveAttributeValue(attr: String, value: String, ignoreCase: Boolean = false) = Matcher { element: Element ->
    MatcherResult.invoke(
        element.attr(attr).equals(value, ignoreCase = ignoreCase),
        { "Element ${element.descr} has $attr=\"${element.attr(attr)}\" but `$attr` value equal to \"$value\" ignoring is expected" },
        { "Element ${element.descr} should not have attribute `$attr` value equal to $\"$value\" igonring case" },
    )
}

fun haveClass(className: String) = Matcher { element: Element ->
    MatcherResult(
        className in element.classNames(),
        { "Element ${element.descr} do not have class $className in ${element.classNames()} but should" },
        { "Element ${element.descr} have class $className in ${element.classNames()} but not should" }
    )
}

infix fun Element.shouldHaveClass(className: String): Element {
    this should haveClass(className)
    return this
}

fun haveText(text: String) = Matcher { element: Element ->
    MatcherResult(
        element.text() == text,
        { "Element ${element.descr} has text [${element.text()}] but [$text] is expected`" },
        { "Element ${element.descr} should not have text [${element.text()}]" }
    )
}

fun haveTitle(title: String): Matcher<Element> =
    Matcher.all(
        haveComponent(SelectorOnlyComponent("title")),
        Matcher { element: Element ->
            val titleElement = element.getElementsByTag("title").single()
            MatcherResult(
                titleElement.text() == title,
                { "Element ${titleElement.descr} have title [${titleElement.text()}] but [$title] expected" },
                { "Element ${titleElement.descr} should not have title [${titleElement.text()}]" },
            )
        }
    )

infix fun Element.shouldHaveTitle(title: String): Element {
    this should haveTitle(title)
    return this
}

fun haveInputWithValue(input: Input, value: String) = Matcher { element: Element ->
    val isComponent = haveComponent(input).test(element)
    if (!isComponent.passed()) {
        return@Matcher isComponent
    }
    val inputElement = element.select(input.selector()).single()
    val actualValue = input.value(inputElement)

    MatcherResult(
        actualValue == value,
        { "Input ${inputElement.descr} have value `$actualValue` but `$value` expected" },
        { "Input ${inputElement.descr} should not have value `$value`" }
    )
}

fun haveCheckboxChecked(
    selector: String = "input.form-check-input[type=checkbox][name=shouldBeShown]",
    expected: Boolean
): Matcher<Element> = Matcher { el ->
    val checkbox = el.select(selector).first()
    val actual = checkbox?.hasAttr("checked") == true
    MatcherResult(
        checkbox != null && actual == expected,
        {
            val found = if (checkbox == null) "not found" else if (actual) "checked" else "unchecked"
            "Expected checkbox '$selector' to be ${if (expected) "checked" else "unchecked"}, but $found"
        },
        { "Checkbox '$selector' unexpectedly matches expected=${expected}" }
    )
}

val Element.descr
    get() = "<${this.tag().name} id=\"${this.id()}\">" +
            this.text().take(32) +
            ("...".takeIf { this.text().length > 32 } ?: "") +
            "</${this.tag().name}>"

infix fun Element.shouldHaveComponent(component: Component): Element {
    this should haveComponent(component)
    return this
}

fun htmlMatch(regex: Regex) = Matcher { element: Element ->
    MatcherResult(
        element.html().matches(regex),
        { "Element ${element.descr} html does not matches regex $regex" },
        { "Element ${element.descr} html should not match regex $regex" }
    )
}

infix fun Element.shouldNotHave(component: Component): Element {
    this shouldNot haveComponent(component)
    return this
}

infix fun Element.shouldNotHave(selector: String) {
    this shouldNot haveComponent(SelectorOnlyComponent(selector))
}

fun bePage(page: HtmlPage) = Matcher.all(
    Matcher { element: Element ->
        MatcherResult(element.root() == element,
            { "Element ${element.descr} should be root element but it isn't" },
            { "Element ${element.descr} should not be root element" }
        )
    },
    page.matcher
)

infix fun Element.shouldBePage(page: HtmlPage): Element {
    this should bePage(page)
    return this
}

fun alwaysSuccess(): Matcher<Element> = Matcher { _ ->
    MatcherResult(true, { "" }, { "" })
}

fun haveComponent(selector: String) = haveComponent(SelectorOnlyComponent(selector))

fun haveElements(selector: String, count: Int): Matcher<Element> = Matcher { element ->
    val elements = element.select(selector)
    val actualCount = elements.size
    MatcherResult(
        actualCount == count,
        {
            "Element ${element.descr} have ${actualCount} elements matching $selector but $count is expected.\nElements: \n${
                elements.joinToString("\n") { it.descr }
            }"
        },
        {
            "Element ${element.descr} should not have ${actualCount} elements matching $selector.\nElements: \n${
                elements.joinToString("\n") { it.descr }
            }"
        }
    )

}

fun haveAtLeastElements(componentTemplate: Component, count: Int): Matcher<Element> = Matcher { element ->
    val elements = element.select(componentTemplate.selector())
    val actualMatchingSelectorCount = elements.size
    val actualMatchingComponentCount = elements.count { componentTemplate.matcher().test(it).passed() }
    MatcherResult(
        actualMatchingComponentCount >= count,
        {
            "Element ${element.descr} have $actualMatchingSelectorCount elements matching selector ${componentTemplate.selector()} and $actualMatchingComponentCount of them matching component $componentTemplate but at least $count is expected.\nElements: \n${
                elements.joinToString("\n") { it.descr }
            }"
        },
        {
            "Element ${element.descr} have $actualMatchingSelectorCount elements matching selector ${componentTemplate.selector()} and $actualMatchingComponentCount of them matching component $componentTemplate but at most $count is expected.\nElements: \n${
                elements.joinToString("\n") { it.descr }
            }"
        }
    )

}

infix fun Element.shouldHaveElement(selector: String): Element {
    this should haveElement(selector)
    return this
}

fun Element.shouldHaveElements(selector: String, count: Int): Element {
    this should haveElements(selector, count)
    return this
}

@Deprecated("Use shouldHaveElements")
infix fun Element.shouldHave(selector: String) {
    this should haveElement(selector)
}

fun haveElement(selector: String) = haveElements(selector, 1)

data class SelectorOnlyComponent(val selector: String) : Component {

    override fun selector(): String = selector

    override fun matcher(): Matcher<Element> = alwaysSuccess()

}
