package us.zoom.shareelementwithviewmodel.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

import us.zoom.shareelementwithviewmodel.ImageListViewModel;
import us.zoom.shareelementwithviewmodel.data.model.Photo;
import us.zoom.shareelementwithviewmodel.databinding.FragmentDetailBinding;
import us.zoom.shareelementwithviewmodel.ui.listener.OnGlideRequestListener;

public class DetailFragment extends Fragment implements OnGlideRequestListener {
    private static final String TAG = DetailFragment.class.getSimpleName();

    private ImageListViewModel viewModel;
    private FragmentDetailBinding binding;
    private Photo photo;

    public DetailFragment(Photo photo) {
        // Required empty public constructor
        this.photo = photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(ImageListViewModel.class);
        }
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setListener(this);
        binding.setData(photo);

        viewModel.getCurrentPhoto().observe(this, photo -> binding.setData(photo));
        return binding.getRoot();
    }

    @Override
    public void onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        getParentFragment().startPostponedEnterTransition();
    }

    @Override
    public void onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        getParentFragment().startPostponedEnterTransition();
    }
}
