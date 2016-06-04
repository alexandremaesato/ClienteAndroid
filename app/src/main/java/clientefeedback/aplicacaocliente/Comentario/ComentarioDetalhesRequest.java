package clientefeedback.aplicacaocliente.Comentario;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 31/05/2016.
 */
public class ComentarioDetalhesRequest implements Transaction {
    Context context;
    View rootView;
    ProgressBar progressBar;
    ScrollView scrollView;
    ListView listView;
    int empresaId;
    List<Comentario> comentarios;
    List<Pessoa> pessoas;

    public ComentarioDetalhesRequest(Context c, int empresaId){
        this.context = c;
        this.empresaId = empresaId;
        rootView = ((Activity)c).getWindow().getDecorView().findViewById(android.R.id.content);
        listView = (ListView)rootView.findViewById(R.id.lvComentarios);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBarComentarioDetalhes);
        scrollView = (ScrollView)rootView.findViewById(R.id.scroll);

        (new VolleyConGET(context, this)).execute();

    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {

        JSONObject json = null;
        try {
            json = new JSONObject(answer);

            JSONArray comentariosJson = json.getJSONArray("comentarios");
            JSONArray pessoasJson = json.getJSONArray("pessoas");
            Gson gson = new Gson();

            comentarios = gson.fromJson(comentariosJson.toString(),  new TypeToken<ArrayList<Comentario>>() {}.getType());
            pessoas = gson.fromJson(pessoasJson.toString(),  new TypeToken<ArrayList<Pessoa>>() {}.getType());

            loadAdapterComentarioDetalhes();
            progressBar.setVisibility(View.GONE);


        } catch (JSONException e) {
            Toast.makeText(context, "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"comentario/getComentarioDetalhes/"+empresaId, "", "") );
    }

    private void loadAdapterComentarioDetalhes(){
        List<Imagem> imagens = new ArrayList<>();
        if(comentarios.size() > 0) {
            listView.setAdapter(new ComentariosAdapter(context, comentarios, pessoas, imagens));
        }
    }
}
