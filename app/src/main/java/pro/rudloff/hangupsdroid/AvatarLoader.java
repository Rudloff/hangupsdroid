package pro.rudloff.hangupsdroid;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

public class AvatarLoader implements ImageLoader {
    public void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
