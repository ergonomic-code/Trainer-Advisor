package pro.qyoga.tests.scripts

import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.pages.therapist.clients.ClientsListPage
import pro.qyoga.tests.platform.selenide.await
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.platform.selenide.open
import pro.qyoga.tests.platform.selenide.typeInto

fun loginAsTheTherapist() {
    login(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)
}

fun login(login: String, password: String) {
    open(LoginPage)

    typeInto(LoginPage.LoginForm.username, login)
    typeInto(LoginPage.LoginForm.password, password)
    click(LoginPage.LoginForm.submit)
    await(ClientsListPage)
}
