package dsa.eetac.upc.edu.tracksminimo2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SingleTrackDialog.SingleTrackDialogListener{

    private APIRest myapirest;
    private Recycler recycler;
    private RecyclerView recyclerView;

    ProgressDialog progressDialog;
    private String token;
    private TextView idTrack;
    private TextView titleTrack;
    private TextView singerTrack;
    private Button getAllTracks;
    private Button getSingleTrack;
    private Button createTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Recycler(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        idTrack = findViewById(R.id.idTrack);
        titleTrack = findViewById(R.id.titleTrack);
        singerTrack = findViewById(R.id.singerTrack);
        getAllTracks = findViewById(R.id.getAllTracksbtn);
        getSingleTrack = findViewById(R.id.getSingleTrackbtn);
        createTrack = findViewById(R.id.createTrackbtn);

        //Progress loading
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        myapirest = APIRest.createAPIRest();

        getAllTracks();

        getSingleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        getAllTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllTracks();
            }
        });

        createTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateLayout();
            }
        });

    }

    private void openCreateLayout(){
        Intent intent = new Intent(this, CreateTrackActivity.class);
        startActivity(intent);
    }

    private void openDialog() {
        SingleTrackDialog  singleTrackDialog= new SingleTrackDialog();
        singleTrackDialog.show(getSupportFragmentManager(), "Single Track Dialog");
    }

    @Override
    public void applyTexts(int id){
        getSingleTrack(id);
    }


    public void getAllTracks() {
        Call<List<Track>> trackCall = myapirest.getAllTracks();

        trackCall.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if(response.isSuccessful()){
                    List<Track> tracksList = response.body();

                    if(tracksList.size() != 0){
                        recycler.clear();
                        recycler.addTracks(tracksList);
                    }

                    progressDialog.hide();

                    for(int i = 0; i < tracksList.size(); i++) {
                        Log.i("Track id: " + tracksList.get(i).id, response.message());
                    }
                    Log.i("Size of the list: " +tracksList.size(), response.message());
                }
                else{
                    Log.e("No api connection", response.message());

                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void getSingleTrack (int id){
        Call<Track> trackCall = myapirest.getTrack(id);

        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(response.isSuccessful()){
                    Track track = response.body();

                    if(recycler.getItemCount() != 0){
                        recycler.clear();
                        recycler.addSingleTrack(track);
                    }

                    Log.i("Single Track id: " +track.id, response.message());

                    progressDialog.hide();
                }
                else{
                    Log.e("No api connection", response.message());

                    progressDialog.hide();

                    //Show the alert dialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, which) -> {
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.e("No api connection: ", t.getMessage());

                progressDialog.hide();

                //Show the alert dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilder
                        .setTitle("Error")
                        .setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, which) -> {
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (token != null) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            token = data.getStringExtra("token");
        }
    }
}
