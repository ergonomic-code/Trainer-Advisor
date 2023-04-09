package nsu.fit.qyoga.app.questionnaires

import nsu.fit.qyoga.core.questionnaires.api.dtos.*
import nsu.fit.qyoga.core.questionnaires.api.services.*
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping("/questionnaires/")
class QuestionnairesController(
    private val questionnaireService: QuestionnaireService,
    private val questionService: QuestionService,
    private val answerService: AnswerService,
    private val imageService: ImageService,
    private val decodingService: DecodingService
) {

    /**
     * Получение списка опросников
     */
    @GetMapping()
    fun getQuestionnairesList(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list"
    }

    /**
     * Фильтрация опросников
     */
    @GetMapping("action")
    fun sortQuestionnaires(
        @ModelAttribute("questionnaireSearchDto") questionnaireSearchDto: QuestionnaireSearchDto,
        @PageableDefault(value = 10, page = 0, sort = ["title"]) pageable: Pageable,
        model: Model
    ): String {
        val questionnaires = questionnaireService.findQuestionnaires(
            questionnaireSearchDto,
            pageable
        )
        addQuestionnairePageAttributes(model, questionnaireSearchDto, questionnaires)
        return "questionnaire/questionnaire-list :: page-content"
    }

    /**
     * Создание нового опросника
     */
    @GetMapping("new")
    fun getCreateQuestionnairePage(
        model: Model
    ): String {
        val questionnaireId = questionnaireService.createQuestionnaire()
        val question = questionService.createQuestion(questionnaireId)
        answerService.createAnswer(question.id)
        return "redirect:/questionnaires/${questionnaireId}/edit"
    }

    /**
     * Редактирование опросника
     */
    @GetMapping("{id}/edit")
    fun editQuestionnaire(
        model: Model,
        @PathVariable id: Long
    ): String {
        val questionnaire = questionnaireService.findQuestionnaireWithQuestions(id)
        model.addAttribute("questionnaire", questionnaire)
        return "questionnaire/create-questionnaire"
    }

    /**
     * Добавление нового вопроса
     */
    @GetMapping("{id}/edit/add-question")
    fun addNewQuestionToQuestionnaire(
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.createQuestion(id)
        question.answers += answerService.createAnswer(question.id)
        model.addAttribute("questionnaire", questionnaireService.findQuestionnaireWithQuestions(id))
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Создание опросника
     */
    @PostMapping("{id}/edit")
    fun createQuestionnaire(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable id: Long
    ): String {
        questionnaireService.updateQuestionnaire(questionnaire)
        return "redirect:/questionnaires/$id/setResult"
    }

    /**
     * Получение изображение по id
     */
    @GetMapping("image/{id}")
    @ResponseBody
    fun loadImageToPage(
        @PathVariable id: Long
    ): ResponseEntity<InputStreamResource> {
        val image = imageService.getImage(id)
        return ResponseEntity.ok()
            .contentLength(image.size)
            .contentType(MediaType.parseMediaType(image.mediaType))
            .body(InputStreamResource(image.data.inputStream()))
    }

    /**
     * Добавление изображение вопросу
     */
    @PostMapping("question/{id}/image")
    fun addImageToQuestion(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("questionIndex") questionIndex: Int,
        @PathVariable id: Long,
        model: Model
    ): String {
        val question = questionService.findQuestion(id)
        if( question.imageId != null) {
            imageService.deleteImage(question.imageId)
        }
        val questionDto = QuestionDto(
            id = question.id,
            title = question.title,
            questionType = question.questionType,
            questionnaireId = question.questionnaireId,
            imageId = imageService.uploadImage(file)
        )
        questionService.updateQuestion(questionDto)
        model.addAttribute("question", questionDto)
        model.addAttribute("questionIndex", questionIndex)
        return "fragments/create-questionnaire-image::questionImage"
    }

    /**
     * Добавление изображение ответу
     */
    @PostMapping("answer/{id}/image")
    fun addImageToAnswer(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("questionIndex") questionIndex: Int,
        @RequestParam("answerIndex") answerIndex: Int,
        @PathVariable id: Long,
        model: Model
    ): String {
        val answer = answerService.findAnswer(id)
        answer.imageId?.let {
            imageService.deleteImage(it)
        }
        val answerDto = AnswerDto(
            id = answer.id,
            title = answer.title,
            lowerBound = answer.lowerBound,
            lowerBoundText = answer.lowerBoundText,
            upperBound = answer.upperBound,
            upperBoundText = answer.upperBoundText,
            score = answer.score,
            imageId = imageService.uploadImage(file),
            questionId = answer.questionId
        )
        answerService.updateAnswer(answerDto)
        model.addAttribute("answer", answerDto)
        model.addAttribute("questionIndex", questionIndex)
        model.addAttribute("answerIndex", answerIndex)
        return "fragments/create-questionnaire-image::answerImage"
    }

    /**
     * Удаление изображения
     */
    @DeleteMapping("image/{id}")
    @ResponseBody
    fun deleteImageFromQuestion(
        @PathVariable id: Long
    ): ResponseEntity<String> {
        imageService.deleteImage(id)
        return ResponseEntity.ok().body("")
    }

    /**
     * Удаление вопроса из опросника
     */
    @DeleteMapping("{questionnaireId}/edit/question/{questionId}")
    fun deleteQuestionFromQuestionnaire(
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        questionService.deleteQuestion(questionId)
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Добавление ответа в вопрос
     */
    @GetMapping("{questionnaireId}/edit/question/{questionId}/addAnswer")
    fun addAnswerToQuestion(
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        answerService.createAnswer(questionId)
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * Удаление ответа из вопроса
     */
    @DeleteMapping("{questionnaireId}/edit/answer/{answerId}")
    fun deleteAnswerFromQuestion(
        @PathVariable answerId: Long,
        @PathVariable questionnaireId: Long,
        model: Model
    ): String {
        answerService.deleteAnswer(answerId)
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/create-questionnaire::questions"
    }

    @PostMapping("{id}/edit/title")
    @ResponseBody
    fun changeQuestionnaireTitle(
        questionnaire: QuestionnaireDto
    ): HttpStatus{
        questionnaireService.updateQuestionnaire(questionnaire)
        return HttpStatus.OK
    }

    /**
     * Изменить тип вопроса
     */
    @PostMapping("{questionnaireId}/edit/question/{questionId}/change-type")
    fun changeAnswersType(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        model: Model,
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long
    ): String {
        questionnaire.questions.forEach { question ->
            if (question.id == questionId){
                questionService.updateQuestion(
                    QuestionDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId
                    )
                )
            }
        }
        val deletedAnswers = answerService.deleteAllByQuestionId(questionId)
        for(answer in deletedAnswers){
            if(answer.imageId != null){
                imageService.deleteImage(answer.imageId)
            }
        }
        answerService.createAnswer(questionId)
        model.addAttribute(
            "questionnaire",
            questionnaireService.findQuestionnaireWithQuestions(questionnaireId)
        )
        return "questionnaire/create-questionnaire::questions"
    }

    /**
     * обновить вопрос
     */
    @PostMapping("{questionnaireId}/edit/question/{questionId}/update")
    @ResponseBody
    fun changeQuestionTitle(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable questionId: Long,
        @PathVariable questionnaireId: Long
    ): HttpStatus{
        questionnaire.questions.forEach { question ->
            if (question.id == questionId){
                questionService.updateQuestion(
                    QuestionDto(
                        id = question.id,
                        title = question.title,
                        questionType = question.questionType,
                        questionnaireId = question.questionnaireId,
                        imageId = question.imageId
                    )
                )
                return HttpStatus.OK
            }
        }
        return HttpStatus.BAD_REQUEST
    }

    /**
     * Обновление ответа
     */
    @PostMapping("question/{questionId}/answer/{answerId}/update")
    @ResponseBody
    fun changeAnswerTitle(
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto,
        @PathVariable answerId: Long,
        @PathVariable questionId: Long
    ): HttpStatus{
        questionnaire.questions.forEach { question ->
            if (question.id == questionId){
                question.answers.forEach { answer ->
                    if (answer.id == answerId){
                        answerService.updateAnswer(answer)
                        return HttpStatus.OK
                    }
                }
            }
        }
        return HttpStatus.BAD_REQUEST
    }

    /**
     * Получить фрагмент страницы задания баллов для ответов
     */
    @GetMapping("question/{id}/setScores")
    fun setQuestionScore(
        model : Model,
        @PathVariable id: Long,
        @ModelAttribute("questionIndex") questionIndex: Int
    ): String {
        val question = questionService.findQuestionWithAnswers(id)
        model.addAttribute("question", question)
        model.addAttribute("questionIndex", questionIndex)
        return "fragments/create-questionnaire-answer-set-score::answersScore"
    }

    /**
     * Получить фрагмент страницы редактирования ответа
     */
    @PostMapping("question/{id}/setAnswers")
    fun setQuestionAnswers(
        model : Model,
        @PathVariable id: Long,
        @ModelAttribute("questionIndex") questionIndex: Int,
        @ModelAttribute("questionnaire") questionnaire: QuestionnaireWithQuestionDto
    ): String {
        questionnaire.questions.forEach { question ->
            if (question.id == id){
                for(answer in question.answers){
                    answerService.updateAnswer(answer)
                }
                return@forEach
            }
        }
        val question = questionService.findQuestionWithAnswers(id)
        model.addAttribute("questionnaire", questionnaire)
        model.addAttribute("question", question)
        model.addAttribute("questionIndex", questionIndex)
        return "fragments/create-questionnaire-answer::question"
    }

    @GetMapping("{questionnaireId}/setResult")
    fun getSetResultPage(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        val test = DecodingDtoList(decodingService.findDecodingByQuestionnaireId(questionnaireId))
        model.addAttribute(
            "results",
            test
        )
        return "questionnaire/questionnaire-decoding"
    }

    @DeleteMapping("{questionnaireId}/setResults/{resultId}")
    fun deleteResultRow(
        model: Model,
        @PathVariable resultId: Long,
        @PathVariable questionnaireId: Long
    ): String{
        decodingService.deleteById(resultId)
        model.addAttribute(
            "results",
            DecodingDtoList(decodingService.findDecodingByQuestionnaireId(questionnaireId)))
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    @GetMapping("{questionnaireId}/setResults/addResult")
    fun addResultToQuestionnaire(
        model: Model,
        @PathVariable questionnaireId: Long
    ): String {
        decodingService.createNewDecoding(questionnaireId)
        model.addAttribute(
            "results",
            DecodingDtoList(decodingService.findDecodingByQuestionnaireId(questionnaireId)))
        return "questionnaire/questionnaire-decoding::tableDecoding"
    }

    @PostMapping("setResults/{resultId}/update")
    @ResponseBody
    fun updateResultsTableRow(
        @PathVariable resultId: Long,
        @ModelAttribute("results") results: DecodingDtoList,
    ): HttpStatus{
        results.decodingDtoList.forEach { result ->
            if (result.id == resultId){
                decodingService.saveDecoding(result)
                return HttpStatus.OK
            }
        }
        return HttpStatus.BAD_REQUEST
    }

    @PostMapping("setResults")
    fun saveResultsTable(
        @ModelAttribute("results") results: DecodingDtoList,
    ): String{
        decodingService.saveDecodingList(results.decodingDtoList)
        return "redirect:/questionnaires/"
    }


    fun addQuestionnairePageAttributes(
        model: Model,
        questionnaireSearchDto: QuestionnaireSearchDto,
        questionnaires: Page<QuestionnaireDto>
    ) {
        model.addAttribute("questionnaires", questionnaires)
        model.addAttribute("questionnaireSearchDto", questionnaireSearchDto)
        model.addAttribute(
            "sortType",
            questionnaires.sort.getOrderFor("title").toString().substringAfter(' ')
        )
    }
}