package clientefeedback.aplicacaocliente.Avaliacao;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.google.gson.Gson;

import java.util.HashMap;

import clientefeedback.aplicacaocliente.Interfaces.CallBack;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 11/06/2016.
 */
public class AvaliacaoDialogProdutoFragment extends DialogFragment implements Transaction{
    ProgressBar progressBar;
    Avaliacao avaliacao;
    RatingBar ratingBar;
    EditText editTextComentario;
    CallBack callBack;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            avaliacao = bundle.getParcelable("avaliacao");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_fragment_avaliacao, container);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBarDialog);
        progressBar.setVisibility(View.GONE);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBarDialog);
        Integer notaInt = avaliacao.getNota();
        Float notaFloat = notaInt.floatValue();
        notaFloat = (notaFloat/10)/2;
        ratingBar.setRating(notaFloat);


        editTextComentario = (EditText)view.findViewById(R.id.etComentario);
        editTextComentario.setText(avaliacao.getDescricao());

        Button btnEnviarAvaliacao = (Button) view.findViewById(R.id.btnEnviarAvaliacao);
        btnEnviarAvaliacao.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest();
                callBack.executeThis();
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
    }

    @Override
    public RequestData getRequestData() {
        HashMap<String,String> params = new HashMap<>();
        Gson gson = new Gson();
        Float f = ratingBar.getRating();
        f = f *10*2;
        Integer i = f.intValue();
        avaliacao.setNota(i);
        avaliacao.setTipoAvalicao("produto");
        avaliacao.setDescricao(editTextComentario.getText().toString());
        params.put("avaliacao", gson.toJson(avaliacao));
        return( new RequestData(Url.getUrl()+"avaliacao/setAvaliacao", "", params) );
    }

    public void setCallBack(CallBack c){
        this.callBack = c;
    }

}
