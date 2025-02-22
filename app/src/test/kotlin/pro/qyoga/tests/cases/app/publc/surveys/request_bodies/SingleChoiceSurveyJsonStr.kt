package pro.qyoga.tests.cases.app.publc.surveys.request_bodies

import org.intellij.lang.annotations.Language

@Language("JSON")
fun singleChoiceSurveyJsonStr(yandexAdminEmail: String, phoneNumber: String, question: String, answer: String) =
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
            {
              "key": "140078453",
              "slug": "140078453",
              "text": "$answer"
            }
          ],
          "question": {
            "id": 69945831,
            "slug": "question",
            "options": {
              "required": true
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
    "$question": "$answer"
  }
}"""