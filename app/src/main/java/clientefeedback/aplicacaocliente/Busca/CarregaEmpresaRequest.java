package clientefeedback.aplicacaocliente.Busca;

import android.app.Activity;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaFragment;
import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaRequest;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.Models.Pessoa;
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

    Context c;
    ProgressBar progressBar;
    Empresa empresa;
    List<Favorito>favoritos = new ArrayList<>();
    Avaliacao avaliacao;
    Bundle eBundle = new Bundle();
    Intent intent;
    Integer idEmpresa;
    Integer idPessoa;
    boolean empresaBool = false;
    boolean avaliacaoBool = false;

    Fragment mFragment = new PrincipalEmpresaFragment();

    public CarregaEmpresaRequest(Context c, FragmentManager fm, Integer idEmpresa){
        this.fragmentManager = fm;

        this.c = c;
        this.idEmpresa = idEmpresa;
        this.progressBar = (ProgressBar)((Activity)c).getWindow().getDecorView().findViewById(R.id.pb_load);
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
        try {
            Gson gson = new Gson();
            JSONObject json = new JSONObject(answer);
            JSONObject empresaJson = (JSONObject) json.get("empresa");
            JSONArray favoritosJson = json.getJSONArray("favoritos");
            favoritos = gson.fromJson(favoritosJson.toString(), new TypeToken<ArrayList<Favorito>>() {
            }.getType());

            empresa = gson.fromJson(empresaJson.toString(), Empresa.class);
            if (empresa != null) {

                eBundle.putParcelable("empresa", empresa);
                eBundle.putParcelableArrayList("favoritos", (ArrayList<Favorito>)favoritos);
                mFragment.setArguments(eBundle);
                empresaBool = true;
                (new PrincipalEmpresaRequest(c, getTransactionAvaliacao())).execute();
                beginTransaction();
            }
        }catch(Exception e){};
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"Empresa/getEmpresa/"+idEmpresa+"/"+idPessoa, "", "") );
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
