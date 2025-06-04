package dev.keii.goldenage.utils;

import javax.annotation.Nullable;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class DateUtility {
    public static @Nullable LocalDateTime epochSecondsToDateTime(Integer epochTimeSeconds) {
        if (epochTimeSeconds == null) {
            return null;
        }

        Instant instant = Instant.ofEpochMilli((long) epochTimeSeconds * 1000L);

        return instant.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDateTime();
    }

    private static String getShort(String unit, boolean shortUnits) {
        if (shortUnits) {
            return unit.substring(0, 2).trim();
        } else {
            return unit;
        }
    }

    public static String getHumanReadableTimeSpan(long epochSeconds, boolean shortUnits) {
        Instant now = Instant.now();
        Instant past = Instant.ofEpochSecond(epochSeconds);
        Duration duration = Duration.between(past, now);

        long seconds = duration.getSeconds();

        if (seconds <= 0) {
            return "now";
        }

        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        long weeks = days / 7;
        days = days % 7;

        List<String> parts = new ArrayList<>();

        if (weeks > 0) {
            parts.add(weeks + getShort(weeks > 1 ? " weeks" : " week", shortUnits));
        }
        if (days > 0) {
            parts.add(days + getShort(days > 1 ? " days" : " day", shortUnits));
        }
        if (hours > 0) {
            parts.add(hours + getShort(hours > 1 ? " hours" : " hour", shortUnits));
        }
        if (minutes > 0 && weeks == 0) {
            parts.add(minutes + getShort(minutes > 1 ? " minutes" : " minute", shortUnits));
        }
        if (remainingSeconds > 0 && days == 0 && hours == 0 && weeks == 0) {
            parts.add(remainingSeconds + getShort(remainingSeconds > 1 ? " seconds" : " second", shortUnits));
        }

        if (parts.isEmpty()) {
            return "now";
        }

        return String.join(" ", parts.subList(0, Math.min(3, parts.size())));
    }

}
