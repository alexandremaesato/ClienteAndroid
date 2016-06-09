package clientefeedback.aplicacaocliente.Empresa;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import java.util.HashMap;

import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 07/06/2016.
 */
public class SouDonoRequest implements Transaction {
    private Context context;
    private int identidade_criada;
    private ProgressBar progressBar;
    private SharedData sharedData;

    public SouDonoRequest(Context context, int identidade_criada){
        this.context = context;
        this.identidade_criada = identidade_criada;
        (new VolleyConn(context,this)).execute();
    }

    @Override
    public void doBefore() {
        progressBar = (ProgressBar)((Activity) context).findViewById(R.id.progressBarGeral);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public RequestData getRequestData() {
        sharedData = new SharedData(context);
        int idresponsavel = sharedData.getPessoaId();
//        HashMap<String,String> hm = new HashMap<String,String>();
//        hm.put("idreponsavel",String.valueOf(idresponsavel));
//        hm.put("identidade_criada",String.valueOf(identidade_criada));
        return( new RequestData(Url.getUrl()+"Empresa/setSouDono/"+identidade_criada+"/"+idresponsavel, "",new HashMap<>() ) );
    }
}
