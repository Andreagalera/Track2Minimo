package dsa.eetac.upc.edu.tracksminimo2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTrackActivity extends AppCompatActivity {
    private TextView updateTrackTitle;
    private TextView updateTrackSinger;
    private Button updateTrackbtn;
    private APIRest myapirest;
    public Track updateTrack;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_track_layout);

        updateTrackTitle = findViewById(R.id.update_track_title);
        updateTrackSinger = findViewById(R.id.update_track_singer);
        updateTrackbtn = findViewById(R.id.update_track_btn);

        Intent intent = getIntent();
        String messageId = intent.getStringExtra("TRACK ID");
        String[] messageIdParts = messageId.split(" ");
        int id = Integer.parseInt(messageIdParts[1]);
        String title = intent.getStringExtra("TRACK TITLE");
        String[] titleparts = title.split(":");
        updateTrackTitle.setText(titleparts[1]);
        String singer = intent.getStringExtra("TRACK SINGER");
        String[] singerparts = singer.split(":");
        updateTrackSinger.setText(singerparts[1]);

        updateTrack = new Track(id, title, singer);

        myapirest = APIRest.createAPIRest();

        updateTrackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress loading
                progressDialog = new ProgressDialog(UpdateTrackActivity.this);
                progressDialog.setTitle("Loading...");
                progressDialog.setMessage("Waiting for the server");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                updateTrack(updateTrack);
            }
        });


    }

    private void updateTrack(Track updateTrack) {
        Call<Void> trackCall = myapirest.updateTrack(updateTrack);

        trackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    progressDialog.hide();
                }
                else{
                    Log.e("No api connection", response.message());

                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateTrackActivity.this);

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
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateTrackActivity.this);

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
