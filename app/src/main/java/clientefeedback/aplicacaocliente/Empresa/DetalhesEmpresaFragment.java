package clientefeedback.aplicacaocliente.Empresa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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
import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
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
    ImageView imagemPerfil;
    TextView avaliar;
    TextView notaAvaliacao;
    TextView comentarioAvaliacao;
    ImageLoader imageLoader;

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

        nomeEmpresa = (TextView)rootView.findViewById(R.id.tvNome);
        nomeEmpresa.setText(empresa.getNomeEmpresa());

        numComentarios = (TextView)rootView.findViewById(R.id.tvNumeroComentarios);
        numComentarios.setText(String.valueOf(empresa.getComentarios().size()));

        numAvaliacoes = (TextView)rootView.findViewById(R.id.tvNumeroAvaliacoes);
        numAvaliacoes.setText(String.valueOf(empresa.getAvaliacoes().size()));

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
            url = Url.url + empresa.getImagemPerfil().getCaminho();
        }
        imageLoader.displayImage(url, imagemPerfil);

        avaliar = (TextView)rootView.findViewById(R.id.tvAvaliar);
        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialogAvaliacao();
            }
        });

        notaAvaliacao = (TextView)rootView.findViewById(R.id.nota);
        comentarioAvaliacao = (TextView)rootView.findViewById(R.id.comentario);

        areaAvaliacao = (LinearLayout) rootView.findViewById(R.id.areaAvaliacao);
        areaAvaliacao.setVisibility(View.GONE);
        avaliar.setVisibility(View.VISIBLE);
        if(avaliacao.getNota() > 0 || avaliacao.getDescricao()!="") {
            areaAvaliacao.setVisibility(View.VISIBLE);
            avaliar.setVisibility(View.GONE);
            notaAvaliacao.setText(String.valueOf(avaliacao.getNota()));
            comentarioAvaliacao.setText(avaliacao.getDescricao());
        }

        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        } );

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

    private void loadDialogAvaliacao(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AvaliacaoDialogFragment avaliacaoDialogFragment = new AvaliacaoDialogFragment();
        avaliacaoDialogFragment.setArguments(getBundleAvaliacao());
        avaliacaoDialogFragment.setTargetFragment(this, 1);
        avaliacaoDialogFragment.show(ft, "dialog");

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
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}