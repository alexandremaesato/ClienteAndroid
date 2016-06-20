package clientefeedback.aplicacaocliente.Favorito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Url;

/**
 * Created by Alexandre on 19/06/2016.
 */
public class TodosFavoritosAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    Context context;
    List<Produto> produtos;
    List<Empresa> empresas;
    List<Favorito> favoritos;
    Map<Integer, Empresa> mapEmpresas = new HashMap<Integer, Empresa>();
    Map<Integer, Produto> mapProdutos = new HashMap<Integer, Produto>();


    public TodosFavoritosAdapter(Context context, List<Produto> produtos, List<Empresa> empresas, List<Favorito> favoritos) {
        this.context = context;
        this.produtos = produtos;
        this.empresas = empresas;
        this.favoritos = favoritos;
        imageLoader = ImageLoaderCustom.getImageloader(context);
        empresasToMap();
        produtosToMap();
    }

    @Override
    public int getCount() {
        return this.favoritos.size();
    }

    @Override
    public Object getItem(int i) {
        return this.favoritos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.favoritos.get(i).getIdFavorito();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.item_favoritado, null);

        TextView nomeItem = (TextView)layout.findViewById(R.id.tvNome);
        TextView tipoFavoritado = (TextView)layout.findViewById(R.id.tvTipoFavoritado);
        TextView descricao = (TextView)layout.findViewById(R.id.tvDescricao);
        ImageView ivFavoritado = (ImageView) layout.findViewById(R.id.ivFavoritado);
        RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.ratingBar);
        TextView preco = (TextView) layout.findViewById(R.id.tvPreco);
        TextView textPreco = (TextView) layout.findViewById(R.id.textPreco);

        if("empresa".equals(favoritos.get(i).getTipoFavoritado())) {
            tipoFavoritado.setText("Empresa");
            Empresa empresa = ((Empresa) mapEmpresas.get(favoritos.get(i).getIdFavoritado()));
            nomeItem.setText(empresa.getNomeEmpresa());
            descricao.setText(empresa.getDescricao());
            ratingBar.setRating(empresa.getNotaStars());
            textPreco.setVisibility(View.GONE);


            String url;
            if (empresa.hasImagemPerfil()){
                url = Url.URL_IMAGEM + empresa.getImagemPerfil().getCaminho();
            }else{
                url = Url.URL_IMAGEM + "/images/sem_imagem.jpg";
            }
            imageLoader.displayImage(url, ivFavoritado);

        }else{
            tipoFavoritado.setText("Produto");
            Produto produto = ((Produto)mapProdutos.get(favoritos.get(i).getIdFavoritado()));
            nomeItem.setText( produto.getNomeProduto() );
            descricao.setText( produto.getDescricao() );
            ratingBar.setRating(produto.getNotaStars());
            preco.setText("R$" + String.format("%.2f", produto.getPreco().floatValue()));

            String url;
            if (produto.hasImagemPerfil()){
                url = Url.URL_IMAGEM + produto.getImagemPerfil().getCaminho();
            }else{
                url = Url.URL_IMAGEM + "/images/sem_imagem.jpg";
            }
            imageLoader.displayImage(url, ivFavoritado);
        }


        return layout;
    }

    private void empresasToMap(){
        for(int i =0; i<empresas.size(); i++){
            mapEmpresas.put(empresas.get(i).getEmpresaId(), empresas.get(i));
        }
    }

    private void produtosToMap(){
        for(int i =0; i<produtos.size(); i++){
            mapProdutos.put(produtos.get(i).getProdutoid(), produtos.get(i));
        }
    }


}
