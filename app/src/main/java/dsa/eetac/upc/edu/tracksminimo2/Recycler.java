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

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private List<Track> data;
    private Context context;
    private APIRest myapirest;

    public void addTracks(List<Track> tracksList) {
        data.addAll(tracksList);
        notifyDataSetChanged();
    }

    public void addSingleTrack(Track track) {
        data.add(track);
    }

    //Asign the text TextView to the text1 in the layout
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView idTrackView;
        private TextView titleTrackView;
        private TextView singerTrackView;
        private Button deletebtn;

        public ViewHolder(View v) {
            super(v);
            idTrackView = v.findViewById(R.id.idTrack);
            titleTrackView = v.findViewById(R.id.titleTrack);
            singerTrackView = v.findViewById(R.id.singerTrack);
            deletebtn = v.findViewById(R.id.deleteTrackbtn);
            linearLayout = v.findViewById(R.id.linearLayout);
        }
    }

    //Constructor
    public Recycler(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
        myapirest = APIRest.createAPIRest();
    }

    @Override
    public Recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Recycler.ViewHolder holder, int position) {
        Track trackData = data.get(position);
        holder.idTrackView.setText("ID: " + String.valueOf(trackData.id));
        holder.titleTrackView.setText("Title: " +trackData.title);
        holder.singerTrackView.setText("Singer: " +trackData.singer);

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(trackData.id);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateTrackActivity.class);
                TextView textId = v.findViewById(R.id.idTrack);
                TextView textTitle = v.findViewById(R.id.titleTrack);
                TextView textSinger = v.findViewById(R.id.singerTrack);
                String messageId = textId.getText().toString();
                String[] messageIdparts = messageId.split(":");
                String id = messageIdparts[1];
                String messageTitle = textTitle.getText().toString();
                String messageSinger = textSinger.getText().toString();
                intent.putExtra("TRACK ID", id);
                intent.putExtra("TRACK TITLE", messageTitle);
                intent.putExtra("TRACK SINGER", messageSinger);
                context.startActivity(intent);
            }
        });
    }

    private void deleteTrack(int id) {
        Call<Void> trackCall = myapirest.deleteTrack(id);

        trackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
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

    public void clear(){
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

