package dev.keii.goldenage.utils;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateUtility {
    public static @Nullable LocalDateTime epochSecondsToDateTime(Integer epochTimeSeconds) {
        if(epochTimeSeconds == null)
        {
            return null;
        }

        Instant instant = Instant.ofEpochMilli(epochTimeSeconds);
        return instant.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDateTime();
    }
}
