package pro.rudloff.hangupsdroid;

import android.app.Activity;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.DateFormatter.Formatter;
import java.util.Date;

/** Class used by ChatKit to format dates in the conversation list. */
public class MessageDateFormatter implements Formatter {

    /** Current activity. */
    private Activity activity;

    /**
     * MessageDateFormatter constructor.
     *
     * @param newActivity Current activity
     */
    public MessageDateFormatter(Activity newActivity) {
        activity = newActivity;
    }

    /**
     * Format the date correctly.
     *
     * @param date Date to format
     * @return Formatted date
     */
    @Override
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
