package pro.qyoga.app.platform.components.combobox

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestParam


interface ComboBoxController<UD> {

    fun search(
        @RequestParam searchKey: String?,
        @RequestParam currentValue: String?,
        @AuthenticationPrincipal userDetails: UD
    ): ComboBoxModelAndView

}