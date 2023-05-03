package nsu.fit.qyoga.core.images.api.error

import nsu.fit.platform.errors.ResourceNotFound

open class ImageException(message: String) : ResourceNotFound(message)
