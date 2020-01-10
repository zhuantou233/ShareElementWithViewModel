/*
 * Copyright (C) 2016 The Android Open Source Project
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

package us.zoom.shareelementwithviewmodel.ui.grid;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.atomic.AtomicBoolean;

import us.zoom.shareelementwithviewmodel.data.model.Photo;
import us.zoom.shareelementwithviewmodel.databinding.PhotoItemBinding;
import us.zoom.shareelementwithviewmodel.ui.listener.OnGlideRequestListener;

public class PhotoAdapter extends ListAdapter<Photo, PhotoAdapter.PhotoViewHolder> {

    private static final PhotoDiffCallback diffCallback = new PhotoDiffCallback();

    private OnListItemClickListener onListItemClickListener;
    private OnLoadCompletedListener onLoadCompletedListener;

    public PhotoAdapter(OnListItemClickListener onListItemClickListener, OnLoadCompletedListener onLoadCompletedListener) {
        super(diffCallback);
        this.onListItemClickListener = onListItemClickListener;
        this.onLoadCompletedListener = onLoadCompletedListener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new PhotoViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.getContext())), onLoadCompletedListener);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        Photo data = getItem(position);
        holder.bind(data);
        holder.itemView.setOnClickListener(view -> onListItemClickListener.onItemClick(view, position));
    }

    public interface OnListItemClickListener {
        void onItemClick(View view, int adapterPosition);
    }

    public static class PhotoDiffCallback extends DiffUtil.ItemCallback<Photo> {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.id == newItem.id;
        }
    }

    public interface OnLoadCompletedListener {
        void onLoadCompleted(int adapterPosition);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements OnGlideRequestListener {

        private final PhotoItemBinding binding;
        private int adapterPosition;
        private AtomicBoolean enterTransitionStarted;
        private OnLoadCompletedListener onLoadCompletedListener;

        PhotoViewHolder(PhotoItemBinding binding, OnLoadCompletedListener onLoadCompletedListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onLoadCompletedListener = onLoadCompletedListener;
            this.enterTransitionStarted = new AtomicBoolean();
        }

        void bind(Photo photo) {
            adapterPosition = getAdapterPosition();
            binding.setData(photo);
            binding.setListener(this);
            binding.executePendingBindings();
        }

        public PhotoItemBinding getBinding() {
            return binding;
        }

        @Override
        public void onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            onLoadCompletedListener.onLoadCompleted(adapterPosition);
        }

        @Override
        public void onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            onLoadCompletedListener.onLoadCompleted(adapterPosition);
        }
    }
}
