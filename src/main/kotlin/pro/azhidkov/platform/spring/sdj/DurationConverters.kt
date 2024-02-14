package pro.azhidkov.platform.spring.sdj

import org.postgresql.util.PGInterval
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import pro.azhidkov.platform.postgresql.toDuration
import pro.azhidkov.platform.postgresql.toPGInterval
import java.time.Duration

@WritingConverter
class DurationToPGIntervalConverter : Converter<Duration, PGInterval> {

    override fun convert(source: Duration): PGInterval {
        return source.toPGInterval()
    }

}

@ReadingConverter
class PGIntervalToDurationConverter : Converter<PGInterval, Duration> {

    override fun convert(source: PGInterval): Duration {
        return source.toDuration()
    }

}
