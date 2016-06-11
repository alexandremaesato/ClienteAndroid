package clientefeedback.aplicacaocliente.Produto;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 09/06/2016.
 */
public class ProdutoAvaliacoesAdapter extends BaseAdapter{
    Context context;
    List<Avaliacao> avaliacoes;

    public ProdutoAvaliacoesAdapter(Context context, List<Avaliacao> avaliacoes){
        this.context = context;
        this.avaliacoes = avaliacoes;
    }


    @Override
    public int getCount() {
        return avaliacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return avaliacoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Avaliacao avaliacao = avaliacoes.get(i);

        LayoutInflater layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.item_avaliacao_produto, null);

        TextView nomePessoa = (TextView)layout.findViewById(R.id.tvNomePessoa);

        TextView comentario = (TextView)layout.findViewById(R.id.tvComentarioProduto);
        comentario.setText(avaliacao.getDescricao());

        TextView data = (TextView)layout.findViewById(R.id.tvDataAvaliacao);
        if(avaliacao.getData_modificacao() != null) {
            data.setText(avaliacao.getData_modificacao().toString());
        }

        RatingBar rb = (RatingBar)layout.findViewById(R.id.ratingBarAvaliacaoProduto);
        Integer nota = avaliacao.getNota();
        Float notaFloat = nota.floatValue();
        notaFloat = (notaFloat/10)/2;
        rb.setRating(notaFloat);

        return layout;
    }
}
