package us.zoom.shareelementwithviewmodel;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import us.zoom.shareelementwithviewmodel.data.model.Photo;
import us.zoom.shareelementwithviewmodel.ui.ImageSize;
import us.zoom.shareelementwithviewmodel.ui.listener.OnGlideRequestListener;
import us.zoom.shareelementwithviewmodel.ui.grid.PhotoAdapter;

/**
 * Created by Tao Zhou on 2020/1/8
 * Package name: us.zoom.shareelementwithviewmodel
 */
public class BindingAdapters {

    @BindingAdapter("listData")
    public static void bindRecyclerView(RecyclerView recyclerView, List<Photo> data) {
        PhotoAdapter adapter = (PhotoAdapter) recyclerView.getAdapter();
        assert adapter != null;
        adapter.submitList(data);
    }

    @BindingAdapter("loadingStatus")
    public static void bindStatus(ProgressBar progressBar, Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @BindingAdapter(value = {"imageUrl", "glideListener"}, requireAll = true)
    public static void bindImage(ImageView imageView, Photo photo, OnGlideRequestListener listener) {
        int requestedPhotoWidth = imageView.getContext().getResources().getDisplayMetrics().widthPixels;
        Glide.with(imageView.getContext())
                .load(photo.getPhotoUrl(requestedPhotoWidth))
                .placeholder(R.color.placeholder)
                .override(ImageSize.NORMAL[0], ImageSize.NORMAL[1])
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        listener.onLoadFailed(e, model, target, isFirstResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        listener.onResourceReady(resource, model, target, dataSource, isFirstResource);
                        return false;
                    }
                })
                .into(imageView);
    }
}
