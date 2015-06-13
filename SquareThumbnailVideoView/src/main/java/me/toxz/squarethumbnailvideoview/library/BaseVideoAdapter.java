package me.toxz.squarethumbnailvideoview.library;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * Created by yyz on 6/13/15.
 */
public abstract class BaseVideoAdapter implements VideoAdapter {
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean setThumbnailImage(@NonNull ImageView thumbnailImageView, @Nullable Bitmap bitmap) {
        return false;
    }
}
