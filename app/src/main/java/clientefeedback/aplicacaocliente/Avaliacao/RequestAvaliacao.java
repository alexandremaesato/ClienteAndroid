package clientefeedback.aplicacaocliente.Avaliacao;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaRequest;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 22/05/2016.
 */
public class RequestAvaliacao implements Transaction {
    Context c;
    View v;
    TextView notaAvaliacao;
    TextView comentarioAvaliacao;
    ProgressBar pgAvaliacao;
    Avaliacao avaliacao;
    int id;

    public RequestAvaliacao(Context c, View v, int id){
        this.c = c;
        this.v = v;
        this.id = id;
        notaAvaliacao = (TextView)v.findViewById(R.id.nota);
        comentarioAvaliacao = (TextView)v.findViewById(R.id.comentario);
        pgAvaliacao = (ProgressBar) v.findViewById(R.id.progressBarAvaliacao);
        (new PrincipalEmpresaRequest(c, this)).execute();
    }

    public void execute(){
        (new PrincipalEmpresaRequest(c, this)).execute();
    }

    @Override
    public void doBefore() {
        pgAvaliacao.setVisibility(View.VISIBLE);

    }

    @Override
    public void doAfter(String answer) {
        Gson gson = new Gson();
        avaliacao = gson.fromJson(answer, Avaliacao.class);
        if (avaliacao != null) {
            notaAvaliacao.setText(String.valueOf(avaliacao.getNota()));
            comentarioAvaliacao.setText(avaliacao.getDescricao());
        }
        pgAvaliacao.setVisibility(View.GONE);

    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"avaliacao/getAvaliacaoById/"+id, "", "") );
    }
}
