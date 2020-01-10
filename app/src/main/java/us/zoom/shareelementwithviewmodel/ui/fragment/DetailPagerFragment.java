package us.zoom.shareelementwithviewmodel.ui.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import us.zoom.shareelementwithviewmodel.ImageListViewModel;
import us.zoom.shareelementwithviewmodel.MainActivity;
import us.zoom.shareelementwithviewmodel.R;
import us.zoom.shareelementwithviewmodel.ui.pager.DetailViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPagerFragment extends Fragment {

    private ViewPager viewPager;
    private ImageListViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.viewModel = ViewModelProviders.of(getActivity()).get(ImageListViewModel.class);
        View view = inflater.inflate(R.layout.fragment_detail_pager, container, false);
        viewPager = view.findViewById(R.id.pager);
        viewModel.getPhotos().observe(this, photos -> viewPager.setAdapter(new DetailViewPagerAdapter(
                getChildFragmentManager(), photos.size(), photos)));

        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        viewModel.getCurrentPosition().observe(this, position -> viewPager.setCurrentItem(position));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewModel.setCurrentPosition(position);
            }
        });

        prepareSharedElementTransition();

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null) {
            postponeEnterTransition();
        }

        return view;
    }

    private void prepareSharedElementTransition() {
        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.shared_main_detail);
        setSharedElementEnterTransition(transition);

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        // Locate the image view at the primary fragment (the ImageFragment that is currently
                        // visible). To locate the fragment, call instantiateItem with the selection position.
                        // At this stage, the method will simply return the fragment at the position and will
                        // not create a new one.
                        Fragment currentFragment = (Fragment) viewPager.getAdapter()
                                .instantiateItem(viewPager, viewModel.getCurrentPosition().getValue());
                        View view = currentFragment.getView();
                        if (view == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements.put(names.get(0), view.findViewById(R.id.photo));
                    }
                });
    }
}
