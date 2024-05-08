package pro.qyoga.tests.infra.green_mail

import com.icegreen.greenmail.configuration.GreenMailConfiguration
import com.icegreen.greenmail.junit5.GreenMailExtension
import com.icegreen.greenmail.util.ServerSetupTest


val greenMail: GreenMailExtension = GreenMailExtension(ServerSetupTest.SMTP.port(10_025))
    .withConfiguration(GreenMailConfiguration.aConfig().withUser("qyogapro@yandex.ru", "password"))