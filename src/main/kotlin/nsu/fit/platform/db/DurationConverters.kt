package nsu.fit.platform.db

import nsu.fit.platform.postgresql.toDuration
import org.postgresql.util.PGInterval
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.time.Duration

@WritingConverter
class DurationToPGIntervalConverter : Converter<Duration, PGInterval> {

    override fun convert(source: Duration): PGInterval {
        return PGInterval(source.toString())
    }

}

@ReadingConverter
class PGIntervalToDurationConverter : Converter<PGInterval, Duration> {

    override fun convert(source: PGInterval): Duration {
        return source.toDuration()
    }

}
