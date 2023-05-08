package nsu.fit.qyoga.app.questionnaires

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/questionnaires/")
class QuestionnaireImageController(
    //private val imageService: ImageService
)
    /**
     * Получение изображение по id
     */
/*@GetMapping("image/{id}")
@ResponseBody
fun loImageToPage(
    @PathVariable id: Long
): ResponseEntity<InputStreamResource> {
    val image = imageService.getImage(id)
    return ResponseEntity.ok()
        .contentLength(image.size)
        .contentType(MediaType.parseMediaType(image.mediaType))
        .body(InputStreamResource(image.data.inputStream()))
}*/

/**
 * Удаление изображения
 */
/*@DeleteMapping("image/{id}")
@ResponseBody
fun deteImageFromQuestion(
@PathVariable id: Long
): ResponseEntity<String> {
imageService.deleteImage(id)
return ResponseEntity.ok().body("")
}*/
