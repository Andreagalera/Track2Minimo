package dsa.eetac.upc.edu.tracksminimo2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    //Para recojer el texto del TextView al cliclarle boton
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    //Creamos lista con la estrcutura de track para recoger todos las tracks. Llamlo data
    private List<Track> data;
    //Necesario paar el constructor del recycler
    private Context context;
    //Declaar API
    private APIRest myapirest;

    //Añadir
    //Añade a la lista data de las tracks
    public void addTracks(List<Track> tracksList) {
        data.addAll(tracksList);
        notifyDataSetChanged();
    }

    //Añade nuva cancion
    public void addSingleTrack(Track track) {
        data.add(track);
    }

    //Gestionamos el RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        //DEcalramos los textviews y botones del recycler
        private LinearLayout linearLayout;
        private TextView idTrackView;
        private TextView titleTrackView;
        private TextView singerTrackView;
        private Button deletebtn;

        //Con esta funcion a asignamos las variables anteriores a los nombres del textview y botones del item
        public ViewHolder(View v) {
            super(v);
            idTrackView = v.findViewById(R.id.idTrack);
            titleTrackView = v.findViewById(R.id.titleTrack);
            singerTrackView = v.findViewById(R.id.singerTrack);
            deletebtn = v.findViewById(R.id.deleteTrackbtn);
            linearLayout = v.findViewById(R.id.linearLayout);
        }
    }

    //Constructor, utilizar context. Declaramos la lista data iniciandola
    //Llamamos APIRest
    public Recycler(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        myapirest = APIRest.createAPIRest();
    }

    //Inflamos el reccylerView con las filas del item_follower
    @Override
    public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(v);
    }

    //Donde se muestra las cosas
    @Override
    public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
        //Creamos variable track y vamos guardando la información de la lista data
        Track trackData = data.get(position);
        //Esta el id de esta manera porque es int
        //Para cada variable añadimos su campos de textView
        holder.idTrackView.setText("ID: " + String.valueOf(trackData.id));
        holder.titleTrackView.setText("Title: " +trackData.title);
        holder.singerTrackView.setText("Singer: " +trackData.singer);

        //Evento del boton
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(trackData.id);
            }
        });

        //Si clicamos una fila del recycler nos lleva a otro layout para editar titulo o cantante
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos un intent para abrir el nuevo activity, le ponemos el nombre de la clase relacionada
                Intent intent = new Intent(context, UpdateTrackActivity.class);

                //Creamos y asignamos variable de de los diferents textviews
                TextView textId = v.findViewById(R.id.idTrack);
                TextView textTitle = v.findViewById(R.id.titleTrack);
                TextView textSinger = v.findViewById(R.id.singerTrack);
                //Guardamos el valor id del editText una variable tipo String
                String messageId = textId.getText().toString();
                //Trozear Solo queremos int y asi nos aseguramos que solo cojemos id
                String[] messageIdparts = messageId.split(":");
                String id = messageIdparts[1];
                //Guardamos el valor del titulo y cantante editText una variable tipo String
                String messageTitle = textTitle.getText().toString();
                String messageSinger = textSinger.getText().toString();
                //Para pasar el string de un a otro activity se lo defines de esta manera
                intent.putExtra("TRACK ID", id);
                intent.putExtra("TRACK TITLE", messageTitle);
                intent.putExtra("TRACK SINGER", messageSinger);
                //Se abre la actividad
                context.startActivity(intent);
            }
        });
    }

    //Funcion eliminar cancion
    private void deleteTrack(int id) {
        //Como el boton eliminar esta dentro del recyler //Desarrollamos función del API
        //Call<Void> deleteTrack(@Path("id") int id);
        Call<Void> trackCall = myapirest.deleteTrack(id);
        trackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    //Cuando se elimina sale un mensaje en negro diciendo la id elimanda
                    Toast.makeText(context, "Song with ID: " +id + " deleted", Toast.LENGTH_LONG).show();
                }
                else{

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    //Poner
    public void clear(){
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    //Devuelce el tamaño de la información de Item. Poner
    @Override
    public int getItemCount() {
        return data.size();
    }
}

