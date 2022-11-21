package org.lamisplus.datafi.utilities;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class DateUtils {

    public static long getAgeFromBirthdate(int year, int month, int day){
        ZoneId timeZoneId = ZoneId.of("Africa/Lagos"); // assuming you're in Poland - put the your time zone name here ;-)
        LocalDate today = LocalDate.now(timeZoneId);
        LocalDate birthday = LocalDate.of(year, month, day);
        Period lifePeriod = Period.between(birthday, today);
        long age = lifePeriod.getYears();
        return age;
    }
}
