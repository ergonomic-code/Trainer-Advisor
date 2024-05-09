package pro.qyoga.core.clients.files.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.journals.model.JournalEntry
import java.time.Instant


@Table("client_files")
data class ClientFile(
    val clientRef: AggregateReference<Client, Long>,
    val fileRef: AggregateReference<FileMetaData, Long>,
    val journalEntryRef: AggregateReference<JournalEntry, Long>? = null,

    @Id
    val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val modifiedAt: Instant? = null,
    @Version
    val version: Long = 0
) {


    object Fetch {
        val fileOnly = setOf(ClientFile::fileRef)
    }

}