package clientefeedback.aplicacaocliente.Empresa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogFragment;
import clientefeedback.aplicacaocliente.Avaliacao.RequestAvaliacao;
import clientefeedback.aplicacaocliente.Comentario.ComentarioDetalhesRequest;
import clientefeedback.aplicacaocliente.Comentario.ComentarioDialogFragment;
import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Produto.CadastrarProdutoActivity;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.TabPagerItem;


/**
 * Created by Alexandre on 25/04/2016.
 */
public class DetalhesEmpresaFragment extends PrincipalEmpresaFragment{
    private List<TabPagerItem> mTabs = new ArrayList<>();
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private Empresa empresa;
    private Avaliacao avaliacao;
    ToggleButton btnFavorite;
    TextView nomeEmpresa;
    TextView avaliacoes;
    TextView numCheckins;
    TextView numComentarios;
    TextView numAvaliacoes;
    TextView culinaria;
    TextView endereco;
    TextView telefone;
    TextView descricao;
    TextView adicionarProduto;
    ImageView imagemPerfil;
    Button avaliar;
    Button comentar;
    TextView notaAvaliacao;
    TextView comentarioAvaliacao;
    ImageLoader imageLoader;
    SharedData sharedData;
    CoordinatorLayout coordinatorLayout;

    LinearLayout areaAvaliacao;
    ImageButton btnEditarAvaliacao;



    public static DetalhesEmpresaFragment newInstance(String text){
        DetalhesEmpresaFragment mFragment = new DetalhesEmpresaFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedData = new SharedData(getContext());
        imageLoader = ImageLoaderCustom.getImageloader(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            avaliacao = bundle.getParcelable("avaliacao");
            empresa = bundle.getParcelable("empresa");

        }
        createTabPagerItem();
    }

    private void createTabPagerItem(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalhes_empresa, container, false);
        btnFavorite = (ToggleButton)rootView.findViewById(R.id.btnFavorite);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));

        coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.main_content);

        nomeEmpresa = (TextView)rootView.findViewById(R.id.tvNome);
        nomeEmpresa.setText(empresa.getNomeEmpresa());

        numComentarios = (TextView)rootView.findViewById(R.id.tvNumeroComentarios);
        numComentarios.setText(String.valueOf(empresa.getQtdeComentarios()));

        numAvaliacoes = (TextView)rootView.findViewById(R.id.tvNumeroAvaliacoes);
        numAvaliacoes.setText(String.valueOf(empresa.getQtdeAvaliacoes()));

        endereco = (TextView)rootView.findViewById(R.id.endereco);
        endereco.setText(empresa.getEndereco().getRua() + ", " + empresa.getEndereco().getNumero() + ", " + empresa.getEndereco().getBairro());

        btnEditarAvaliacao = (ImageButton)rootView.findViewById(R.id.btnEditarAvaliacao);
        btnEditarAvaliacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogAvaliacao();
            }
        });

        String tel = "";
        for(int i=0; i<empresa.getTelefones().size(); i++ ){
            if(empresa.getTelefones().get(i).getNumero() != "null") {
                tel = tel + " " + empresa.getTelefones().get(i).getNumero();
                if( i+1<empresa.getTelefones().size()){
                    tel = tel+", ";
                }
            }

        }
        telefone = (TextView)rootView.findViewById(R.id.telefone);
        telefone.setText(tel);

        imagemPerfil = (ImageView)rootView.findViewById(R.id.imagemPerfil);
        String url = null;
        if(empresa.hasImagemPerfil()) {
            url = Url.URL_IMAGEM + empresa.getImagemPerfil().getCaminho();
        }
        imageLoader.displayImage(url, imagemPerfil);

        avaliar = (Button)rootView.findViewById(R.id.btnAvaliar);
        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogAvaliacao();
            }
        });

        comentar = (Button)rootView.findViewById(R.id.btnComentar);
        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogComentar();
            }
        });

        notaAvaliacao = (TextView)rootView.findViewById(R.id.nota);
        comentarioAvaliacao = (TextView)rootView.findViewById(R.id.comentario);

        areaAvaliacao = (LinearLayout) rootView.findViewById(R.id.areaAvaliacao);
        areaAvaliacao.setVisibility(View.GONE);
        avaliar.setVisibility(View.VISIBLE);
        if(avaliacao.getNota() > 0 && avaliacao.getDescricao()!="") {
            areaAvaliacao.setVisibility(View.VISIBLE);
            avaliar.setVisibility(View.GONE);
            notaAvaliacao.setText(String.valueOf(avaliacao.getNota()));
            comentarioAvaliacao.setText(avaliacao.getDescricao());
        }

        adicionarProduto = (TextView)rootView.findViewById(R.id.tvAdicionarProduto);
        adicionarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("id", empresa.getEmpresaId());
                Intent it = new Intent(getContext(), CadastrarProdutoActivity.class);
                it.putExtras(b);
                startActivity(it);
            }
        });

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loadComentarios();
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Favorito clicado", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public Bitmap getImageFromBase64(String img){
        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public Bundle getBundleAvaliacao(){
        Bundle bundle = new Bundle();
        SharedData sharedData = new SharedData(getContext());
        //Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAvaliadoid(empresa.getEmpresaId());
        avaliacao.setPessoaid(sharedData.getPessoaId());
        avaliacao.setTipoAvalicao(Avaliacao.EMPRESA);
        avaliacao.setAvaliadoid(empresa.getEmpresaId());
        bundle.putParcelable("avaliacao", avaliacao);
        return bundle;
    }

    public Bundle getBundleComentario(){
        Bundle bundle = new Bundle();
        Comentario comentario = new Comentario();
        comentario.setComentadoid(empresa.getEmpresaId());
        comentario.setPessoaid(sharedData.getPessoaId());
        comentario.setTipoComentado(Comentario.EMPRESA);
        bundle.putParcelable("comentario", comentario);
        return bundle;
    }

    private void loadDialogAvaliacao(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AvaliacaoDialogFragment avaliacaoDialogFragment = new AvaliacaoDialogFragment();
        avaliacaoDialogFragment.setArguments(getBundleAvaliacao());
        avaliacaoDialogFragment.setTargetFragment(this, 1);
        avaliacaoDialogFragment.show(ft, "dialog");
    }

    private void loadDialogComentar(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ComentarioDialogFragment comentarioDialogFragment = new ComentarioDialogFragment();
        comentarioDialogFragment.setArguments(getBundleComentario());
        comentarioDialogFragment.setTargetFragment(this, 1);
        comentarioDialogFragment.show(ft, "dialog");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // Stuff to do, dependent on requestCode and resultCode
        if(requestCode == 1)  // 1 is an arbitrary number, can be any int
        {
            if(resultCode == 1) // 1 is an arbitrary number, can be any int
            {
                (new RequestAvaliacao(getContext(),getView(),avaliacao.getAvaliacaoid())).execute();
                areaAvaliacao.setVisibility(View.VISIBLE);
                avaliar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadComentarios(){
        ComentarioDetalhesRequest c = new ComentarioDetalhesRequest(getContext(), empresa.getEmpresaId());



    }


}