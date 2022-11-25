package org.lamisplus.datafi.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static long getAgeFromBirthdate(int year, int month, int day){
        ZoneId timeZoneId = ZoneId.of("Africa/Lagos"); // assuming you're in Poland - put the your time zone name here ;-)
        LocalDate today = LocalDate.now(timeZoneId);
        LocalDate birthday = LocalDate.of(year, month, day);
        Period lifePeriod = Period.between(birthday, today);
        long age = lifePeriod.getYears();
        return age;
    }

    public static String getAgeFromBirthdate(int age){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        // Perform addition/subtraction
        c.add(Calendar.YEAR, -age);
        Date currentDatePlusOne = c.getTime();
        return dateFormat.format(currentDatePlusOne);
    }
}
