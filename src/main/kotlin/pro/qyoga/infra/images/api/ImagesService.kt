package pro.qyoga.infra.images.api


interface ImagesService {

    fun uploadImage(image: Image): Long

}
