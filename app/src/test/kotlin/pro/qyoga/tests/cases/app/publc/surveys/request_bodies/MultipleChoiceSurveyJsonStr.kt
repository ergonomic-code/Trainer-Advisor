package pro.qyoga.tests.cases.app.publc.surveys.request_bodies

import org.intellij.lang.annotations.Language
import pro.qyoga.tests.fixture.data.faker

@Language("JSON")
fun multipleChoiceSurveyJsonStr(
    yandexAdminEmail: String,
    phoneNumber: String,
    question: String,
    answers: List<String>
) =
    """
{
  "yandexAdminEmail": "$yandexAdminEmail",
  "surveyName": "Поле с типом один вариант",
  "survey": {
    "id": 1980111802,
    "survey_id": "67ad69c1d046882d1e250ecf",
    "created": "2025-02-13T03:48:36Z",
    "lang": "ru",
    "answer": {
      "id": 1980111802,
      "data": {
        "phoneNumber": {
          "value": "$phoneNumber",
          "question": {
            "id": 69944855,
            "slug": "phoneNumber",
            "options": {
              "required": true
            },
            "answer_type": {
              "id": 38,
              "slug": "answer_phone"
            }
          }
        },
        "question": {
          "value": [
            ${answers.joinToString { value(it) }}
          ],
          "question": {
            "id": 69945831,
            "slug": "question",
            "options": {
              "required": true,
              "multiple": true
            },
            "answer_type": {
              "id": 3,
              "slug": "answer_choices"
            }
          }
        }
    },
    "survey": {
      "id": "67a325dde010db097c1208dc"
    },
    "created": "2025-02-09T04:02:42Z"
    }
  },
  "questionNames": {
    "Телефон для связи": "$phoneNumber",
    "$question": "${answers.joinToString()}"
  }
}"""

fun value(answer: String, key: String = faker.random().nextInt(100000000, 200000000).toString()) = """
    {
      "key": "$key",
      "slug": "$key",
      "text": "$answer"
    }
""".trimIndent()