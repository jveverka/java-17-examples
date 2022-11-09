package one.microproject.timezone.examples;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class TimeZoneUtils {

    private static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTERS = new ConcurrentHashMap<>();

    public static TimeZone getDefault() {
        return TimeZone.getDefault();
    }

    public static ZonedDateTime convertToUtc(String timeStampPattern, String zoneId, String timestamp) {
        final ZonedDateTime timeStampWithAppZone = parseZonedDateTime(timestamp, timeStampPattern, getZoneId(zoneId));
        return timeStampWithAppZone.withZoneSameInstant(getZoneId("UTC"));
    }

    public static ZoneId getZoneId(String timeZone) {
        return TimeZone.getTimeZone(timeZone).toZoneId();
    }

    public static ZonedDateTime parseZonedDateTime(String date, String pattern, ZoneId zoneId) {
        return ZonedDateTime.parse(date, getFormatter(pattern, zoneId));
    }

    public static DateTimeFormatter getFormatter(String pattern, ZoneId zoneId) {
        final String key = pattern + zoneId;
        DATE_TIME_FORMATTERS.putIfAbsent(key, createFormatter(pattern, zoneId));
        return DATE_TIME_FORMATTERS.get(key);
    }

    public static DateTimeFormatter createFormatter(String pattern, ZoneId zoneId) {
        return new DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern(pattern)
                .toFormatter()
                .withZone(zoneId)
                .withResolverStyle(ResolverStyle.STRICT);
    }

}
