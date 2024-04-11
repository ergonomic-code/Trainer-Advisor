package pro.qyoga.tests.scripts

import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.pages.publc.LoginPage
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.platform.selenide.open
import pro.qyoga.tests.platform.selenide.typeInto

fun loginAsTheTherapist() {
    open(LoginPage)

    typeInto(LoginPage.LoginForm.username, THE_THERAPIST_LOGIN)
    typeInto(LoginPage.LoginForm.password, THE_THERAPIST_PASSWORD)
    click(LoginPage.LoginForm.submit)
}
