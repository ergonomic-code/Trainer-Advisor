package pro.qyoga.infra.files.internal

import pro.qyoga.infra.files.api.File
import pro.qyoga.infra.files.api.FilesService

class FilesServiceImpl(
    private val filesRepo: FilesRepo
) : FilesService {

    override fun uploadFile(file: File): Long {
        return filesRepo.save(file).id
    }

}