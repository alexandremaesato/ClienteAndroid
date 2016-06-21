package clientefeedback.aplicacaocliente.Empresa;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 21/06/2016.
 */
public class SouDonoDialog extends DialogFragment {
    int id;
    public SouDonoDialog(int id){
        this.id = id;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Você é o dono deste estabelecimento?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new SouDonoRequest(getContext(),id);
                        afterOk();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void afterOk(){

    }
}
