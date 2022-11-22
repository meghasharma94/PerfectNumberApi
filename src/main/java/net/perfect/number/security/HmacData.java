package net.perfect.number.security;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Builder
@Data
public class HmacData {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
    private static final String SEPRATOR = ",";
    public static final long MAX_EXPIRY_IN_SECONDS = 6000;
    private String applicationName;
    private String httpMethod;
    private Instant expiry;

    public static String createHmacString(String applicationName, String httpMethod, Instant expiry) {
        return HmacData.builder()
                .applicationName(applicationName)
                .httpMethod(httpMethod)
                .expiry(expiry)
                .build()
                .stringDataFormat();
    }

    private String stringDataFormat() {
        return new StringBuilder(applicationName != null ? applicationName : "")
                .append(httpMethod)
                .append(expiry)
                .toString();
    }
}
