package pro.qyoga.app.therapist.clients.journal.edit_entry.shared

import pro.azhidkov.platform.errors.DomainError
import java.util.*

class ClientNotFound(clientId: UUID) : DomainError("Client with id $clientId not found")