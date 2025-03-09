package com.transaction.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The CustomDateTime utility class provides methods to get the current date and time
 * in a specific time zone.
 */
public class CustomDateTime {

    /**
     * Returns the current date and time in the "Asia/Kolkata" time zone.
     *
     * @return the current LocalDateTime in the "Asia/Kolkata" time zone
     */
    public static LocalDateTime getTimeSpecificZone(){
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        return zonedDateTime.toLocalDateTime();
    }
}
