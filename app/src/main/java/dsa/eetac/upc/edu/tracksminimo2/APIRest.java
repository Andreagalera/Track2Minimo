package dsa.eetac.upc.edu.tracksminimo2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
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

    //GET all tracks. Funcion que da la lista con las diversas canciones
    @GET("tracks")
    Call<List<Track>> getAllTracks();

    //Get an especific track passing its ID
    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id") int id);

    //Create a new track, crear nueva track se pasa body ya que pasamos titulo, artista y id, tipo trak
    @POST("tracks")
    Call<Track> createTrack(@Body Track track);

    //Update a track
    @PUT("tracks")
    Call<Void> updateTrack(@Body Track track);

    //Delete a track, solo necesitamos el parametro id que vamos a borrar
    @DELETE("tracks/{id}")
    Call<Void> deleteTrack(@Path("id") int id);

    //Arrancar API importante
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
