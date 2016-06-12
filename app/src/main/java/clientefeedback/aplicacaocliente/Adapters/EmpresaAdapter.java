package clientefeedback.aplicacaocliente.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.ImageHelper;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.Url;

/**
 * Created by Guilherme on 21/04/2016.
 */
public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.MyViewHolder> {
    private Context mContext;
    private List<Empresa> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;

    public EmpresaAdapter(Context c, List<Empresa> l) {
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_empresa_main, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNome.setText(mList.get(position).getNomeEmpresa());
        String local = mList.get(position).getEndereco().getCidade() + "/" + mList.get(position).getEndereco().getEstado();
        holder.tvLocalizacao.setText(local);
        holder.tvAvaliacao.setRating(mList.get(position).getNotaStars());   //setText(String.valueOf(mList.get(position).getAvaliacaoNota()));


        Uri uri = Uri.parse(Url.URL_IMAGEM + mList.get(position).getImagemPerfil().getCaminho());

        // Resizing image using Fresco
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(holder.draweeView.getController())
                    .setImageRequest(request)
                    .build();
            holder.draweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(Empresa e, int position){
        mList.add(position, e);
        notifyItemInserted(position);
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public ImageView ivEmpresa;
        public SimpleDraweeView draweeView;
        public TextView tvNome;
        public TextView tvLocalizacao;
        public RatingBar tvAvaliacao;
//        public ToggleButton favoritos;

        public MyViewHolder(View itemView) {
            super(itemView);

//            ivEmpresa = (ImageView) itemView.findViewById(R.id.iv_empresa);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_empresa);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tvLocalizacao = (TextView) itemView.findViewById(R.id.tv_localizacao);
            tvAvaliacao = (RatingBar) itemView.findViewById(R.id.ratingBar2);
//            favoritos = (ToggleButton) itemView.findViewById(R.id.btnFavorite);

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
