package dev.keii.goldenage.utils;

import dev.keii.goldenage.GoldenAge;

import javax.annotation.Nullable;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class DateUtility {
    public static @Nullable LocalDateTime epochSecondsToDateTime(Integer epochTimeSeconds) {
        if (epochTimeSeconds == null) {
            return null;
        }

        GoldenAge.getLogger().info(epochTimeSeconds.toString());
        GoldenAge.getLogger().info(String.valueOf((long) epochTimeSeconds * 1000L));
        Instant instant = Instant.ofEpochMilli((long) epochTimeSeconds * 1000L);

        return instant.atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toLocalDateTime();
    }


    public static String getHumanReadableTimeSpan(long epochSeconds) {
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
            parts.add(weeks + (weeks > 1 ? " weeks" : " week"));
        }
        if (days > 0) {
            parts.add(days + (days > 1 ? " days" : " day"));
        }
        if (hours > 0) {
            parts.add(hours + (hours > 1 ? " hours" : " hour"));
        }
        if (minutes > 0 && weeks == 0) {
            parts.add(minutes + (minutes > 1 ? " minutes" : " minute"));
        }
        if (remainingSeconds > 0 && days == 0 && hours == 0 && weeks == 0) {
            parts.add(remainingSeconds + (remainingSeconds > 1 ? " seconds" : " second"));
        }

        if (parts.isEmpty()) {
            return "now";
        }

        return String.join(" ", parts.subList(0, Math.min(3, parts.size())));
    }

}
