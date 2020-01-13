/*
 * Copyright (C) 2019 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.zoom.shareelementwithviewmodel.ui.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.List;

import us.zoom.shareelementwithviewmodel.data.model.Photo;

/**
 * Adapter for paging detail views.
 */

public class DetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private int size;
    private List<Photo> photos;

    DetailViewPagerAdapter(@NonNull FragmentManager fm, int size, List<Photo> photos) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.size = size;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new DetailFragment(photos.get(position));
    }
}
