package pro.rudloff.hangupsdroid;

import android.app.Activity;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.DateFormatter.Formatter;
import java.util.Date;

public class MessageDateFormatter implements Formatter {

    private Activity activity;

    public MessageDateFormatter(Activity newActivity) {
        activity = newActivity;
    }

    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return activity.getString(R.string.yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }
}
