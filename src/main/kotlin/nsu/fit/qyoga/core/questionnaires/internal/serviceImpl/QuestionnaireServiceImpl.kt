package nsu.fit.qyoga.core.questionnaires.internal.serviceImpl

import nsu.fit.qyoga.core.images.api.model.Image
import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.errors.QuestionnaireException
import nsu.fit.qyoga.core.questionnaires.api.model.Answer
import nsu.fit.qyoga.core.questionnaires.api.model.Decoding
import nsu.fit.qyoga.core.questionnaires.api.model.Question
import nsu.fit.qyoga.core.questionnaires.api.model.Questionnaire
import nsu.fit.qyoga.core.questionnaires.api.services.QuestionnaireService
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireJdbcTemplateRepo
import nsu.fit.qyoga.core.questionnaires.internal.repository.QuestionnaireRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream

@Service
class QuestionnaireServiceImpl(
    private val questionnaireRepo: QuestionnaireRepo,
    private val questionnaireJdbcTemplateRepo: QuestionnaireJdbcTemplateRepo
) : QuestionnaireService {

    override fun findQuestionnaires(
        questionnaireSearchDto: QuestionnaireSearchDto,
        pageable: Pageable
    ): Page<QuestionnaireDto> {
        return questionnaireJdbcTemplateRepo.findQuestionnaireOnPage(
            title = questionnaireSearchDto.title ?: "",
            pageable = pageable
        )
    }

    override fun saveQuestionnaire(createQuestionnaireDto: CreateQuestionnaireDto): Long {
        return questionnaireRepo.save(
            Questionnaire(
                title = createQuestionnaireDto.title,
                question = createQuestionnaireDto.question.map { question -> questionDtoToQuestion(question) }.toSet(),
                decoding = createQuestionnaireDto.decoding.map { decoding -> decodingDtoToDecoding(decoding) }.toSet()
            )
        ).id
    }

    override fun findQuestionnaireWithQuestions(id: Long): CreateQuestionnaireDto {
        val questionnaire = questionnaireRepo.findById(id).orElse(null)
            ?: throw QuestionnaireException("Выбранный опросник не найден")
        val questionnaireDto = CreateQuestionnaireDto(
            id = questionnaire.id,
            title = questionnaire.title,
            question = questionnaire.question.mapIndexed { index, question ->
                questionToQuestionDto(question, index.toLong())
            }.toMutableList(),
            decoding = questionnaire.decoding.mapIndexed { index, decoding ->
                decodingToDecodingDto(decoding, index.toLong())
            }.toMutableList()
        )
        return questionnaireDto
    }

    fun questionDtoToQuestion(questionDto: CreateQuestionDto): Question {
        return Question(
            title = questionDto.title,
            questionType = questionDto.questionType,
            image = imageDtoToImage(questionDto.image),
            answers = questionDto.answers.map { answer ->
                Answer(
                    title = answer.title,
                    lowerBound = answer.bounds.lowerBound,
                    lowerBoundText = answer.bounds.lowerBoundText,
                    upperBound = answer.bounds.upperBound,
                    upperBoundText = answer.bounds.upperBoundText,
                    score = answer.score,
                    image = imageDtoToImage(answer.image)
                )
            }.toMutableSet()
        )
    }

    fun questionToQuestionDto(question: Question, questionIndex: Long): CreateQuestionDto {
        return CreateQuestionDto(
            questionIndex,
            question.title,
            question.questionType,
            imageToImageDto(question.image),
            question.answers.mapIndexed { answerIndex, answer ->
                CreateAnswerDto(
                    answerIndex.toLong(),
                    answer.title,
                    AnswerBoundsDto(
                        lowerBound = answer.lowerBound,
                        lowerBoundText = answer.lowerBoundText,
                        upperBound = answer.upperBound,
                        upperBoundText = answer.upperBoundText,
                    ),
                    answer.score,
                    imageToImageDto(answer.image)
                )
            }.toMutableList()
        )
    }

    fun imageToImageDto(image: Image?): ImageDto? {
        image ?: return null
        return ImageDto(
            id = image.id,
            name = image.name,
            mediaType = image.mediaType,
            size = image.size,
            data = image.data
        )
    }

    fun imageDtoToImage(imageDto: ImageDto?): Image? {
        imageDto ?: return null
        return Image(
            id = imageDto.id,
            name = imageDto.name,
            mediaType = imageDto.mediaType,
            size = imageDto.size,
            data = imageDto.data
        )
    }

    fun decodingDtoToDecoding(decodingDto: DecodingDto): Decoding {
        return Decoding(
            lowerBound = decodingDto.lowerBound,
            upperBound = decodingDto.upperBound,
            result = decodingDto.result
        )
    }

    fun decodingToDecodingDto(decoding: Decoding, index: Long): DecodingDto {
        return DecodingDto(
            index,
            decoding.lowerBound,
            decoding.upperBound,
            decoding.result
        )
    }
}
