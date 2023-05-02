package nsu.fit.qyoga.core.images.internal

import nsu.fit.qyoga.core.images.api.model.Image
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepo : CrudRepository<Image, Long>
