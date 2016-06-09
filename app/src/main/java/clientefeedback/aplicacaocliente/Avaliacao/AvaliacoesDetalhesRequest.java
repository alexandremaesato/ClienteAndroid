package clientefeedback.aplicacaocliente.Avaliacao;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Comentario.ComentariosAdapter;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 08/06/2016.
 */
public class AvaliacoesDetalhesRequest implements Transaction{
    Context context;
    View rootView;
    ProgressBar progressBar;
    ScrollView scrollView;
    ListView listView;
    int empresaId;
    List<Avaliacao> avaliacoes;
    List<Pessoa> pessoas;


    public AvaliacoesDetalhesRequest(Context c, int empresaId){
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
            JSONArray avaliacoesJson = json.getJSONArray("avaliacoes");
            JSONArray pessoasJson = json.getJSONArray("pessoas");
            Gson gson = new Gson();
            avaliacoes = gson.fromJson(avaliacoesJson.toString(),  new TypeToken<ArrayList<Avaliacao>>() {}.getType());
            pessoas = gson.fromJson(pessoasJson.toString(),  new TypeToken<ArrayList<Pessoa>>() {}.getType());
            createView(avaliacoes.size());
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
        return( new RequestData(Url.getUrl()+"avaliacao/getAvaliacoesByIdEmpresa/"+empresaId, "", "") );
    }


    public void createView(int qtd){
        //List<String> nomes = new ArrayList<>();
        LinearLayout ll1 = (LinearLayout)rootView.findViewById(R.id.llAvaliacao1);
        LinearLayout ll2 = (LinearLayout)rootView.findViewById(R.id.llAvaliacao2);
        LinearLayout ll3 = (LinearLayout)rootView.findViewById(R.id.llAvaliacao3);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);

        if(qtd>0) {
            ll1.setVisibility(View.VISIBLE);
            TextView tvAvaliacaoNome1 = (TextView) rootView.findViewById(R.id.tvAvaliacaoNome1);
            if(pessoas.get(0).getNome() != null){
                tvAvaliacaoNome1.setText(pessoas.get(0).getNome());
            }

            TextView tvAvaliacaoComentario1 = (TextView) rootView.findViewById(R.id.tvAvaliacaoComentario1);
            if(avaliacoes.get(0).getDescricao() != null) {
                tvAvaliacaoComentario1.setText(avaliacoes.get(0).getDescricao());
            }

            TextView tvAvaliacaoData1 = (TextView) rootView.findViewById(R.id.tvAvaliacaoData1);
            if(avaliacoes.get(0).getData_modificacao() != null) {
                tvAvaliacaoData1.setText(avaliacoes.get(0).getData_modificacao().toString());
            }

            RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBarAvaliacao1);
            Integer nota = avaliacoes.get(0).getNota();
            Float notaFloat = nota.floatValue();
            notaFloat = (notaFloat/10)/2;
            ratingBar.setRating(notaFloat);

        }
        if(qtd>1) {
            ll2.setVisibility(View.VISIBLE);
            TextView tvAvaliacaoNome2 = (TextView) rootView.findViewById(R.id.tvAvaliacaoNome2);
            if(pessoas.get(1).getNome() != null){
                tvAvaliacaoNome2.setText(pessoas.get(1).getNome());
            }

            TextView tvAvaliacaoComentario2 = (TextView) rootView.findViewById(R.id.tvAvaliacaoComentario2);
            if(avaliacoes.get(1).getDescricao() != null) {
                tvAvaliacaoComentario2.setText(avaliacoes.get(1).getDescricao());
            }

            TextView tvAvaliacaoData2 = (TextView) rootView.findViewById(R.id.tvAvaliacaoData2);
            if(avaliacoes.get(1).getData_modificacao() != null) {
                tvAvaliacaoData2.setText(avaliacoes.get(1).getData_modificacao().toString());
            }

            RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBarAvaliacao2);
            Integer nota = avaliacoes.get(1).getNota();
            Float notaFloat = nota.floatValue();
            notaFloat = (notaFloat/10)/2;
            ratingBar.setRating(notaFloat);
        }
        if(qtd>2) {
            ll3.setVisibility(View.VISIBLE);
            TextView tvAvaliacaoNome3 = (TextView) rootView.findViewById(R.id.tvAvaliacaoNome3);
            if(pessoas.get(2).getNome() != null){
                tvAvaliacaoNome3.setText(pessoas.get(2).getNome());
            }

            TextView tvAvaliacaoComentario3 = (TextView) rootView.findViewById(R.id.tvAvaliacaoComentario3);
            if(avaliacoes.get(2).getDescricao() != null) {
                tvAvaliacaoComentario3.setText(avaliacoes.get(2).getDescricao());
            }

            TextView tvAvaliacaoData3 = (TextView) rootView.findViewById(R.id.tvAvaliacaoData3);
            if(avaliacoes.get(2).getData_modificacao() != null) {
                tvAvaliacaoData3.setText(avaliacoes.get(2).getData_modificacao().toString());
            }

            RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBarAvaliacao3);
            Integer nota = avaliacoes.get(2).getNota();
            Float notaFloat = nota.floatValue();
            notaFloat = (notaFloat/10)/2;
            ratingBar.setRating(notaFloat);

        }
    }


}
