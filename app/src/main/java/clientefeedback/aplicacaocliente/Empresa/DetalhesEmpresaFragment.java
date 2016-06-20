package clientefeedback.aplicacaocliente.Empresa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import clientefeedback.aplicacaocliente.Avaliacao.AvaliacoesDetalhesRequest;
import clientefeedback.aplicacaocliente.Avaliacao.RequestAvaliacao;
import clientefeedback.aplicacaocliente.Avaliacao.TodasAvaliacoesActivity;
import clientefeedback.aplicacaocliente.Comentario.ComentarioDetalhesRequest;
import clientefeedback.aplicacaocliente.Comentario.ComentarioDialogFragment;
import clientefeedback.aplicacaocliente.Comentario.TodosComentariosActivity;
import clientefeedback.aplicacaocliente.Favorito.FavoritarRequest;
import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Comentario;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Favorito;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.Produto.CadastrarProdutoActivity;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ImageLoaderCustom;
import clientefeedback.aplicacaocliente.Services.Lista;
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
    private List<Favorito> favoritos;
    private Favorito favorito;
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
    TextView souDono;
    TextView todosComentarios;
    TextView todasAvaliacoes;
    ImageView imagemPerfil;
    Button avaliar;
    Button comentar;
    TextView notaAvaliacao;
    TextView comentarioAvaliacao;
    ImageLoader imageLoader;
    SharedData sharedData;
    CoordinatorLayout coordinatorLayout;
    boolean temDono;

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
        temDono = false;
        if (bundle != null) {
            avaliacao = bundle.getParcelable("avaliacao");
            empresa = bundle.getParcelable("empresa");
            favoritos = bundle.getParcelableArrayList("favoritos");
            if(empresa.getEntidade() != null){
                if(empresa.getEntidade().getIdcriador()>0) {
                    temDono = true;
                }
            }
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

        souDono = (TextView)rootView.findViewById(R.id.tvSouDono);
        souDono.setVisibility(View.GONE);
        if(temDono){
            souDono.setVisibility(View.VISIBLE);
        }
        souDono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SouDonoRequest(getContext(),empresa.getEmpresaId());
            }
        });

        nomeEmpresa = (TextView)rootView.findViewById(R.id.tvNome);
        nomeEmpresa.setText(empresa.getNomeEmpresa());

        descricao = (TextView)rootView.findViewById(R.id.tvDescricao);
        descricao.setText(empresa.getDescricao());

        numComentarios = (TextView)rootView.findViewById(R.id.tvNumeroComentarios);
        numComentarios.setText(String.valueOf(empresa.getQtdeComentarios()));

        culinaria = (TextView)rootView.findViewById(R.id.tvCulinaria);
        culinaria.setText(mountStringCulinaria());

        numAvaliacoes = (TextView)rootView.findViewById(R.id.tvNumeroAvaliacoes);
        numAvaliacoes.setText(String.valueOf(empresa.getQtdeAvaliacoes()));

        endereco = (TextView)rootView.findViewById(R.id.endereco);
        endereco.setText(mountStringAddress());

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
        }else{
            url = Url.URL_IMAGEM + "/images/sem_imagem.jpg";
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

        adicionarProduto = (TextView) rootView.findViewById(R.id.tvAdicionarProduto);
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
        if(!temDono) {
            adicionarProduto.setVisibility(View.GONE);
            adicionarProduto.setEnabled(false);
        }


        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });

        todosComentarios = (TextView)rootView.findViewById(R.id.tvTodosComentarios);
        todosComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TodosComentariosActivity.class);
                intent.putExtra("idEmpresa", empresa.getEmpresaId());
                startActivity(intent);
            }
        });

        todasAvaliacoes = (TextView)rootView.findViewById(R.id.tvTodasAvaliacoes);
        todasAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TodasAvaliacoesActivity.class);
                intent.putExtra("idEmpresa", empresa.getEmpresaId());
                startActivity(intent);
            }
        });

        btnFavorite.setChecked(isFavoritado());
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedData sd = new SharedData(getContext());

                //if(favorito == null) {
                //    favorito = getFavoritoEmpresa();
                    favorito = new Favorito();
                    favorito.setTipoFavoritado("empresa");
                    favorito.setIdFavoritado(empresa.getEmpresaId());
                    favorito.setIdPessoa(sd.getPessoaId());
                //}
                favorito.setCheck(btnFavorite.isChecked());
                new FavoritarRequest(getContext(), favorito);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loadComentarios();

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
        reload();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void loadComentarios(){
        ComentarioDetalhesRequest c = new ComentarioDetalhesRequest(getContext(), empresa.getEmpresaId());
        AvaliacoesDetalhesRequest a = new AvaliacoesDetalhesRequest(getContext(), empresa.getEmpresaId());
    }

    private String mountStringAddress(){
        String result = "";


        if(empresa.getEndereco().getRua() != null)
            result += empresa.getEndereco().getRua()+" ";

        if(empresa.getEndereco().getNumero() != null )
            result += empresa.getEndereco().getNumero()+" ";

        if(empresa.getEndereco().getBairro() != null)
            result += empresa.getEndereco().getBairro()+" ";

        if(empresa.getEndereco().getCidade() != null)
            result += empresa.getEndereco().getCidade()+" ";

        return result;
    }

    private String mountStringCulinaria(){
        Lista<Integer> culinarias = new Lista<>();
        String culinaria = "";
        for(int i=0; i<empresa.getProdutos().size(); i++){
            if(!culinarias.contains(empresa.getProdutos().get(i).getCulinaria())){
                culinarias.add(empresa.getProdutos().get(i).getCulinaria());
                if(culinarias.size() > 1){
                    culinaria += ", ";
                }
                culinaria += getResources().getStringArray(R.array.tipo_cozinhas)[i];
            }
        }
        return culinaria;

    }

    public void reload(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }

    //verifica se esta favoritado
    private boolean isFavoritado() {
        if(getFavoritoEmpresa() != null) {
            if (getFavoritoEmpresa().isCheck()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //pegar se tem um favoritado para a empresa dessa pessoa
    private Favorito getFavoritoEmpresa(){
        favorito = new Favorito();
        if(this.favoritos != null) {
            for (int i = 0; i < this.favoritos.size(); i++) {
                if ("empresa".equals(this.favoritos.get(i).getTipoFavoritado()) && this.favoritos.get(i).getIdFavoritado() == empresa.getEmpresaId()) {
                    favorito = this.favoritos.get(i);
                    return favorito;
                }
            }
        }
        return favorito;
    }

}