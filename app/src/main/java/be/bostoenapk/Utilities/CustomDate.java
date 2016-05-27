package be.bostoenapk.Utilities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomDate extends Date {
    private Date date;
    private SimpleDateFormat formatter;
    private String format = "yyyy-MM-dd'-'HH:mm:ss";

    /**
     *
     */
    public CustomDate() {
        date = new Date();
        formatter = new SimpleDateFormat(format);
    }


    /**
     * @param date
     */
    public CustomDate(long date) {
        this.date = new Date(date);
        formatter = new SimpleDateFormat(format);
    }


    /**
     * @param date
     */
    public CustomDate(Date date) {
        this.date = date;
        formatter = new SimpleDateFormat(format);
    }

    /**
     * @param date
     *
     * @throws ParseException wordt opgegooid wanneer de String in een verkeerd formaat wordt opgegeven
     */
    public CustomDate(String date) throws ParseException {
        formatter = new SimpleDateFormat(format);
        this.date = formatter.parse(date);
    }

    public String toString() {
        Log.d("original date", date.toString());
        return formatter.format(date);
    }
}