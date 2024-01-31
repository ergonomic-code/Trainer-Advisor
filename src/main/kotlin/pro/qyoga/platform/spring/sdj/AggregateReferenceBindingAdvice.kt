package pro.qyoga.platform.spring.sdj

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.InitBinder
import java.beans.PropertyEditorSupport


@ControllerAdvice("pro.qyoga.app")
class AggregateReferenceBindingAdvice {

    @InitBinder
    fun initBinders(binder: WebDataBinder) {
        binder.registerCustomEditor(AggregateReference::class.java, object : PropertyEditorSupport() {
            override fun setAsText(text: String?) {
                this.value = text?.takeIf { it.isNotBlank() }
                    ?.let { AggregateReference.to<Any, Any>(it) }
            }
        })
    }

}