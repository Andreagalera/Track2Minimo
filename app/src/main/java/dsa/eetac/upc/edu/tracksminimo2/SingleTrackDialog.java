package dsa.eetac.upc.edu.tracksminimo2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class SingleTrackDialog extends AppCompatDialogFragment {
    //Declara textview que aparece en el layput para pasar el valor
    private EditText trackidtext;
    //Crea/Declara eñ escuchador
    private SingleTrackDialogListener listener;

    //Es dialog
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Type a track ID")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = Integer.parseInt(trackidtext.getText().toString());
                        listener.applyTexts(id);
                    }
                });

        trackidtext = view.findViewById(R.id.id_track);

        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (SingleTrackDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface SingleTrackDialogListener{
        void applyTexts(int id);
    }

}
