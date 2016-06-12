package clientefeedback.aplicacaocliente.Comentario;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 18/05/2016.
 */
public class ComentarioDialogFragment extends DialogFragment implements Transaction {
    ProgressBar progressBar;
    Comentario comentario;
    EditText editTextComentario;
    Button comentar;
    Button cancelar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            comentario = bundle.getParcelable("comentario");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_fragment_comentario, container);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBarDialog);
        progressBar.setVisibility(View.GONE);

        editTextComentario = (EditText)view.findViewById(R.id.editTextComentario);
//        if(comentario.getDescricao() != "" && comentario.getDescricao() != "null") {
//            editTextComentario.setText(comentario.getDescricao());
//        }

        Button btnEnviarComentario = (Button) view.findViewById(R.id.btnComentar);
        btnEnviarComentario.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
                doRequest();

                //getTargetFragment().onActivityResult(getTargetRequestCode(), 1, getActivity().getIntent());
                //dismiss();
            }
        });

        Button btnCancelar = (Button)view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Log.i("Script", "onActivityCreated()");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.i("Script", "onAttach()");
    }


    @Override
    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        Log.i("Script", "onCancel()");
    }

    public void doRequest(){
        (new VolleyConn(getContext(), this)).execute();
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);
        if(!"".equals(answer)){
            Toast.makeText(getContext(), answer, Toast.LENGTH_SHORT).show();
            if("Comentario Inserido com sucesso".equals(answer)){
                dismiss();
            }
        }


    }

    @Override
    public RequestData getRequestData() {
        HashMap<String,String> params = new HashMap<>();
        Gson gson = new Gson();
        comentario.setDescricao(editTextComentario.getText().toString());
        params.put("comentario", gson.toJson(comentario));
        return( new RequestData(Url.getUrl()+"comentario/setComentario", "", params) );
    }
}
