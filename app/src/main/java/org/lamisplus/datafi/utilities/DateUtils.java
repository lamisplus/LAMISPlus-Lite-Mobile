package org.lamisplus.datafi.utilities;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.lamisplus.datafi.application.LamisCustomFileHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {

//    public static long getAgeFromBirthdate(int year, int month, int day) {
//        ZoneId timeZoneId = ZoneId.of("Africa/Lagos"); // assuming you're in Poland - put the your time zone name here ;-)
//        LocalDate today = LocalDate.now(timeZoneId);
//        LocalDate birthday = LocalDate.of(year, month, day);
//        Period lifePeriod = Period.between(birthday, today);
//        long age = lifePeriod.getYears();
//        return age;
//    }

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

    public static Integer gestationAge(String lmp, String dateEnrollment) {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale(Locale.US);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH

            LocalDate lmpDate = LocalDate.parse(lmp, formatter);
            LocalDate dateEnroll = LocalDate.parse(dateEnrollment, formatter);

            int ga = (int) ChronoUnit.WEEKS.between(lmpDate, dateEnroll);
            if (ga < 0) ga = 0;
            return ga;
        }
        return 0;
    }

    public static Integer testInfantAge(String dateOfBirth, String dateAtTest) {
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = formatter.withLocale(Locale.US);  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH

            LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
            LocalDate testDate = LocalDate.parse(dateAtTest, formatter);

            int td = (int) ChronoUnit.MONTHS.between(birthDate, testDate);
            if (td < 0) td = 0;
            return td;
        }
        return 0;
    }


    //   public int calculateGA2(String hospitalNumber, LocalDate visitDate) {
//        Calendar a = new GregorianCalendar();
//        Calendar b = new GregorianCalendar();
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date start = dateFormat.parse(lmp);
//            Date end = dateFormat.parse(dateEnrollment);
//            assert start != null;
//            a.setTime(start);
//            assert end != null;
//            b.setTime(end);
//            Integer value = b.get(Calendar.WEEK_OF_YEAR) - a.get(Calendar.WEEK_OF_YEAR);
//            if(value < 0){
//                Log.v("Baron", " values " + value + " <=> " + b.get(Calendar.WEEK_OF_YEAR) + " " + a.get(Calendar.WEEK_OF_YEAR));
//                return b.get(Calendar.WEEK_OF_YEAR);
//            }
//            return value;
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
    //  }

    public static Integer gestationAge2(String lmp, String dateEnrollment) {
        Calendar a = new GregorianCalendar();
        Calendar b = new GregorianCalendar();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(lmp);
            Date end = dateFormat.parse(dateEnrollment);
            a.setTime(start);
            b.setTime(end);
            return b.get(Calendar.WEEK_OF_YEAR) - a.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String currentDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
        }
        return "";
    }

    public static Boolean compareDate(String dateParam) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String currentDate = dtf.format(now);

                SimpleDateFormat dateFormatCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateCurrrent = dateFormatCurrent.parse(currentDate);

                Calendar a = new GregorianCalendar();
                Calendar b = new GregorianCalendar();


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateCaptured = dateFormat.parse(dateParam);

                a.setTime(dateCaptured);
                b.setTime(dateCurrrent);

                boolean sameDay = a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR) &&
                        a.get(Calendar.YEAR) == b.get(Calendar.YEAR);
                Log.v("Baron", "Data is " + sameDay);
                return sameDay;
            }catch (Exception e){

            }
        }
        return false;
    }
}
