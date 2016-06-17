package clientefeedback.aplicacaocliente.Empresa;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Busca.BuscaRequest;
import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.Produto.CadastrarProdutoActivity;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.TabPagerItem;
import clientefeedback.aplicacaocliente.ViewPagerAdapter;

/**
 * Created by Alexandre on 14/06/2016.
 */
public class ProgramacaoEmpresaFragment extends PrincipalEmpresaFragment {
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

    public static ProgramacaoEmpresaFragment newInstance(String text){
        ProgramacaoEmpresaFragment mFragment = new ProgramacaoEmpresaFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if (bundle != null) {
        //    empresa = bundle.getParcelable("empresa");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_programacao_empresa, container, false);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
