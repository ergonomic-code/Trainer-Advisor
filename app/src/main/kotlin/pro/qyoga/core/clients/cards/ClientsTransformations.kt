package pro.qyoga.core.clients.cards

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.model.Client


fun Client(therapistId: Long, createRequest: ClientCardDto): Client =
    Client(
        createRequest.firstName,
        createRequest.lastName,
        createRequest.middleName,
        createRequest.birthDate,
        createRequest.phoneNumber,
        createRequest.email,
        createRequest.address,
        createRequest.complaints,
        createRequest.anamnesis,
        createRequest.distributionSource,
        AggregateReference.to(therapistId)
    )

fun Client.patchedBy(clientCardDto: ClientCardDto): Client = Client(
    firstName = clientCardDto.firstName,
    lastName = clientCardDto.lastName,
    middleName = clientCardDto.middleName,
    birthDate = clientCardDto.birthDate,
    phoneNumber = clientCardDto.phoneNumber,
    email = clientCardDto.email,
    address = clientCardDto.address,
    distributionSource = clientCardDto.distributionSource,
    complaints = clientCardDto.complaints,
    anamnesis = clientCardDto.anamnesis,
    therapistId = therapistId,

    id = id,
    version = version
)

fun Client.toDto(): ClientCardDto = ClientCardDto(
    firstName = firstName,
    lastName = lastName,
    middleName = middleName,
    birthDate = birthDate,
    phoneNumber = phoneNumber,
    email = email,
    address = address,
    distributionSourceType = distributionSource?.type,
    distributionSourceComment = distributionSource?.comment,
    complaints = complaints,
    anamnesis = anamnesis,
    version = version
)