package one.microproject.timezone.examples.tests;

import one.microproject.timezone.examples.TimeZoneUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TimeZoneUtilTests {

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-01-01T12:00:00.000Z", "2022-01-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-02-01T12:00:00.000Z", "2022-02-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-03-01T12:00:00.000Z", "2022-03-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-04-01T12:00:00.000Z", "2022-04-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-05-01T12:00:00.000Z", "2022-05-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-06-01T12:00:00.000Z", "2022-06-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-07-01T12:00:00.000Z", "2022-07-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-08-01T12:00:00.000Z", "2022-08-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-09-01T12:00:00.000Z", "2022-09-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-10-01T12:00:00.000Z", "2022-10-01T10:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-11-01T12:00:00.000Z", "2022-11-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "CET", "2022-12-01T12:00:00.000Z", "2022-12-01T11:00Z[UTC]"),

                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-01-01T12:00:00.000Z", "2022-01-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-02-01T12:00:00.000Z", "2022-02-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-03-01T12:00:00.000Z", "2022-03-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-04-01T12:00:00.000Z", "2022-04-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-05-01T12:00:00.000Z", "2022-05-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-06-01T12:00:00.000Z", "2022-06-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-07-01T12:00:00.000Z", "2022-07-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-08-01T12:00:00.000Z", "2022-08-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-09-01T12:00:00.000Z", "2022-09-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-10-01T12:00:00.000Z", "2022-10-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-11-01T12:00:00.000Z", "2022-11-01T11:00Z[UTC]"),
                Arguments.of("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'", "GMT+01", "2022-12-01T12:00:00.000Z", "2022-12-01T11:00Z[UTC]")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForIsBlank")
    public void testTimeZones(String timeStampPattern, String zoneId, String timestamp, String convertedTimestamp) {
        ZonedDateTime zonedDateTime = TimeZoneUtils.convertToUtc(timeStampPattern, zoneId, timestamp);
        assertNotNull(zonedDateTime);
        assertEquals(convertedTimestamp, zonedDateTime.toString());
    }

}
