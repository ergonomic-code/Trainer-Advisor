package pro.qyoga.app.therapist.clients.cards

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.azhidkov.platform.errors.EntityNotFoundError
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.toDto
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptor
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptorsRepo
import pro.qyoga.core.clients.therapeutic_data.descriptors.findByTherapistId
import pro.qyoga.core.clients.therapeutic_data.values.TherapeuticDataFieldValuesRepo
import pro.qyoga.core.clients.therapeutic_data.values.findByClientId


@Component
class GetClientCardWorkflow(
    private val clientsRepo: ClientsRepo,
    private val therapeuticDataDescriptorsRepo: TherapeuticDataDescriptorsRepo,
    private val therapeuticDataFieldValuesRepo: TherapeuticDataFieldValuesRepo
) : (Long, Long) -> Pair<EditClientCardForm, TherapeuticDataDescriptor> {

    override fun invoke(therapistId: Long, clientId: Long): Pair<EditClientCardForm, TherapeuticDataDescriptor> {
        val fieldValues = therapeuticDataFieldValuesRepo.findByClientId(clientId)
            .toList()
        val client = clientsRepo.findByIdOrNull(clientId)
            ?: throw EntityNotFoundError(Client::class, clientId)

        val therapeuticDataDescriptor = therapeuticDataDescriptorsRepo.findByTherapistId(therapistId)
            ?: throw EntityNotFoundError(TherapeuticDataDescriptor::class, therapistId)


        return Pair(
            EditClientCardForm(
                client.toDto(),
            ),
            therapeuticDataDescriptor
        )
    }

}

