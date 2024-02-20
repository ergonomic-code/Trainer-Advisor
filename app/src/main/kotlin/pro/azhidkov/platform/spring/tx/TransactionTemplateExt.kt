package pro.azhidkov.platform.spring.tx

import org.springframework.transaction.support.TransactionTemplate

interface TransactionalService {

    val transactionTemplate: TransactionTemplate

    fun <T : Any?> transaction(body: () -> T): T {
        val res = transactionTemplate.execute {
            body()
        }
        @Suppress("UNCHECKED_CAST")
        return res as T
    }

}