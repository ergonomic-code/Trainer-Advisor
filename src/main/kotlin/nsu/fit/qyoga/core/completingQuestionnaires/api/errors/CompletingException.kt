package nsu.fit.qyoga.core.completingQuestionnaires.api.errors

import nsu.fit.platform.errors.ResourceNotFound

class CompletingException(message: String) : ResourceNotFound(message)