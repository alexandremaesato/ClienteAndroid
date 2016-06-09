package clientefeedback.aplicacaocliente.Comentario;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
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
            Gson gson = new Gson();
            comentarios = gson.fromJson(comentariosJson.toString(),  new TypeToken<ArrayList<Comentario>>() {}.getType());
            //loadAdapterComentarioDetalhes();
            createView(comentarios.size());
            progressBar.setVisibility(View.GONE);


        } catch (JSONException e) {
            Toast.makeText(context, "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);

            e.printStackTrace();

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"comentario/getComentarioDetalhes/"+empresaId, "", "") );
    }

//    private void loadAdapterComentarioDetalhes(){
//        List<Imagem> imagens = new ArrayList<>();
//        if(comentarios.size() > 0) {
//            listView.setAdapter(new ComentariosAdapter(context, comentarios, imagens));
//        }
//    }

    public void createView(int qtd){
        LinearLayout ll1 = (LinearLayout)rootView.findViewById(R.id.llComentario1);
        LinearLayout ll2 = (LinearLayout)rootView.findViewById(R.id.llComentario2);
        LinearLayout ll3 = (LinearLayout)rootView.findViewById(R.id.llComentario3);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);

        if(qtd>0) {
            ll1.setVisibility(View.VISIBLE);
            TextView tvNome1 = (TextView) rootView.findViewById(R.id.tvNome1);
            if(comentarios.get(0).getPessoa().getNome() != null){
                tvNome1.setText(comentarios.get(0).getPessoa().getNome());
            }

            TextView tvComentario1 = (TextView) rootView.findViewById(R.id.tvComentario1);
            tvComentario1.setText(comentarios.get(0).getDescricao());

            TextView tvData1 = (TextView) rootView.findViewById(R.id.tvData1);
            tvData1.setText(comentarios.get(0).getData_modificacao().toString());
        }
        if(qtd>1) {
            ll2.setVisibility(View.VISIBLE);
            TextView tvNome2 = (TextView) rootView.findViewById(R.id.tvNome2);
            if(comentarios.get(1).getPessoa().getNome() != null) {
                tvNome2.setText(comentarios.get(1).getPessoa().getNome());
            }

            TextView tvComentario2 = (TextView) rootView.findViewById(R.id.tvComentario2);
            tvComentario2.setText(comentarios.get(1).getDescricao());

            TextView tvData2 = (TextView) rootView.findViewById(R.id.tvData2);
            tvData2.setText(comentarios.get(1).getData_modificacao().toString());
        }
        if(qtd>2) {
            ll3.setVisibility(View.VISIBLE);
            TextView tvNome3 = (TextView) rootView.findViewById(R.id.tvNome3);
            if(comentarios.get(2).getPessoa().getNome() != null) {
                tvNome3.setText(comentarios.get(2).getPessoa().getNome());
            }

            TextView tvComentario3 = (TextView) rootView.findViewById(R.id.tvComentario3);
            tvComentario3.setText(comentarios.get(2).getDescricao());

            TextView tvData3 = (TextView) rootView.findViewById(R.id.tvData3);
            tvData3.setText(comentarios.get(2).getData_modificacao().toString());
        }
    }
}
