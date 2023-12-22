package pro.qyoga.app.therapist.clients.journal.create_entry

import pro.qyoga.platform.errors.DomainError

class ClientNotFound(clientId: Long) : DomainError("Client with id $clientId not found")