package pro.qyoga.tests.cases.app.publc.surveys.request_bodies

import org.intellij.lang.annotations.Language

@Language("JSON")
fun complaintsAndCustomQuestionSurveyJsonStr(
    yandexAdminEmail: String,
    phone: String,
    complaints: String,
    customQuestion: String,
    customAnswer: String
) = """
{
  "survey": {
    "id": 1976441439,
    "survey_id": "67a325dde010db097c1208dc",
    "created": "2025-02-09T04:02:42Z",
    "lang": "ru",
    "answer": {
      "id": 1976441439,
      "data": {
        "lastName": {
          "value": "Романов",
          "question": {
            "id": 69944398,
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
        "firstName": {
          "value": "Терек",
          "question": {
            "id": 69944599,
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
        "phoneNumber": {
          "value": "$phone",
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
       "complaints": {
          "value": "$complaints",
          "question": {
            "id": 69945787,
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
        "$customQuestion": {
          "value": "$customAnswer",
          "question": {
            "id": 69945830,
            "slug": "$customQuestion",
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
        "id": "67a325dde010db097c1208dc"
      },
      "created": "2025-02-09T04:02:42Z"
    }
  },
  "yandexAdminEmail": "$yandexAdminEmail",
  "surveyName": "Входная анкета",
  "questionNames": {
    "Фамилия": "Романов",
    "Имя": "Терек",
    "Телефон для связи": "+7 999 241-50-33",
    "$customQuestion": "$customAnswer"
  }
}"""