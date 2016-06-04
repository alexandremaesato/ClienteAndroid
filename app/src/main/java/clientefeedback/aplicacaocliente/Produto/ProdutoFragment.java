package clientefeedback.aplicacaocliente.Produto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Empresa.CadastrarEmpresaActivity;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.ConnectionVerify;

/**
 * Created by Alexandre on 04/05/2016.
 */
public class ProdutoFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private RecyclerView mRecyclerView;
    private List<Produto> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Empresa empresa;
    Bundle bundle;
    int pos = 0;

    public static ProdutoFragment newInstance(String text){
        ProdutoFragment mFragment = new ProdutoFragment();
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
            empresa = bundle.getParcelable("empresa");
        }
        mList = getSetProdutoList(5);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        final View rootView = inflater.inflate(R.layout.fragment_produto, container, false);
        if(!empresa.produtosIsEmpty()) {
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
                    ProdutoAdapter adapter = (ProdutoAdapter) mRecyclerView.getAdapter();

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                        List<Produto> listAux = getSetProdutoList(5);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }
                    }
                }
            });

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);

//            mList = getSetProdutoList(5);
            ProdutoAdapter adapter = new ProdutoAdapter(getActivity(), mList);
            adapter.setRecyclerViewOnClickListenerHack(this);
            mRecyclerView.setAdapter(adapter);

            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_swipe);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if (ConnectionVerify.verifyConnection(getActivity())) {
                        ProdutoAdapter adapter = (ProdutoAdapter) mRecyclerView.getAdapter();
                        List<Produto> listAux = getSetProdutoList(5);
                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), 0);
                            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
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
        }

        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu, menu);
//
//        //Select search item
//        final MenuItem menuItem = menu.findItem(R.id.menu_search);
//        menuItem.setVisible(true);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Busca");
//
//        ((EditText) searchView.findViewById(R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(R.color.colorPrimary));
//        searchView.setOnQueryTextListener(onQuerySearchView);
//
//        menu.findItem(R.id.menu_add).setVisible(true);
//        mSearchCheck = false;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:
                Intent intent = new Intent(getContext(), CadastrarEmpresaActivity.class);
                startActivity(intent);
                break;

            case R.id.menu_search:
                mSearchCheck = true;
                Toast.makeText(getActivity(), "Busca", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

//    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String s) {
//            Toast.makeText(getContext(), "Submitou", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String s) {
//            if (mSearchCheck){
//                //Toast.makeText(getContext(), "Teste", Toast.LENGTH_SHORT).show();
//            }
//            return false;
//        }
//    };

    public List<Produto> getSetProdutoList(int qtd){
        List<Produto> lista = new ArrayList<>();
        int size = empresa.getProdutos().size();
        int n = bundle.getInt("categoria");

        for(int i=0; i < qtd; i++) {
            if(pos < size){
                if(empresa.getProdutos().get(pos).getCategoria() == n) {
                    lista.add(empresa.getProdutos().get(pos));
                }
            }else{
                i = qtd+1;
            }
            pos++;
        }
        return(lista);
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Position: "+position, Toast.LENGTH_SHORT).show();
        ProdutoAdapter adapter = (ProdutoAdapter) mRecyclerView.getAdapter();
        //adapter.removeListItem(position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }
}

