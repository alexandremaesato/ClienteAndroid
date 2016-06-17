package clientefeedback.aplicacaocliente.Comentario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 16/06/2016.
 */
public class TodosComentariosAdapter extends BaseAdapter {
    Context context;
    List<Comentario> comentarios;
    List<Pessoa> pessoas;

    public TodosComentariosAdapter(Context context, List<Comentario> comentarios, List<Pessoa> pessoas){
        this.context = context;
        this.comentarios = comentarios;
        this.pessoas = pessoas;
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int i) {
        return comentarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Comentario comentario = comentarios.get(i);

        LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.item_comentario, null);

        TextView nomePessoa = (TextView)layout.findViewById(R.id.tvNome);
        if(comentarios.get(i).getPessoa().getNome() != null){
            nomePessoa.setText(comentarios.get(i).getPessoa().getNome());
        }

        TextView comentariotv = (TextView)layout.findViewById(R.id.tvComentario);
        if(comentario.getDescricao() != null){
           if (comentario.getDescricao() == ""){
               comentariotv.setText("Sem texto!");
           }else {
               comentariotv.setText(comentario.getDescricao());
           }
        }


        TextView data = (TextView)layout.findViewById(R.id.tvData);
        if(comentario.getData_modificacao() != null) {
            data.setText(comentario.getData_modificacao().toString());
        }

//        RatingBar rb = (RatingBar)layout.findViewById(R.id.ratingBarAvaliacaoProduto);
//        Integer nota = comentario.getNota();
//        Float notaFloat = nota.floatValue();
//        notaFloat = (notaFloat/10)/2;
//        rb.setRating(notaFloat);

        return layout;
    }
}
