package clientefeedback.aplicacaocliente.Busca;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import java.util.List;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.Url;


public class BuscaEmpresaAdapter extends RecyclerView.Adapter<BuscaEmpresaAdapter.MyViewHolder> {
    private Context mContext;
    private List<Empresa> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;

    public BuscaEmpresaAdapter(Context c, List<Empresa> l) {
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_busca_empresa, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvNome.setText(mList.get(position).getNomeEmpresa());
        holder.tvDescricao.setText(mList.get(position).getDescricao());
        Integer nota = mList.get(position).getAvaliacaoNota();
        Float notaFloat = nota.floatValue();
        notaFloat = (notaFloat/10)/2;
        holder.ratingBar.setRating(notaFloat);


        Uri uri = Uri.parse(Url.IP + "ServidorAplicativo/" + mList.get(position).getImagemPerfil().getCaminho());

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

    public long getItemId(int position) {
        return mList.get(position).getEmpresaId();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivEmpresa;
        public TextView tvNome;
        public TextView tvDescricao;
        public RatingBar ratingBar;
        public SimpleDraweeView draweeView;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivEmpresa = (ImageView) itemView.findViewById(R.id.iv_empresa);
            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tvDescricao = (TextView) itemView.findViewById(R.id.tv_descricao);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarBusca);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_empresa);
            draweeView.setAnimation(new RotateAnimation(1,100));

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

