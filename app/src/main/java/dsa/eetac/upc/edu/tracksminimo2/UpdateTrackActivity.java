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
    //Declaar API
    private APIRest myapirest;
    //Declarar textview y botones que aparecen en el layout para pasar valor
    private TextView updateTrackTitle;
    private TextView updateTrackSinger;
    private Button updateTrackbtn;
    //Creamos track
    public Track updateTrack;
    //Declarar spinner de cargando donde estamos esperando los datos
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_track_layout);

        //Asignamos variable de de los diferents textview y botones a sus variables
        updateTrackTitle = findViewById(R.id.update_track_title);
        updateTrackSinger = findViewById(R.id.update_track_singer);
        updateTrackbtn = findViewById(R.id.update_track_btn);

        //Obtiene el Intent para empiezar la actividad y extraigo el string
        Intent intent = getIntent();
        //Obtiene el Intent para inicia la actividad y extraigo los strings de cada campo
        String messageId = intent.getStringExtra("TRACK ID");
        String[] messageIdParts = messageId.split(" ");
        int id = Integer.parseInt(messageIdParts[1]);
        String title = intent.getStringExtra("TRACK TITLE");
        String[] titleparts = title.split(":");
        updateTrackTitle.setText(titleparts[1]);
        String singer = intent.getStringExtra("TRACK SINGER");
        String[] singerparts = singer.split(":");
        updateTrackSinger.setText(singerparts[1]);

        //A partir de lso datos recogidos de id, titulo y cantante hacemos track
        updateTrack = new Track(id, title, singer);

        //Creamos API( vigilar que API este bien)
        myapirest = APIRest.createAPIRest();

        //Evento boton update
        updateTrackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Progress loading. Justo al abrir esta actividad ponemos el spinner de cargando
                progressDialog = new ProgressDialog(UpdateTrackActivity.this);
                progressDialog.setTitle("Loading...");
                progressDialog.setMessage("Waiting for the server");
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                //llamar función update track
                updateTrack(updateTrack);
            }
        });


    }

    //Función actualizar track
    private void updateTrack(Track updateTrack) {
        //Desarrollamos función del API //Call<Void> updateTrack(@Body Track track);
        Call<Void> trackCall = myapirest.updateTrack(updateTrack);
        trackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    //Si se actualiza el spinner desaparece
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
