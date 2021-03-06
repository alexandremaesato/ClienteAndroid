package clientefeedback.aplicacaocliente.Produto;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogFragment;
import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogProdutoFragment;
import clientefeedback.aplicacaocliente.Avaliacao.RequestAvaliacao;
import clientefeedback.aplicacaocliente.Favorito.FavoritarRequest;
import clientefeedback.aplicacaocliente.Interfaces.CallBack;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

public class ProdutoDetalhes extends AppCompatActivity implements Transaction, CallBack {
    int idProduto;
    boolean favoritado;
    ProgressBar progressBar;
    ImageView imagem;
    TextView preco;
    TextView descricao;
    TextView tvNomeProduto;
    Produto produto;
    List<Pessoa> pessoas;
    ListView listView;
    private float scale;
    private int width;
    private int height;
    Button btnAvaliar;
    ToggleButton btnFavorito;
    View header;
    AvaliacaoDialogProdutoFragment avaliacaoDialogProdutoFragment;
    VolleyConGET conn;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avaliacaoDialogProdutoFragment = new AvaliacaoDialogProdutoFragment();
        Fresco.initialize(this);
        setContentView(R.layout.activity_produto_detalhes);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.idProduto = extras.getInt("idProduto");
            this.favoritado = extras.getBoolean("favoritado");
            createView();
            conn = new VolleyConGET(this, this);
            conn.execute();
        }

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
            JSONObject produtoJson = (JSONObject) json.get("produto");
            JSONArray pessoasJson = json.getJSONArray("pessoas");
            pessoas = gson.fromJson(pessoasJson.toString(),  new TypeToken<ArrayList<Pessoa>>() {}.getType());
            produto = (Produto)gson.fromJson(produtoJson.toString(), Produto.class);
            loadView();
        }catch (Exception e){
            Toast.makeText(getBaseContext(), "Não foi possível recuperar as informações", Toast.LENGTH_SHORT).show();
            finish();

        }


    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl() + "Produto/getProdutoDetalhes/"+idProduto, "",new HashMap<String,String>() );
    }

    public void createView(){
        listView = (ListView)findViewById(R.id.listViewProdutoAvaliacao);
        header = getLayoutInflater().inflate(R.layout.activity_produto_detalhes_header, null);
        listView.addHeaderView(header);
        progressBar = (ProgressBar) findViewById(R.id.progressBarGeral);
        tvNomeProduto = (TextView)findViewById(R.id.tvNomeProduto);
        preco = (TextView)findViewById(R.id.tvPreco);
        descricao = (TextView)findViewById(R.id.tvDescricaoProduto);
        imagem = (ImageView)findViewById(R.id.ivPerfilProduto);
        scale = getResources().getDisplayMetrics().density;
        width = getResources().getDisplayMetrics().widthPixels - (int) (14 * scale + 0.5f);
        height = (width / 16) * 9;
        btnAvaliar = (Button)findViewById(R.id.btnAvaliarProduto);
        btnFavorito = (ToggleButton)findViewById(R.id.favorito);

    }

    public void loadView(){
        listView.setAdapter(new ProdutoAvaliacoesAdapter(this, produto.getAvaliacoes(), pessoas));
        tvNomeProduto.setText(produto.getNomeProduto());
        if(produto.getPreco()!=null) {
            preco.setText("R$" + String.format("%.2f", produto.getPreco().floatValue()));
        }
        descricao.setText(produto.getDescricao());

        Uri uri = Uri.parse(Url.URL_IMAGEM + produto.getImagemPerfil().getCaminho());
        // Resizing image using Fresco
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.ivPerfilProduto);
        draweeView.setImageURI(uri);

        btnAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAvaliacao();
            }
        });

        btnFavorito.setChecked(favoritado);
        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData sd = new SharedData(getBaseContext());
                Favorito favorito = new Favorito();
                //favorito.setIdFavorito();
                favorito.setCheck(btnFavorito.isChecked());
                favorito.setTipoFavoritado("produto");
                favorito.setIdFavoritado(idProduto);
                favorito.setIdPessoa(sd.getPessoaId());
                new FavoritarRequest(c, favorito);
            }
        });
    }

    public Bundle getBundleAvaliacao(int id){
        Bundle bundle = new Bundle();
        SharedData sharedData = new SharedData(this);
        Avaliacao avaliacao = new Avaliacao();
        if(produto.getAvaliacoes().size()>0){
            for(int i=0; i<produto.getAvaliacoes().size(); i++) {
                if (produto.getAvaliacoes().get(i).getPessoaid() == sharedData.getPessoaId()){
                    avaliacao = produto.getAvaliacoes().get(i);
                }
                else {
                    avaliacao.setAvaliadoid(id);
                    avaliacao.setPessoaid(sharedData.getPessoaId());
                    avaliacao.setTipoAvalicao(Avaliacao.PRODUTO);
                    avaliacao.setNota(avaliacao.getNota());
                    avaliacao.setDescricao(avaliacao.getDescricao());
                    i = produto.getAvaliacoes().size()+1;
                }
            }
        }else{
            avaliacao.setAvaliadoid(this.produto.getProdutoid());
            avaliacao.setPessoaid(sharedData.getPessoaId());
        }

        bundle.putParcelable("avaliacao", avaliacao);
        return bundle;
    }

    private void dialogAvaliacao() {
        FragmentManager fm = getSupportFragmentManager();
        avaliacaoDialogProdutoFragment.setArguments(getBundleAvaliacao(produto.getProdutoid()));
        avaliacaoDialogProdutoFragment.setCallBack(this);
        avaliacaoDialogProdutoFragment.show(fm, "asd");

    }


    @Override
    public void executeThis() {
        finish();
        startActivity(getIntent());
    }
}
