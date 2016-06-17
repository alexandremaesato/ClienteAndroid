package clientefeedback.aplicacaocliente.Comentario;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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

import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogProdutoFragment;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.Produto.ProdutoAvaliacoesAdapter;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

public class TodosComentariosActivity extends AppCompatActivity implements Transaction {
    int idEmpresa;
    ProgressBar progressBar;
    ListView listView;
    List<Comentario> comentarios;
    List<Pessoa> pessoas;
    TextView semComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_comentarios);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idEmpresa = extras.getInt("idEmpresa");
            createView();
            (new VolleyConGET(this, this)).execute();
        }
    }

    public void createView(){
        listView = (ListView)findViewById(R.id.listViewTodosComentarios);
        progressBar = (ProgressBar)findViewById(R.id.progressBarComentarios);
        semComentarios = (TextView)findViewById(R.id.tvSemComentarios);
    }

    public void loadView(){
        listView.setAdapter(new TodosComentariosAdapter(this, comentarios, pessoas));
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
        semComentarios.setVisibility(View.GONE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);
        JSONObject json = null;
        try{
            Gson gson = new Gson();
            comentarios = gson.fromJson(answer,  new TypeToken<ArrayList<Comentario>>() {}.getType());
            if ( comentarios.size() > 0){
                loadView();
            }else{
                semComentarios.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            Toast.makeText(getBaseContext(), "Não foi possível recuperar as informações", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl() + "comentario/getComentariosByIdEmpresa/"+idEmpresa, "",new HashMap<String,String>() );
    }
}
