package pro.rudloff.hangupsdroid;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;

/** Class used to display avatars (and other images). */
public class AvatarLoader implements ImageLoader {

    /**
     * Load an image into a view.
     *
     * @param imageView View to load the image into.
     * @param url URL of the image
     */
    public void loadImage(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
