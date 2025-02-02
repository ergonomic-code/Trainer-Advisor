package pro.qyoga.tests.cases.app.publc.surveys

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.http.HttpStatus
import pro.qyoga.tests.clients.YandexFormsClient
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseKoTest


@DisplayName("Операция создания анкеты")
class SubmitSurveyTest : QYogaAppIntegrationBaseKoTest({

    val yandexFormsClient by lazy { YandexFormsClient(client) }

    "при отправке корректного запроса новым клиентом должна" - {
        // Сетап
        val entranceSurveyJson = jacksonObjectMapper().readTree(entranceSurveyJsonStr)
        val clientPhone = entranceSurveyJson["answer"]["data"]["phone"]["value"].textValue()

        val status = yandexFormsClient.createSurvey(entranceSurveyJsonStr)

        "возвращать статус 204" {
            status shouldBe HttpStatus.NO_CONTENT
        }

        "создавать клиента с корректным телефоном и именем" {
            val client = backgrounds.clients.getAllClients().singleOrNull()

            client shouldNotBe null
        }
    }

    "при отправке запроса только с телефоном и именем клиента должна " - {

        "создавать клиента с соответствующими данными" {
            TODO()
        }

    }

})

const val entranceSurveyJsonStr = """
    {
 "id": 1940104707,
 "survey_id": "676b7c8a068ff0563b20c8a5",
 "created": "2024-12-27T12:09:58Z",
 "lang": "ru",
 "answer": {
   "id": 1940104707,
   "data": {
     "firstName": {
       "value": " Алексей",
       "question": {
         "id": 66918271,
         "slug": "firstName",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 1,
           "slug": "answer_short_text"
         }
       }
     },
     "lastName": {
       "value": "Жидков",
       "question": {
         "id": 66918649,
         "slug": "lastName",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 1,
           "slug": "answer_short_text"
         }
       }
     },
     "phone": {
       "value": "+7 1234566788",
       "question": {
         "id": 66918685,
         "slug": "phone",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 38,
           "slug": "answer_phone"
         }
       }
     },
     "sex": {
       "value": [
         {
           "key": "131201137",
           "slug": "131201137",
           "text": "Мужской"
         }
       ],
       "question": {
         "id": 66918851,
         "slug": "sex",
         "options": {
           "multiple": false,
           "ordering": "natural",
           "required": true,
           "data_source": "survey_question_choice"
         },
         "answer_type": {
           "id": 3,
           "slug": "answer_choices"
         }
       }
     },
     "birthDate": {
       "value": "1943-06-02",
       "question": {
         "id": 66918815,
         "slug": "birthDate",
         "options": {
           "required": true,
           "date_range": false
         },
         "answer_type": {
           "id": 39,
           "slug": "answer_date"
         }
       }
     },
     "city": {
       "value": [
         {
           "key": "177080",
           "slug": "110935",
           "text": "Кольцово, Новосибирская область"
         }
       ],
       "question": {
         "id": 66918872,
         "slug": "city",
         "options": {
           "multiple": true,
           "required": true,
           "data_source": "city"
         },
         "answer_type": {
           "id": 3,
           "slug": "answer_choices"
         }
       }
     },
     "height": {
       "value": "180",
       "question": {
         "id": 66918986,
         "slug": "height",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 1,
           "slug": "answer_short_text"
         }
       }
     },
     "weight": {
       "value": "87",
       "question": {
         "id": 66918993,
         "slug": "weight",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 1,
           "slug": "answer_short_text"
         }
       }
     },
     "complaints": {
       "value": "нет",
       "question": {
         "id": 66918885,
         "slug": "complaints",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 2,
           "slug": "answer_long_text"
         }
       }
     },
     "injuries": {
       "value": "нет",
       "question": {
         "id": 66918955,
         "slug": "injuries",
         "options": {
           "required": true
         },
         "answer_type": {
           "id": 2,
           "slug": "answer_long_text"
         }
       }
     }
   },
   "survey": {
     "id": "676b7c8a068ff0563b20c8a5"
   },
   "created": "2024-12-27T12:09:58Z"
 }
}
"""