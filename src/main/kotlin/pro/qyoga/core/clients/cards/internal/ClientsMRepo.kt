package pro.qyoga.core.clients.cards.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.core.clients.cards.api.Client


@Repository
interface ClientsMRepo : CrudRepository<Client, Long>