package pro.qyoga.core.users.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.users.api.Therapist


@Repository
interface TherapistMRepo : CrudRepository<Therapist, Long>