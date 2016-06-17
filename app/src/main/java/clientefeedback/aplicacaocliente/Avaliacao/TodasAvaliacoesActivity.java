package clientefeedback.aplicacaocliente.Avaliacao;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clientefeedback.aplicacaocliente.Comentario.TodosComentariosAdapter;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.Produto.ProdutoAvaliacoesAdapter;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

public class TodasAvaliacoesActivity extends AppCompatActivity implements Transaction{
    int idEmpresa;
    ProgressBar progressBar;
    List<Pessoa> pessoas;
    List<Avaliacao> avaliacoes;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todas_avaliacoes);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idEmpresa = extras.getInt("idEmpresa");
            createView();
            (new VolleyConGET(this,this)).execute();
        }
    }

    public void createView(){
        listView = (ListView)findViewById(R.id.listViewAvaliacoes);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void loadView(){
        listView.setAdapter(new TodasAvaliacoesAdapter(this, avaliacoes, pessoas));
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);

        JSONObject json = null;
        try{
            Gson gson = new Gson();
            json = new JSONObject(answer);
            JSONArray avaliacoesJson = json.getJSONArray("avaliacoes");
            JSONArray pessoasJson = json.getJSONArray("pessoas");
            pessoas =    gson.fromJson(pessoasJson.toString(),  new TypeToken<ArrayList<Pessoa>>() {}.getType());
            avaliacoes = gson.fromJson(avaliacoesJson.toString(),  new TypeToken<ArrayList<Avaliacao>>() {}.getType());
            loadView();
        }catch (Exception e){
            Toast.makeText(getBaseContext(), "Não foi possível recuperar as informações", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl() + "avaliacao/getAvaliacoesByIdEmpresa/"+idEmpresa, "",new HashMap<String,String>() );
    }
}
