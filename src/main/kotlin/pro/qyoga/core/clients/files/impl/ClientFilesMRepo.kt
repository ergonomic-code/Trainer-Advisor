package pro.qyoga.core.clients.files.impl

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.files.model.ClientFile


@Repository
interface ClientFilesMRepo : CrudRepository<ClientFile, Long>