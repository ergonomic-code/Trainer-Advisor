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
        val decoding = Decoding(
            lowerBound = 0,
            upperBound = 0,
            result = "",
            questionnaireId = questionnaireId
        )
        val inDbDecoding = decodingRepo.save(decoding)
        return DecodingDto(
            inDbDecoding.id,
            inDbDecoding.lowerBound,
            inDbDecoding.upperBound,
            inDbDecoding.result,
            inDbDecoding.questionnaireId
        )
    }

    override fun deleteById(id: Long) {
        decodingRepo.deleteById(id)
    }

    override fun saveDecoding(decoding: DecodingDto): DecodingDto {
        val savedDecoding = decodingRepo.save(
            Decoding(
                decoding.id,
                decoding.lowerBound,
                decoding.upperBound,
                decoding.result,
                decoding.questionnaireId
            )
        )
        return DecodingDto(
            savedDecoding.id,
            savedDecoding.lowerBound,
            savedDecoding.upperBound,
            savedDecoding.result,
            savedDecoding.questionnaireId
        )
    }

    override fun saveDecodingList(decodingList: List<DecodingDto>): List<DecodingDto> {
        return decodingRepo.saveAll(decodingList.map {
            Decoding(
                it.id,
                it.lowerBound,
                it.upperBound,
                it.result,
                it.questionnaireId
            )
        }).map { DecodingDto(it.id, it.lowerBound, it.upperBound, it.result, it.questionnaireId) }
    }

    override fun findDecodingByQuestionnaireId(questionnaireId: Long): List<DecodingDto> {
        val decodingList = decodingRepo.findAllByQuestionnaireIdOrderById(questionnaireId)
        return decodingList.map { DecodingDto(it.id, it.lowerBound, it.upperBound, it.result, it.questionnaireId) }
    }
}
