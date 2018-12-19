package dsa.eetac.upc.edu.tracksminimo2;

import android.telecom.Call;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIRest {
    //We specify the url
    String BASE_URL = "http://147.83.7.155:8080/dsaApp/";

    //GET all tracks
    @GET("tracks")
    retrofit2.Call<List<Track>> getAllTracks();

    //Get an especific track passing its ID
    @GET("tracks/{id}")
    retrofit2.Call<Track> getTrack(@Path("id") int id);

    //Create a new track
    @POST("tracks")
    retrofit2.Call<Track> createTrack(@Body Track track);

    //Update a track
    @PUT("tracks")
    retrofit2.Call<Void> updateTrack(@Body Track track);

    //Delete a track
    @DELETE("tracks/{id}")
    retrofit2.Call<Void> deleteTrack(@Path("id") int id);

    static APIRest createAPIRest() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(APIRest.class);
    }

}
