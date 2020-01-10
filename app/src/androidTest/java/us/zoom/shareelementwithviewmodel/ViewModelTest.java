package us.zoom.shareelementwithviewmodel;

import android.util.Log;

import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.shareelementwithviewmodel.data.UnsplashService;
import us.zoom.shareelementwithviewmodel.data.model.Photo;

/**
 * Created by Tao Zhou on 2020/1/8
 * Package name: us.zoom.shareelementwithviewmodel
 */
public class ViewModelTest {

    @Test
    public void testGetPhoto() {
        UnsplashService unsplashApi = new Retrofit.Builder()
                .baseUrl(UnsplashService.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UnsplashService.class);
        unsplashApi.getPhoto().enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.body() != null) {
                    for (Photo photo : response.body()) {
                        Log.i("testGetPhoto", "onResponse: " + photo);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.i("ShareElement", "UnsplashService onFailure: " + t.getMessage());
            }
        });
    }
}
