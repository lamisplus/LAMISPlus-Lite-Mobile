package org.lamisplus.datafi.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static long getAgeFromBirthdate(int year, int month, int day) {
        ZoneId timeZoneId = ZoneId.of("Africa/Lagos"); // assuming you're in Poland - put the your time zone name here ;-)
        LocalDate today = LocalDate.now(timeZoneId);
        LocalDate birthday = LocalDate.of(year, month, day);
        Period lifePeriod = Period.between(birthday, today);
        long age = lifePeriod.getYears();
        return age;
    }

    /**
     * Pass a date string to this function to give the age
     *
     * @param date String in the format yyyy-MM-dd
     * @return
     */
    public static int getAgeFromBirthdateString(String date) {
        String[] splitString = date.split("-");
        int year = Integer.parseInt(splitString[0]);
        int month = Integer.parseInt(splitString[1]);
        int day = Integer.parseInt(splitString[2]);

        ZoneId timeZoneId = null; // assuming you're in Poland - put the your time zone name here ;-)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            timeZoneId = ZoneId.of("Africa/Lagos");
            LocalDate today = LocalDate.now(timeZoneId);
            LocalDate birthday = LocalDate.of(year, month, day);
            Period lifePeriod = Period.between(birthday, today);
            int age = lifePeriod.getYears();
            return age;
        }
        return 0;
    }

    public static String getAgeFromBirthdate(int age) {
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
