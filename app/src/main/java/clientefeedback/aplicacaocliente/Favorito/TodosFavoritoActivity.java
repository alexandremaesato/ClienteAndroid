package clientefeedback.aplicacaocliente.Favorito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clientefeedback.aplicacaocliente.Avaliacao.TodasAvaliacoesAdapter;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

public class TodosFavoritoActivity extends AppCompatActivity implements Transaction{
    int idPessoa;
    ProgressBar progressBar;
    List<Empresa> empresas;
    List<Favorito> favoritos;
    List<Produto> produtos;
    ListView listView;
    TextView nenhumFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_favoritos);
        SharedData sd = new SharedData(this);
        idPessoa = sd.getPessoaId();
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            this.idPessoa = extras.getInt("idPessoa");
        createView();
        (new VolleyConGET(this, this)).execute();
//        }
    }

    public void createView(){
        nenhumFavorito = (TextView)findViewById(R.id.tvNenhumFavorito);
        listView = (ListView)findViewById(R.id.listViewTodosFavoritos);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void loadView(){
        if(favoritos.size()>0) {
            listView.setAdapter(new TodosFavoritosAdapter(this, produtos, empresas, favoritos));
        }else{
            nenhumFavorito.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
        nenhumFavorito.setVisibility(View.GONE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);

        JSONObject json = null;
        try{
            Gson gson = new Gson();
            json = new JSONObject(answer);

            JSONArray favoritosJson = json.getJSONArray("favoritos");
            JSONArray produtosJson = json.getJSONArray("produtos");
            JSONArray empresasJson = json.getJSONArray("empresas");

            favoritos =    gson.fromJson(favoritosJson.toString(),  new TypeToken<ArrayList<Favorito>>() {}.getType());
            produtos =    gson.fromJson(produtosJson.toString(),  new TypeToken<ArrayList<Produto>>() {}.getType());
            empresas =    gson.fromJson(empresasJson.toString(),  new TypeToken<ArrayList<Empresa>>() {}.getType());

            loadView();

        }catch (Exception e){
            Toast.makeText(getBaseContext(), "Não foi possível recuperar as informações", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl() + "favorito/getFavoritosByIdPessoa/"+idPessoa, "",new HashMap<String,String>() );
    }
}
