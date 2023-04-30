package nsu.fit.qyoga.core.questionnaires.api.errors

import nsu.fit.platform.errors.ResourceNotFound

open class DecodingException(message: String) : ResourceNotFound(message)
