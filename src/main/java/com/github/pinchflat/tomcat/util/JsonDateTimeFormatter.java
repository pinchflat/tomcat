package com.github.pinchflat.tomcat.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JsonDateTimeFormatter {
    private final DateTimeFormatter ISO_8601_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .withZone(ZoneId.systemDefault());

    public String format(final long dateTimeInMilis){
        return ISO_8601_FORMAT.format(Instant.ofEpochMilli(dateTimeInMilis));
    }
}
