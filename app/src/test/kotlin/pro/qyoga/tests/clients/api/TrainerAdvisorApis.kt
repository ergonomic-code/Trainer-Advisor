package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import pro.qyoga.tests.infra.web.mainWebTestClient


object TrainerAdvisorApis {

    object WebPushes {

        val publicApi = WebPushesPublicApi(mainWebTestClient)

        fun therapistApi(
            principal: Cookie,
        ) = WebPushesTherapistApi(principal, mainWebTestClient)

    }

}
