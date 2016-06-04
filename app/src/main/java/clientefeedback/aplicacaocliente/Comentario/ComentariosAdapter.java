package clientefeedback.aplicacaocliente.Comentario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 31/05/2016.
 */
public class ComentariosAdapter extends BaseAdapter{
    List<Comentario> comentarioList;
    List<Pessoa> pessoasList;
    List<Imagem> galeria;
    Context context;
    View view;

    public ComentariosAdapter(Context c, List<Comentario> comentaiosList, List<Pessoa> pessoasList, List<Imagem> galeria){
        this.comentarioList = comentaiosList;
        this.pessoasList = pessoasList;
        this.galeria = galeria;
        this.context = c;
    }

    @Override
    public int getCount() {
        return this.comentarioList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.comentarioList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.valueOf(this.comentarioList.get(i).getComentarioid());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Comentario comentario = comentarioList.get(i);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.item_comentario, null);

        if(pessoasList.size()>i) {
            Pessoa pessoa = pessoasList.get(i);

            TextView nomePessoa = (TextView) layout.findViewById(R.id.tvNome);
            nomePessoa.setText(pessoa.getNome());
        }

        TextView comentarioTexto = (TextView)layout.findViewById(R.id.tvComentario);
        comentarioTexto.setText(comentario.getDescricao());

        ImageView fotoPessoa = (ImageView)layout.findViewById(R.id.fotoPessoa);

        ImageButton iBtn1 = (ImageButton)layout.findViewById(R.id.iBtn1);
        ImageButton iBtn2 = (ImageButton)layout.findViewById(R.id.iBtn2);
        ImageButton iBtn3 = (ImageButton)layout.findViewById(R.id.iBtn3);
        ImageButton iBtn4 = (ImageButton)layout.findViewById(R.id.iBtn4);
        layout.requestFocus(R.id.imagemPerfil);
        return layout;
    }
}
