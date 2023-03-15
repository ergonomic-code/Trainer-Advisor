package nsu.fit.qyoga.core.questionnaires.api.dtos

import org.springframework.web.multipart.MultipartFile

data class CreateAnswerDto(
    val title: String?,
    var lowerBound: Int?,
    var lowerBoundText: String?,
    var upperBound: Int?,
    var upperBoundText: String?,
    var score: Int?,
    val photo: MultipartFile?
)
