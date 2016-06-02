package clientefeedback.aplicacaocliente.Produto;

import android.content.Context;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Url;

/**
 * Created by Alexandre on 04/05/2016.
 */
public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.MyViewHolder> {
    private Context mContext;
    private List<Produto> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;
    private ImageLoader imageLoader;

    public ProdutoAdapter(Context c, List<Produto> l) {
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
        imageLoader = ImageLoaderCustom.getImageloader(mContext);


//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
//                .threadPoolSize(5) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)
//                .writeDebugLogs()
//                .build();
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_produto, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNome.setText(mList.get(position).getNomeProduto());
        holder.tvDescricao.setText(mList.get(position).getDescricao());
        holder.ratingBar.setRating(4);

        mList.get(position).getImagemPerfil().getCaminho();
        //String url = Url.IP+"ServidorAplicativo/images/teste_1.jpg";
        String url = Url.IP + mList.get(position).getImagemPerfil().getCaminho()+mList.get(position).getImagemPerfil().getNomeImagem();
        ImageView iv = holder.ivProduto;

        imageLoader.displayImage(url, iv);


//        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mList.get(position).getPhoto());
//        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//
//        bitmap = ImageHelper.getRoundedCornerBitmap(mContext, bitmap, 4, width, height, false, false, true, true);
//        holder.ivProduto.setImageBitmap(bitmap);
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


        public MyViewHolder(View itemView) {
            super(itemView);

            ivProduto = (ImageView) itemView.findViewById(R.id.iv_produto);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tvDescricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            ivProduto = (ImageView) itemView.findViewById(R.id.iv_produto);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);



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

