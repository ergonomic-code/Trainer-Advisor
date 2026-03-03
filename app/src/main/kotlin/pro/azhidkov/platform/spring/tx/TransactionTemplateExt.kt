package pro.azhidkov.platform.spring.tx

import org.springframework.transaction.support.TransactionTemplate

interface TransactionalService {

    val transactionTemplate: TransactionTemplate

    fun <T : Any?> transaction(body: () -> T): T {
        return transactionTemplate.execute {
            body()
        }
    }

}
