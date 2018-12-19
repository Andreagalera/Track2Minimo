package dsa.eetac.upc.edu.tracksminimo2;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTrackActivity extends AppCompatActivity {

    private TextView newTrackID;
    private TextView newTrackTitle;
    private TextView newTrackSinger;
    private Button createNewTrackbtn;
    public Track newTrack;
    private APIRest myapirest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_track);

        newTrackID = findViewById(R.id.update_track_id);
        newTrackTitle = findViewById(R.id.new_track_title);
        newTrackSinger = findViewById(R.id.new_track_singer);
        createNewTrackbtn = findViewById(R.id.create_track_btn);

        myapirest = APIRest.createAPIRest();

        createNewTrackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(newTrackID.getText().toString());
                String title = newTrackTitle.getText().toString();
                String singer = newTrackSinger.getText().toString();

                newTrack = new Track(id, title, singer);

                createNewTrack(newTrack);
            }
        });
    }

    private void createNewTrack(Track newTrack) {
        Call<Track> trackCall = myapirest.createTrack(newTrack);

        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(response.isSuccessful()){
                    finish();
                }
                else{
                    Log.e("No api connection", response.message());

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateTrackActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> finish());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateTrackActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> finish());

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

}
