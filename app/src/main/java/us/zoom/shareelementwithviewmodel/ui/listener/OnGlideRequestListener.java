package us.zoom.shareelementwithviewmodel.ui.listener;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Tao Zhou on 2020/1/10
 * Package name: us.zoom.shareelementwithviewmodel.ui
 */
public interface OnGlideRequestListener {
    void onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource);

    void onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource);
}
