package clientefeedback.aplicacaocliente.Empresa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clientefeedback.aplicacaocliente.Busca.BuscaEmpresaAdapter;
import clientefeedback.aplicacaocliente.Busca.BuscaRequest;
import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Filtro;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.TabPagerItem;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.ViewPagerAdapter;
import clientefeedback.aplicacaocliente.ViewPagerFragment;
import clientefeedback.aplicacaocliente.VolleyConn;

/**
 * Created by Alexandre on 25/04/2016.
 */
public class PrincipalEmpresaFragment extends Fragment{
    private List<TabPagerItem> mTabs = new ArrayList<>();
    private Empresa empresa;
    Bundle bundle = new Bundle();
    boolean notTrue = false;
    boolean mSearchCheck = false;


    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        if (bundle != null) {
            empresa = bundle.getParcelable("empresa");
        }
        createTabPagerItem();

    }

    private void createTabPagerItem(){
        Intent intent = new Intent();
        intent.putExtras(bundle);

        Fragment detalhes = DetalhesEmpresaFragment.newInstance("empresa");
        detalhes.setArguments(bundle);
        mTabs.add(new TabPagerItem("Detalhes", detalhes));

        Fragment cardapio = CardapioFragment.newInstance("cardapio");
        cardapio.setArguments(bundle);
        mTabs.add(new TabPagerItem("Cardápio", cardapio));

        Fragment programacao = ProgramacaoEmpresaFragment.newInstance("programacao");
        programacao.setArguments(bundle);
        mTabs.add(new TabPagerItem("Programação", programacao));

        Fragment ambiente = AmbienteEmpresaFragment.newInstance("ambiente");

        mTabs.add(new TabPagerItem("ambiente", ambiente));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_principal_empresa, container, false);

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //PagerTitleStrip pagerTitleStrip = (PagerTitleStrip)rootView.findViewById(R.id.titlestrip);
        HorizontalScrollView scrollView = new HorizontalScrollView(rootView.getContext());

        Intent intent = new Intent();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(mTabs.size());
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), mTabs));
        TabLayout mSlidingTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSlidingTabLayout.setElevation(15);
        }
        mSlidingTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Busca");

        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(onQuerySearchView);

//        menu.findItem(R.id.menu_add).setVisible(true);
        mSearchCheck = false;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            new BuscaRequest(getContext(), s);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };
}