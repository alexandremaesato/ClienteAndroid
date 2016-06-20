package clientefeedback.aplicacaocliente.Favorito;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 16/06/2016.
 */
public class FavoritarRequest implements Transaction {
    Context context;
    int idPessoa;
    int idFavoritado;
    boolean check;
    String tipoFavorito;
    ProgressBar progressBar;
    Favorito favorito;

    public FavoritarRequest(Context context, Favorito favorito) {
        this.context = context;
        this.favorito = favorito;
        progressBar = (ProgressBar)((Activity)context).findViewById(R.id.progressBarGeral);
        (new VolleyConn(context, this)).execute();

    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();

    }

    @Override
    public RequestData getRequestData() {
        Map<String,String> hashMap = new HashMap<>();
        Gson gson = new Gson();
        hashMap.put("favorito", gson.toJson(favorito));

        return( new RequestData(Url.getUrl()+"favorito/setFavorito/", "", hashMap) );
    }
}
