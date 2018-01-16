package com.pubnub.example.android.datastream.pubnubdatastreams.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Convenience methods for getting timestamps.
 */
public class DateTimeUtil {
//    public static String getTimeStampUtc() {
//        return DateTime.now(DateTimeZone.UTC).toString(ISODateTimeFormat.dateTime().withZoneUTC());
//    }



   public static String getTimeStampUtc() {
       DateTime dateTime = new DateTime().withZone(DateTimeZone.UTC);
       final Date currentTime = dateTime.toDate();
       final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS z");
       sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
      return sdf.format(currentTime);
    }


//    public static String getTimeStampUtc() {
//        String BASE_FORMAT = "dd MM yyyy HH:mm:ss.SSS z";
//        SimpleDateFormat formatUTC = new SimpleDateFormat( BASE_FORMAT );
//        formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
//        DateTime dateTime = new DateTime().withZone(DateTimeZone.UTC);
//        Date javaDate=null;
//        try {
//            javaDate=formatUTC.parse(dateTime.toString(BASE_FORMAT));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
////        return formatUTC.format(javaDate);
//        return formatUTC.format(javaDate);
//    }


    public static String getTimeStampUtc(long instant) {
        return ISODateTimeFormat.dateTime().withZoneUTC().print(instant);
    }
}
