package com.zhangaochong.data_transport_demo.util;

import com.zhangaochong.data_transport_demo.enums.DataTransportTimeUnit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 11:24
 */
public abstract class DataTransportUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime minusTime(LocalDateTime fromTime, Integer time, DataTransportTimeUnit timeUnit) {
        if (fromTime == null) {
            throw new IllegalArgumentException("参数 fromTime 不正确");
        }
        if (time == null || time == 0 || timeUnit == null) {
            throw new IllegalArgumentException("参数 time 或 timeUnit 不正确");
        }

        LocalDateTime toTime;
        switch (timeUnit) {
            case HOUR:
                toTime = fromTime.minusHours(time); break;
            case DAY:
                toTime = fromTime.minusDays(time); break;
            case WEEK:
                toTime = fromTime.minusWeeks(time); break;
            case MONTH:
                toTime = fromTime.minusMonths(time); break;
            case YEAR:
                toTime = fromTime.minusYears(time); break;
            default:
                throw new IllegalArgumentException("参数 " + timeUnit + " 未知的时间单位");
        }
        return toTime;
    }

    public static LocalDateTime parseFromString(String dateTime) {
        return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
    }
}
