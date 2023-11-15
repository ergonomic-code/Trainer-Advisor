package pro.qyoga.infra.files.internal

import org.springframework.stereotype.Service
import pro.qyoga.infra.files.api.File
import pro.qyoga.infra.files.api.FilesService

@Service
class FilesServiceImpl(
    private val filesRepo: FilesRepo
) : FilesService {

    override fun uploadFile(file: File): Long {
        return filesRepo.save(file).id
    }

}