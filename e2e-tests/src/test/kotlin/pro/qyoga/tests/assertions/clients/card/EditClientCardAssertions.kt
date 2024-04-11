package pro.qyoga.tests.assertions.clients.card

import pro.qyoga.tests.assertions.shouldBeEmptyInput
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.platform.html.InputBase
import pro.qyoga.tests.platform.selenide.find

fun CreateClientForm.shouldBeEmpty() {
    components
        .filterIsInstance<InputBase>()
        .forEach { component ->
            find(component).shouldBeEmptyInput()
        }
}
