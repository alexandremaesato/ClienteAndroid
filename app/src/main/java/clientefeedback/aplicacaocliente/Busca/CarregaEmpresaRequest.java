package clientefeedback.aplicacaocliente.Busca;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaFragment;
import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaRequest;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 09/05/2016.
 */
public class CarregaEmpresaRequest implements Transaction{
    FragmentManager fragmentManager;
    View v;
    Context c;
    ProgressBar progressBar;
    Empresa empresa;
    Avaliacao avaliacao;
    Bundle eBundle = new Bundle();
    Intent intent;
    Integer idEmpresa;
    Integer idPessoa;
    boolean empresaBool = false;
    boolean avaliacaoBool = false;

    Fragment mFragment = new PrincipalEmpresaFragment();

    public CarregaEmpresaRequest(View v,Context c, FragmentManager fm, Integer idEmpresa){
        this.fragmentManager = fm;
        this.v = v;
        this.c = c;
        this.idEmpresa = idEmpresa;
        this.progressBar = (ProgressBar)v.findViewById(R.id.pbProxy);
        SharedData sd= new SharedData(c);
        this.idPessoa = sd.getPessoaId();
        (new PrincipalEmpresaRequest(c, this)).execute();

    }


    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {

        Gson gson = new Gson();
        empresa = gson.fromJson(answer, Empresa.class);
        if (empresa != null) {

            eBundle.putParcelable("empresa", empresa);
            mFragment.setArguments(eBundle);
            empresaBool = true;
            (new PrincipalEmpresaRequest(c, getTransactionAvaliacao())).execute();
            beginTransaction();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"Empresa/getEmpresa/"+idEmpresa, "", "") );
    }

    private Transaction getTransactionAvaliacao(){
        Transaction t = new Transaction() {
            @Override
            public void doBefore() {

            }

            @Override
            public void doAfter(String answer) {

                Gson gson = new Gson();
                avaliacao = gson.fromJson(answer, Avaliacao.class);
                if (avaliacao != null) {
                    eBundle.putParcelable("avaliacao", avaliacao);
                    mFragment.setArguments(eBundle);
                    avaliacaoBool = true;
                    beginTransaction();
                }
            }

            @Override
            public RequestData getRequestData() {
                return( new RequestData(Url.getUrl()+"avaliacao/getAvaliacao/"+idPessoa+"/"+idEmpresa+"/empresa", "", "") );
            }
        };
        return t;
    }

    private void beginTransaction(){
        if(empresaBool && avaliacaoBool) {
            fragmentManager.beginTransaction().replace(R.id.conteudo, mFragment).addToBackStack("busca").commit();
        }
    }
}
