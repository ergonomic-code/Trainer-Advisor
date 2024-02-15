package pro.qyoga.app.platform.components.combobox

import org.springframework.web.servlet.ModelAndView


class ComboBoxModelAndView(val searchResult: Iterable<ComboBoxItem>) :
    ModelAndView("components/combo-box.html :: comboBoxSearchResult") {

    init {
        addObject("searchResult", searchResult)
    }

}