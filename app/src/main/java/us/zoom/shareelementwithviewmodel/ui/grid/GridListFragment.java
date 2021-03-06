package us.zoom.shareelementwithviewmodel.ui.grid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import us.zoom.shareelementwithviewmodel.ImageListViewModel;
import us.zoom.shareelementwithviewmodel.R;
import us.zoom.shareelementwithviewmodel.databinding.FragmentListBinding;

public class GridListFragment extends Fragment {
    private static final String TAG = GridListFragment.class.getSimpleName();

    private ImageListViewModel viewModel;
    private FragmentListBinding binding;
    private int currentPosition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(requireActivity()).get(ImageListViewModel.class);
        viewModel.getCurrentPosition().observe(this, position -> currentPosition = position);
        binding = FragmentListBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.imageGrid.setAdapter(new GridPhotoAdapter(this::onListItemClick, this::onImageLoadCompleted));
        prepareTransitions();
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 其他生命周期会触发不显示的结果
        postponeEnterTransition();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollToPosition();
    }

    private void onImageLoadCompleted(int position) {
        // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
        if (currentPosition != position) {
            return;
        }
        startPostponedEnterTransition();
    }

    private void onListItemClick(View view, int position) {
        // Update the position.
        viewModel.setCurrentPosition(position);

        // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
        // instead of fading out with the rest to prevent an overlapping animation of fade and move).
        assert getExitTransition() != null;
        ((TransitionSet) getExitTransition()).excludeTarget(view, true);

        ImageView transitioningView = view.findViewById(R.id.photo);
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(transitioningView, transitioningView.getTransitionName())
                .build();
        Navigation.findNavController(view).navigate(R.id.action_listFragment_to_detailPagerFragment, null, null, extras);
    }

    private void scrollToPosition() {
        binding.imageGrid.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left,
                                       int top,
                                       int right,
                                       int bottom,
                                       int oldLeft,
                                       int oldTop,
                                       int oldRight,
                                       int oldBottom) {
                binding.imageGrid.removeOnLayoutChangeListener(this);
                final RecyclerView.LayoutManager layoutManager = binding.imageGrid.getLayoutManager();
                assert layoutManager != null;
                View viewAtPosition = layoutManager.findViewByPosition(currentPosition);
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)) {
                    Log.i(TAG, "onLayoutChange: " + viewModel.getCurrentPosition().getValue());
                    binding.imageGrid.post(() -> layoutManager.scrollToPosition(currentPosition));
                }
            }
        });
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) binding.imageGrid.getLayoutManager();
        assert gridLayoutManager != null;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position % 6) {
                    case 5:
                        return 3;
                    case 3:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        binding.imageGrid.addItemDecoration(new GridMarginDecoration(
                getResources().getDimensionPixelSize(R.dimen.grid_item_spacing)));
        binding.imageGrid.setHasFixedSize(true);
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private void prepareTransitions() {
        setExitTransition(TransitionInflater.from(getContext())
                .inflateTransition(R.transition.grid_exit));

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the ViewHolder for the clicked position.
                        GridPhotoAdapter.PhotoViewHolder selectedViewHolder = (GridPhotoAdapter.PhotoViewHolder) binding.imageGrid
                                .findViewHolderForAdapterPosition(currentPosition);
                        if (selectedViewHolder == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements
                                .put(names.get(0), selectedViewHolder.getBinding().photo);
                    }
                });
    }
}
