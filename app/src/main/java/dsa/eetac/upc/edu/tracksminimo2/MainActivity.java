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

    //Declaar API
    private APIRest myapirest;
    //Declarar/Crear Recycler
    private Recycler recycler;
    private RecyclerView recyclerView;
    //Declarar spinner de cargando donde estamos esperando los datos
    ProgressDialog progressDialog;
    //Declarar token (2 funciones al final)
    private String token;
    //Declarar textview y boton que aparecen en el layout para pasar valor(solo utilizamos botones)
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

        //Identificamos el recyclerview cerado antes con el nombre que tenga en el xml(LLAMALO recyclerView)
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recycler = new Recycler(this);
        recyclerView.setAdapter(recycler);
        recyclerView.setHasFixedSize(true);
        //Asignamos a cada liena del recyclerview el lienarlayout de itemfollowe
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Asignamos variable de de los diferents textview y botones view a sus variables
        idTrack = findViewById(R.id.idTrack);
        titleTrack = findViewById(R.id.titleTrack);
        singerTrack = findViewById(R.id.singerTrack);
        getAllTracks = findViewById(R.id.getAllTracksbtn);
        getSingleTrack = findViewById(R.id.getSingleTrackbtn);
        createTrack = findViewById(R.id.createTrackbtn);

        //Progress loading. Justo al abrir esta actividad ponemos el spinner de cargando
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Waiting for the server");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        //Creamos API( vigilar que API este bien)
        myapirest = APIRest.createAPIRest();

        //Llamar funcion que te da la lista de tracks
        getAllTracks();

        // Definimos las funciones que se ejecutarán al dar Click a los tres botones
        getSingleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            // Llama a la función que abre una ventanita para darte la información de ese Track
            public void onClick(View v) {
                openDialog();
            }
        });

        getAllTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            // Llama a la función getAllTrack (solo actualiza), no muestra ni abre nada
            public void onClick(View v) {
                getAllTracks();
            }
        });

        createTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            // Llama a la función que abre un nuevo Activity
            public void onClick(View v) {
                openCreateLayout();
            }
        });

    }

    //Abrir layout createactivity
    private void openCreateLayout(){
        Intent intent = new Intent(this, CreateTrackActivity.class);
        startActivity(intent);
    }

    //Abrir ver una cancion, no se un intent si no como antes te sale un cuadro de texto preguntandote el id y cuando se pone se abre el layout
    private void openDialog() {
        SingleTrackDialog  singleTrackDialog= new SingleTrackDialog();
        singleTrackDialog.show(getSupportFragmentManager(), "Single Track Dialog");
    }

    //Poner el id para buscar una cancion
    @Override
    public void applyTexts(int id){
        //Llama funcion getSingleTrack
        getSingleTrack(id);
    }


    //Función que te da la lista de tracks
    public void getAllTracks() {
        //Desarrollamos la función API //Call<List<Track>> getAllTracks();
        Call<List<Track>> trackCall = myapirest.getAllTracks();
        trackCall.enqueue(new Callback<List<Track>>() {
            //Si entra en el response esque la concexion ha ido bien
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if(response.isSuccessful()){
                    //Creamos lista usuario track list con los datos de List<Track> response
                    List<Track> tracksList = response.body();

                    //Si la lista no es 0 el recycler se cargara con la lista
                    if(tracksList.size() != 0){
                        recycler.clear();
                        recycler.addTracks(tracksList);
                    }

                    //Si el recyler se crea desaparece el pregressDialog
                    progressDialog.hide();

                    //Este for sirve para saber en el logcast elid lod ids y el tamaño de la lista de cancioens del recycler
                    for(int i = 0; i < tracksList.size(); i++) {
                        Log.i("Track id: " + tracksList.get(i).id, response.message());
                    }
                    Log.i("Size of the list: " +tracksList.size(), response.message());
                }

                //Si no se ha podido recoger bien la información( Siempre igual, cambiar response failure)
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

            //Si no se ha podido connectar con la API
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

    //Funcion GET que devuelve una cancion a partir de su id
    private void getSingleTrack (int id){
        //Desarrollamos función del API //Call<Track> getTrack(@Path("id") int id);
        //Si fuera un string hay que crear variable private String message
        Call<Track> trackCall = myapirest.getTrack(id);
        //Siempre igual
        trackCall.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(response.isSuccessful()){
                    //Creamos nuevo usuario, se utiliza body ya que tiene más de un atributo
                    Track track = response.body();

                    //Si el recycler no es 0 el recycler se cargara con la lista
                    if(recycler.getItemCount() != 0){
                        recycler.clear();
                        recycler.addSingleTrack(track);
                    }

                    Log.i("Single Track id: " +track.id, response.message());
                    //Si se ha mostrado bien la información el spinner desaparecera
                    progressDialog.hide();
                }
                //Si no se ha podido recoger bien la información( Siempre igual, cambiar response failure)

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

    //Token
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
