package my.com.sains.teams.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 20/2/2017.
 */

public class DateTime {

    private String dateTime;

    public String getCurrentDateTime(String dateFormat){

        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        String dateStr = fmt.format(Calendar.getInstance().getTime());

        Log.e("date", dateStr);
        return dateStr;
    }

    public Date convertDate(String dateFormat, String timeDate){

        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        Date date = new Date();
        try {

            date = fmt.parse(timeDate);

        }catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getStringDateTime(String dateFormat, Date date){

        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return fmt.format(date);
    }

}
