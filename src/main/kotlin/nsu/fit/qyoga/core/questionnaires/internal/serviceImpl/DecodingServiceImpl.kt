package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.questionnaires.api.dtos.DecodingDto
import nsu.fit.qyoga.core.questionnaires.api.model.Decoding
import nsu.fit.qyoga.core.questionnaires.api.services.DecodingService
import nsu.fit.qyoga.core.questionnaires.internal.repository.DecodingRepo
import org.springframework.stereotype.Service

@Service
class DecodingServiceImpl(
    private val decodingRepo: DecodingRepo
): DecodingService {
    override fun createNewDecoding(questionnaireId: Long): DecodingDto {
        val savedDecoding = decodingRepo.save(
            Decoding(
                lowerBound = 0,
                upperBound = 0,
                result = "",
                questionnaireId = questionnaireId
            )
        )
        return decodingToDecodingDto(savedDecoding)
    }

    override fun deleteDecodingById(id: Long) {
        decodingRepo.deleteById(id)
    }

    override fun saveDecoding(decoding: DecodingDto): DecodingDto {
        val savedDecoding = decodingRepo.save(decodingDtoToDecoding(decoding))
        return decodingToDecodingDto(savedDecoding)
    }

    override fun saveDecodingList(decodingList: List<DecodingDto>): List<DecodingDto> {
        return decodingRepo.saveAll(decodingList.map { decodingDtoToDecoding(it) }).map { decodingToDecodingDto(it) }
    }

    override fun findDecodingByQuestionnaireId(questionnaireId: Long): List<DecodingDto> {
        val decodingList = decodingRepo.findAllByQuestionnaireIdOrderById(questionnaireId)
        return decodingList.map { decodingToDecodingDto(it) }
    }

    fun decodingToDecodingDto(decoding: Decoding): DecodingDto {
        return DecodingDto(
            decoding.id,
            decoding.lowerBound,
            decoding.upperBound,
            decoding.result,
            decoding.questionnaireId
        )
    }

    fun decodingDtoToDecoding(decodingDto: DecodingDto): Decoding {
        return Decoding(
            decodingDto.id,
            decodingDto.lowerBound,
            decodingDto.upperBound,
            decodingDto.result,
            decodingDto.questionnaireId
        )
    }
}
