package clientefeedback.aplicacaocliente;

import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Adapters.EmpresaAdapter;
import clientefeedback.aplicacaocliente.Busca.BuscaEmpresaAdapter;
import clientefeedback.aplicacaocliente.Busca.BuscaRequest;
import clientefeedback.aplicacaocliente.Busca.CarregaEmpresaRequest;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.ConnectionVerify;
import clientefeedback.aplicacaocliente.Services.SnackMessage;
import clientefeedback.aplicacaocliente.Services.SnackMessageInterface;
import clientefeedback.aplicacaocliente.Services.Url;


public class MainFragment extends Fragment implements RecyclerViewOnClickListenerHack, SnackMessageInterface {

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private RecyclerView mRecyclerView;
    private List<Empresa> mList;
    private RequestQueue rq;
    private ProgressBar mPbLoad;
    private Gson gson;
    private boolean isLastItem;
    private View rootView;
    boolean boolLoadWifi = false;
    List<Empresa> empresas;
    int next = 0;


    public static MainFragment newInstance(String text){
        MainFragment mFragment = new MainFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rq = Volley.newRequestQueue(getActivity());
        mPbLoad = (ProgressBar) rootView.findViewById(R.id.pb_load);
        gson = new Gson();
        mList = new ArrayList<>();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list);
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
                EmpresaAdapter adapter = (EmpresaAdapter) mRecyclerView.getAdapter();

                if (!isLastItem && mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    execute();
                }

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        EmpresaAdapter adapter = new EmpresaAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        getFragmentManager().beginTransaction().addToBackStack("main").commit();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mList == null || mList.size() == 0){
            callVolleyRequest();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        callVolleyRequest();

    }


    public void callVolleyRequest(){
        execute();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

//            case R.id.menu_add:
//                Toast.makeText(getActivity(), getFragmentManager().getFragments().get(0).toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
//
//                break;

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
            new BuscaRequest(MainActivity.contextOfApplication, s);
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

    @Override //CLICK NA EMPRESA
    public void onClickListener(View view, int position) {
        EmpresaAdapter adapter = (EmpresaAdapter) mRecyclerView.getAdapter();
        new CarregaEmpresaRequest(getContext(), getFragmentManager(), empresas.get(position).getEmpresaId());
//        Toast.makeText(getActivity(), "Position: "+position, Toast.LENGTH_SHORT).show();
//        EmpresaAdapter adapter = (EmpresaAdapter) mRecyclerView.getAdapter();
//        adapter.removeListItem(position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {}

    public void execute(){
        final Empresa empresa = doBefore();
        String url = Url.getUrl()+"Empresa/carregarEmpresas/"+next;
        next = next+10;

        StringRequest request = new AutorizacaoRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    public void onResponse(String result) {
                        doAfter(result);
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 401) {
                                mPbLoad.setVisibility(View.GONE);
                            }
                        }else {
                            mPbLoad.setVisibility(View.GONE);
                            snackShow();
                        }
                    }
                });

        request.setTag(MainFragment.class.getName());
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rq.add(request);
    }

    public Empresa doBefore() {
        mPbLoad.setVisibility(View.VISIBLE);
        Empresa empresa = new Empresa();

        if( ConnectionVerify.verifyConnection(getActivity()) ){

            if( mList != null && mList.size() > 0 ){
                empresa.setAvaliacaoNota(mList.get(mList.size() - 1).getAvaliacaoNota());
            } else{
                empresa.setAvaliacaoNota(-1);
            }
        } else{
            mPbLoad.setVisibility(View.GONE);
            boolLoadWifi = true;
           // new SnackMessage(this).snackShowError(rootView);
            snackShow();
//            Snackbar snackbar = Snackbar
//                    .make(getView(), R.string.connection_swipe, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.connect, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                            startActivity(it);
//                        }
//                    });
//            snackbar.setActionTextColor(Color.YELLOW);
//            snackbar.show();
        }
        return empresa;
    }

    public void doAfter(String json) {
        mPbLoad.setVisibility(View.GONE);

        if( json != null ){
            EmpresaAdapter adapter = (EmpresaAdapter) mRecyclerView.getAdapter();
            Gson gson = new Gson();
            int position;

            try{
                Type type = new TypeToken<List<Empresa>>(){}.getType();
                empresas = gson.fromJson(json, type);
                for(int i = 0, tamI = empresas.size(); i < tamI; i++){
                    Empresa empresa = empresas.get(i);
                    position = mList.size();
                    adapter.addListItem(empresa, position);
                }

                if( json.length() == 0 ){
                    isLastItem = true;
                }

            }
            catch(Exception e){
                Toast.makeText(getActivity(), "Algo aqui", Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(getActivity(), "Falhou. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public void snackShow(){
        if(boolLoadWifi){
            new SnackMessage(this).snackShowErrorWifi(rootView);
        }else{
            new SnackMessage(this).snackShowError(rootView);
        }

    }

    @Override
    public void executeAfterMessage() {
        execute();
    }

    @Override
    public void executeAfterMessageWifi() {
        Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(it);
    }
}
