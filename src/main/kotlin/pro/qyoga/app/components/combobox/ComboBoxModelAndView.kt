package pro.qyoga.app.components.combobox

import org.springframework.web.servlet.ModelAndView


class ComboBoxModelAndView(searchResult: Iterable<ComboBoxItem>) :
    ModelAndView("components/combo-box.html :: comboBoxSearchResult") {

    init {
        addObject("searchResult", searchResult)
    }

}