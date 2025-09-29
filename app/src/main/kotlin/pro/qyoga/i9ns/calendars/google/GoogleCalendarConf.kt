package pro.qyoga.i9ns.calendars.google

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@ComponentScan
@Configuration
@EnableCaching
class GoogleCalendarConf {

    object CacheNames {
        const val CALENDAR_EVENTS = "calendarEvents"
        const val GOOGLE_ACCOUNT_CALENDARS = "googleAccountCalendars"
    }

    @Bean
    fun cacheManager(): CacheManager {
        val eventsCache = CaffeineCache(
            CacheNames.CALENDAR_EVENTS,
            Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(5_000)
                .build()
        )

        val calendarsCache = CaffeineCache(
            CacheNames.GOOGLE_ACCOUNT_CALENDARS,
            Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1_000)
                .build()
        )

        return SimpleCacheManager().apply {
            setCaches(listOf(eventsCache, calendarsCache))
        }
    }

}
