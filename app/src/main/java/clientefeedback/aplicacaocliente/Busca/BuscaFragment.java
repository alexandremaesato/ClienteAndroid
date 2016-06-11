package clientefeedback.aplicacaocliente.Busca;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Filtro;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.ConnectionVerify;
import clientefeedback.aplicacaocliente.Services.SnackMessage;
import clientefeedback.aplicacaocliente.Services.SnackMessageInterface;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;


@SuppressLint("ValidFragment")
public class BuscaFragment extends Fragment implements Transaction,RecyclerViewOnClickListenerHack, SnackMessageInterface {
    static FragmentManager fragmentManager;
    private Context c = getContext();
    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "Busca";
    private ProgressBar progressBar;
    List<Empresa> empresas= new ArrayList<Empresa>();
    String stringBusca = "";

    BuscaEmpresaAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<Empresa> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedData sharedData;
    int pos = 0;


    public BuscaFragment (FragmentManager f){
        this.fragmentManager = f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        final View rootView = inflater.inflate(R.layout.fragment_busca, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.pbProxy);

        (new VolleyConn(getContext(), this)).execute();

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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_busca);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                BuscaEmpresaAdapter adapter = (BuscaEmpresaAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<Empresa> listAux = getSetEmpresaList(5);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mList = getSetEmpresaList(6);
        adapter = new BuscaEmpresaAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);

        mSwipeRefreshLayout =(SwipeRefreshLayout) rootView.findViewById(R.id.srl_swipe_busca);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(ConnectionVerify.verifyConnection(getActivity())){
                    BuscaEmpresaAdapter adapter = (BuscaEmpresaAdapter) mRecyclerView.getAdapter();
                    List<Empresa> listAux = getSetEmpresaList(5);
                    for(int i = 0; i < listAux.size(); i++){
                        adapter.addListItem( listAux.get(i), 0 );
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                } else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    Snackbar snackbar = Snackbar
                            .make(rootView, R.string.connection_swipe, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.connect, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(it);
                                }
                            });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });


        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_busca, menu);

        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Busca");

        ((EditText) searchView.findViewById(R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(onQuerySearchView);

        menu.findItem(R.id.menu_add).setVisible(true);

        mSearchCheck = false;

        final MenuItem menuFilter = menu.findItem(R.id.menu_filter);
        menuFilter.setVisible(true);

        menuFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_search:
                mSearchCheck = true;

                //Toast.makeText(getActivity(), "Busca" , Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            sharedData = new SharedData(getContext());
            sharedData.setSearch(s);
            executeSearch();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck){
                // implement your search here
            }
            return false;
        }
    };


    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void doAfter(String answer) {
        try{
            System.out.println("============ RESULTADO DA BUSCA:=============="+ answer);
            JSONObject json = new JSONObject(answer);
            JSONArray empresasJson = json.getJSONArray("Empresas");
            Gson gson = new Gson();

            empresas = gson.fromJson(empresasJson.toString(),  new TypeToken<ArrayList<Empresa>>() {
            }.getType());

            if (empresas.size() > -1) {
                pos = 0;
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(llm);
                mList = getSetEmpresaList(6);
                adapter = new BuscaEmpresaAdapter(getActivity(), mList);
                adapter.setRecyclerViewOnClickListenerHack(this);
                mRecyclerView.setAdapter(adapter);

            }else{

            }
        }
        catch(Exception e){
            e.printStackTrace();
            new SnackMessage(this).snackShowError(getView());
        }
        finally {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public RequestData getRequestData() {
        return( new RequestData(Url.getUrl()+"Empresa/buscarEmpresas", "", getParams()) );
    }

    public Map<String,String> getParams() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getContext().getSharedPreferences("filtro", Context.MODE_PRIVATE);
        Filtro filtro = new Filtro();
        Gson gson = new Gson();
        Map<String,String> lista = new HashMap<String,String>();
        sharedData = new SharedData(getContext());
        filtro.setNomeempresa(sharedData.getSearch());
        filtro.setCulinaria(sharedPreferences.getString("culinaria", ""));
        filtro.setEstado(sharedPreferences.getString("estado", ""));
        filtro.setCidade(sharedPreferences.getString("cidade", ""));
        filtro.setBairro(sharedPreferences.getString("bairro", ""));
        filtro.setValorMinimo(String.valueOf(sharedPreferences.getInt("valorMinimo", 0)));
        filtro.setValorMaximo(String.valueOf(sharedPreferences.getInt("valorMaximo", 500)));
        filtro.setOrdenacao(sharedPreferences.getString("ordenacao", ""));
        lista.put("filtro", gson.toJson(filtro));
        return lista;
    }

    @Override
    public void onClickListener(View view, int position) {
        BuscaEmpresaAdapter adapter = (BuscaEmpresaAdapter) mRecyclerView.getAdapter();
        new CarregaEmpresaRequest(getView(), getContext(), fragmentManager, (int)adapter.getItemId(position));
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
    }

    public List<Empresa> getSetEmpresaList(int qtd){
        List<Empresa> lista = new ArrayList<>();
        int size = empresas.size();

        for(int i=0; i < qtd; i++) {
            if (pos < size){
                lista.add(empresas.get(pos));
            }else{
                i = qtd+1;
            }
            pos++;
        }

        return(lista);
    }

    @Override
    public void executeAfterMessage() {
        (new VolleyConn(getContext(), this)).execute();

    }

    private void executeSearch(){
        (new VolleyConn(getContext(), this)).execute();
    }




}
