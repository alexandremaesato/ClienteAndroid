package clientefeedback.aplicacaocliente.Perfil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 13/06/2016.
 */
public class PerfilRequest implements Transaction {
    Context context;
    ProgressBar progressBar;
    SharedData sharedData;
    int idPessoa;
    Pessoa pessoa;
    EditText nome;
    EditText sobrenome;
    TextView data;
    ImageView imagem;
    private ImageLoader imageLoader;


    public PerfilRequest(Context context){
        this.context = context;
        progressBar = (ProgressBar)((Activity)context).findViewById(R.id.progressBar);
        nome = (EditText)((Activity)context).findViewById(R.id.etNome);
        sobrenome = (EditText)((Activity)context).findViewById(R.id.etSobrenome);
        data = (TextView)((Activity)context).findViewById(R.id.tvDataNascimento);
        sharedData = new SharedData(context);
        idPessoa = sharedData.getPessoaId();
        imagem = (ImageView)((Activity)context).findViewById(R.id.ivImage);
        imageLoader = ImageLoaderCustom.getImageloader(context);
        (new VolleyConGET(context,this)).execute();
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {
        progressBar.setVisibility(View.GONE);
        if(answer != null){
            try{
                Gson gson = new Gson();
                pessoa = gson.fromJson(answer, Pessoa.class);
                loadView();
            }catch (Exception e){
                Toast.makeText(context, answer, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl()+"pessoa/getPessoa/"+idPessoa,  "", "");
    }

    public void loadView(){
        if(pessoa.getNome() != null) {
            nome.setText(pessoa.getNome());
        }
        if(pessoa.getSobrenome() != null ) {
            sobrenome.setText(pessoa.getSobrenome());
        }
        if(pessoa.getDataNascimento() != null) {
            data.setText(pessoa.getDataNascimento().toString());
        }
        String url;
        if(pessoa.getImagemPerfil() != null){
            url = Url.URL_IMAGEM + pessoa.getImagemPerfil().getCaminho();
        }else{
            url = Url.URL_IMAGEM + "/images/sem_imagem.jpg";
        }

        imageLoader.displayImage(url, imagem);
    }

//    public Bundle getPessoa(){
//        Bundle b = new Bundle();
//        b.putParcelable("pessoa",pessoa);
//        return b;
//    }

}
