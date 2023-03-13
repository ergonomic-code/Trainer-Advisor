package nsu.fit.qyoga.core.exercises.internal

import nsu.fit.qyoga.core.exercises.api.model.Image
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface ImagesRepo : CrudRepository<Image, Long>, PagingAndSortingRepository<Image, Long>
