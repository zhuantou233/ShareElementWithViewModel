package us.zoom.shareelementwithviewmodel.ui.pager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

import us.zoom.shareelementwithviewmodel.data.model.Photo;
import us.zoom.shareelementwithviewmodel.databinding.FragmentDetailBinding;
import us.zoom.shareelementwithviewmodel.ui.listener.OnGlideRequestListener;

public class DetailFragment extends Fragment implements OnGlideRequestListener {
    private static final String TAG = DetailFragment.class.getSimpleName();

    private Photo photo;

    DetailFragment(Photo photo) {
        this.photo = photo;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setListener(this);
        binding.setData(photo);

        return binding.getRoot();
    }

    @Override
    public void onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        requireParentFragment().startPostponedEnterTransition();
    }

    @Override
    public void onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        requireParentFragment().startPostponedEnterTransition();
    }
}
