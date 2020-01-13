package us.zoom.shareelementwithviewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.shareelementwithviewmodel.data.Constants;
import us.zoom.shareelementwithviewmodel.data.UnsplashService;
import us.zoom.shareelementwithviewmodel.data.model.Photo;

/**
 * Created by Tao Zhou on 2020/1/8
 * Package name: us.zoom.shareelementwithviewmodel
 */
public class ImageListViewModel extends ViewModel {

    private static final int PHOTO_COUNT = 12;

    private MutableLiveData<List<Photo>> photos = new MutableLiveData<>();

    public LiveData<List<Photo>> getPhotos() {
        return photos;
    }

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private MutableLiveData<Integer> currentPosition = new MutableLiveData<>();

    public LiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        currentPosition.setValue(position);
    }

    {
        loadPhotos();
        setCurrentPosition(0);
    }

    private void loadPhotos() {
        isLoading.setValue(true);
//        UnsplashService unsplashApi = new Retrofit.Builder()
//                .baseUrl(UnsplashService.BASEURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(UnsplashService.class);
//        unsplashApi.getPhoto().enqueue(new Callback<List<Photo>>() {
//            @Override
//            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
//                List<Photo> list = response.body();
//                if (list != null && !list.isEmpty()) {
//                    photos.setValue(new ArrayList<>(list.subList(list.size() - PHOTO_COUNT,
//                            list.size())));
//                }
//                isLoading.setValue(false);
//            }
//
//            @Override
//            public void onFailure(Call<List<Photo>> call, Throwable t) {
//                Log.i("ShareElement", "UnsplashService onFailure: " + t.getMessage());
//                isLoading.setValue(false);
//            }
//        });
        List<Photo> data = new ArrayList<>();
        for (String url : Constants.getImageUrls()) {
            Photo photo = new Photo("", 0, 0, "", url.hashCode(), "", "", url);
            data.add(photo);
        }
        photos.setValue(data);
        isLoading.setValue(false);
    }

}
