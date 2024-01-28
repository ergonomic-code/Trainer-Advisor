package pro.qyoga.tests.assertions

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.compose.all
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot
import org.jsoup.nodes.Element
import pro.qyoga.tests.infra.html.Component
import pro.qyoga.tests.infra.html.Input

infix fun Element.shouldBeElement(pageMatcher: PageMatcher) = pageMatcher.match(this)
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

fun haveAttributeValue(attr: String, value: String, ignoreCase: Boolean = false) = Matcher { element: Element ->
    MatcherResult.invoke(
        element.attr(attr).equals(value, ignoreCase = ignoreCase),
        { "Element ${element.descr} has $attr=\"${element.attr(attr)}\" but `$attr` value equal to \"$value\" ignoring is expected" },
        { "Element ${element.descr} should not have attribute `$attr` value equal to $\"$value\" igonring case" },
    )
}

fun haveText(text: String) = Matcher { element: Element ->
    MatcherResult(
        element.text() == text,
        { "Element ${element.descr} has text [${element.text()}] but [$text] is expected`" },
        { "Element ${element.descr} should not have text [${element.text()}]" }
    )
}

fun matchSelector(selector: String) = Matcher { element: Element ->
    MatcherResult(
        element.select(selector).first() == element,
        { "Element ${element.descr} doesn't matches selector $selector" },
        { "Element ${element.descr} should not match selector $selector" },
    )
}

fun beTagWithText(tag: String, text: String) = Matcher.all(
    isTag(tag),
    haveText(text)
)

fun beTitle(text: String) = beTagWithText("title", text)
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

infix fun Element.shouldHaveTitle(title: String): Element {
    val titleElement = this.getElementsByTag("title").single()
    titleElement should beTitle(title)
    return this
}

val Element.descr
    get() = "<${this.tag().name} id=\"${this.id()}\"/>"

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

fun alwaysSuccess(): Matcher<Element> = Matcher { element ->
    MatcherResult(true, { "" }, { "" })
}

data class SelectorOnlyComponent(val selector: String) : Component {

    override fun selector(): String = selector

    override fun matcher(): Matcher<Element> = alwaysSuccess()

}