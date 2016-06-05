package clientefeedback.aplicacaocliente.Produto;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogFragment;
import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaFragment;
import clientefeedback.aplicacaocliente.Interfaces.OnItemClickListener;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 04/05/2016.
 */
public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder>{
    private Context mContext;
    private List<Produto> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;
    private ImageLoader imageLoader;
    private FragmentManager fm;
    private OnItemClickListener listener;
    private int id;
    Transaction t;

    public ProdutoAdapter(Activity c, List<Produto> l, OnItemClickListener listener) {
        this.listener = listener;
        this.fm = fm;
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
        imageLoader = ImageLoaderCustom.getImageloader(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_produto, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvNome.setText(mList.get(position).getNomeProduto());
        holder.tvDescricao.setText(mList.get(position).getDescricao());
        if(mList.get(position).getAvaliacaoGeral()>0) {
            holder.ratingBar.setRating(mList.get(position).getAvaliacaoGeral());
        }
        holder.preco.setText("R$" + String.format("%.2f", mList.get(position).getPreco().floatValue()));
        String url;




        ImageView iv = holder.ivProduto;
        url = Url.URL_IMAGEM + "/images/sem_imagem.jpg";
        imageLoader.displayImage(url, iv);
        url = Url.URL_IMAGEM + mList.get(position).getImagemPerfil().getCaminho();
        imageLoader.displayImage(url, iv);

        this.id = mList.get(position).getProdutoid();
        holder.avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, String.valueOf(mList.get(position).getProdutoid()), Toast.LENGTH_SHORT).show();
                listener.onItemClicked(view, mList.get(position).getProdutoid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(Produto e, int position){
        mList.add(position, e);
        notifyItemInserted(position);
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProduto;
        public TextView tvNome;
        public TextView tvDescricao;
        public RatingBar ratingBar;
        public TextView preco;
        public Button avaliar;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivProduto = (ImageView) itemView.findViewById(R.id.iv_produto);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tvDescricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            ivProduto = (ImageView) itemView.findViewById(R.id.iv_produto);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            preco = (TextView) itemView.findViewById(R.id.preco);
            avaliar = (Button) itemView.findViewById(R.id.btnAvaliarProduto);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }



}

