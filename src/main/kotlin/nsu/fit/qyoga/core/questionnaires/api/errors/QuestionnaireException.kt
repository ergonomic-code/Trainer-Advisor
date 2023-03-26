package nsu.fit.qyoga.core.questionnaires.api.errors

import nsu.fit.platform.errors.ResourceNotFound

open class QuestionnaireException(message: String) : ResourceNotFound(message)