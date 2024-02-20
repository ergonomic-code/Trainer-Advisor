package pro.qyoga.app.publc.components

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.components.combobox.ComboBoxController
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.app.platform.components.combobox.ComboBoxModelAndView


@Controller
@RequestMapping(TimeZonesComboBoxController.PATH)
class TimeZonesComboBoxController(
    private val timeZones: TimeZones
) : ComboBoxController<Any?> {

    @GetMapping
    override fun search(
        @RequestParam("timeZoneTitle") searchKey: String?,
        @RequestParam("timeZoneId") currentValue: String?,
        userDetails: Any?
    ): ComboBoxModelAndView {
        val searchResult = timeZones.search(currentValue, searchKey, limit = 5)
            .map { it.toComboBoxItem() }

        return ComboBoxModelAndView(searchResult)
    }

    companion object {
        const val PATH = "/components/time-zone/autocomplete-search"
    }

}

fun LocalizedTimeZone.toComboBoxItem() = ComboBoxItem(this.zone, "${this.displayName} (${this.zone.id})")