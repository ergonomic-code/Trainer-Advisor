package pro.qyoga.platform.file_storage.internal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pro.qyoga.platform.file_storage.api.FileMetaData


@Repository
interface FilesMetaDataMRepo : CrudRepository<FileMetaData, Long>