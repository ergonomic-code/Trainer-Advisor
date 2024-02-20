package pro.qyoga.tests.pages.therapist.therapy.programs

import pro.qyoga.tests.platform.html.FormAction
import pro.qyoga.tests.platform.html.Input
import pro.qyoga.tests.platform.html.QYogaForm

abstract class ProgramForm(action: FormAction) : QYogaForm("programForm", action) {

    val titleInput = Input.text("title", true)

    val therapeuticTaskInput = Input.text("therapeuticTaskName", true)

    val exerciseIdsInputName = "exerciseIds"

    val notExistingTherapeuticTaskMessage =
        "div.invalid-feedback:contains(Терапевтической задачи с таким названием не существует)"

    override val components = listOf(
        titleInput,
        therapeuticTaskInput
    )

}

object CreateProgramForm : ProgramForm(FormAction.hxPost("/therapist/programs/create"))

object EditProgramForm : ProgramForm(FormAction.hxPut("/therapist/programs/{programId}"))