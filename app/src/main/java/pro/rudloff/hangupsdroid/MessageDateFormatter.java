package pro.rudloff.hangupsdroid;

import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.DateFormatter.Formatter;

import java.util.Date;

public class MessageDateFormatter implements Formatter {

    public String format(Date date) {
       if (DateFormatter.isToday(date)) {
           return DateFormatter.format(date, DateFormatter.Template.TIME);
       } else if (DateFormatter.isYesterday(date)) {
           return "Yesterday";
       } else {
           return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
       }
    }

}
