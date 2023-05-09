package nsu.fit.qyoga.infra

import nsu.fit.qyoga.infra.db.DbInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(
    classes = [DbInitializer::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
)
@ActiveProfiles("test")
class QYogaModuleBaseTest {

    @Autowired
    lateinit var dbInitializer: DbInitializer

}
