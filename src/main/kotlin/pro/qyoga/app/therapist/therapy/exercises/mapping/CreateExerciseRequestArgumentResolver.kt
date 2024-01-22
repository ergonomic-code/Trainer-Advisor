package pro.qyoga.app.therapist.therapy.exercises.mapping

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import pro.qyoga.core.therapy.exercises.dtos.CreateExerciseRequest
import pro.qyoga.core.therapy.exercises.dtos.ExerciseSummaryDto
import pro.qyoga.core.therapy.exercises.model.ExerciseStep
import pro.qyoga.core.therapy.exercises.model.ExerciseType
import pro.qyoga.platform.java.time.toDurationMinutes


@Component
class CreateExerciseRequestArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.parameterType == CreateExerciseRequest::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val steps = webRequest.parameterNames.asSequence()
            .filter { it.startsWith("steps") }
            .sortedBy { "steps\\[(\\d+)].*".toRegex().matchEntire(it)!!.groupValues[1].toInt() }
            .map { ExerciseStep(webRequest.getParameter(it)!!, null) }
            .toList()

        val summary = ExerciseSummaryDto(
            webRequest.getParameter("title")!!,
            webRequest.getParameter("description")!!,
            webRequest.getParameter("duration")!!.toDouble().toDurationMinutes(),
            ExerciseType.valueOf(webRequest.getParameter("type")!!)
        )

        return CreateExerciseRequest(summary, steps)
    }

}