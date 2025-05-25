package pro.qyoga.tests.pages.therapist.therapy.programs

import pro.qyoga.app.therapist.therapy.therapeutic_tasks.components.SearchTherapeuticTasksController
import pro.qyoga.tests.platform.html.Button
import pro.qyoga.tests.platform.html.FormAction
import pro.qyoga.tests.platform.html.Input
import pro.qyoga.tests.platform.html.QYogaForm

abstract class ProgramForm(action: FormAction) : QYogaForm("programForm", action) {

    val titleInput by component { Input.text("title", true) }

    val therapeuticTaskInput by component { Input.text(SearchTherapeuticTasksController.SEARCH_KEY_PARAM_NAME, true) }

    val exerciseSearchInput by component { Input.text("searchKey", false, id = "exerciseSearchInput") }

    val save by component { Button("save", "Сохранить") }

    val exerciseIdsInputName = "exerciseIds"

    val notExistingTherapeuticTaskMessage =
        "div.invalid-feedback:contains(Терапевтической задачи с таким названием не существует)"

    val removeButtonSelector = ".remove-button"

}

object CreateProgramForm : ProgramForm(FormAction.hxPost("/therapist/programs/create"))

object EditProgramForm : ProgramForm(FormAction.hxPut("/therapist/programs/{programId}"))