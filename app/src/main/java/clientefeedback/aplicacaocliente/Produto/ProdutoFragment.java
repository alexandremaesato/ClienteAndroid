package clientefeedback.aplicacaocliente.Produto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clientefeedback.aplicacaocliente.Avaliacao.AvaliacaoDialogFragment;
import clientefeedback.aplicacaocliente.Empresa.CadastrarEmpresaActivity;
import clientefeedback.aplicacaocliente.Interfaces.OnItemClickListener;
import clientefeedback.aplicacaocliente.Interfaces.RecyclerViewOnClickListenerHack;
import clientefeedback.aplicacaocliente.Models.Avaliacao;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.ConnectionVerify;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConGET;

/**
 * Created by Alexandre on 04/05/2016.
 */
public class ProdutoFragment extends Fragment implements RecyclerViewOnClickListenerHack, OnItemClickListener, Transaction {

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
    private RecyclerView mRecyclerView;
    private List<Produto> mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Empresa empresa;
    private int idProdutoAvaliacao;
    Avaliacao avaliacao;
    Bundle bundle;
    int pos = 0;
    AvaliacaoDialogFragment avaliacaoDialogFragment = new AvaliacaoDialogFragment();
    FragmentTransaction ft;

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
            ProdutoAdapter adapter = new ProdutoAdapter(getActivity(), mList, this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

//            case R.id.menu_add:
//                Intent intent = new Intent(getContext(), CadastrarEmpresaActivity.class);
//                startActivity(intent);
//                break;

            case R.id.menu_search:
                mSearchCheck = true;
                Toast.makeText(getActivity(), "Busca", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    public List<Produto> getSetProdutoList(int qtd){
        List<Produto> lista = new ArrayList<>();
        int size = empresa.getProdutos().size();
        int n = bundle.getInt("categoria");
        int pegos = 0;

        for(int i=0; i < size; i++) {
            if(pegos < size && pos < size){
                if(empresa.getProdutos().get(pos).getCategoria() == n) {
                    lista.add(empresa.getProdutos().get(pos));
                    pegos++;
                }
            }else{
                i = size+1;
            }
            pos++;
        }
        return(lista);
    }

    @Override
    public void onClickListener(View view, int position) {
        //Toast.makeText(getActivity(), "Position: "+position, Toast.LENGTH_SHORT).show();
        bundle.putInt("idProduto", mList.get(position).getProdutoid());
        ProdutoAdapter adapter = (ProdutoAdapter) mRecyclerView.getAdapter();
        Intent it = new Intent(getContext(),ProdutoDetalhes.class);
        it.putExtra("idProduto", mList.get(position).getProdutoid());
        startActivity(it);
        //adapter.removeListItem(position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }

    @Override
    public void onItemClicked(View v, int id) {
        idProdutoAvaliacao = id;
        new VolleyConGET(getContext(),this).execute();
    }

    public Bundle getBundleAvaliacao(int id){
        Bundle bundle = new Bundle();
        SharedData sharedData = new SharedData(getContext());
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAvaliadoid(id);
        avaliacao.setPessoaid(sharedData.getPessoaId());
        avaliacao.setTipoAvalicao(Avaliacao.PRODUTO);
        bundle.putParcelable("avaliacao", avaliacao);
        return bundle;
    }


    @Override
    public void doBefore() {
        ft = getFragmentManager().beginTransaction();
    }

    @Override
    public void doAfter(String answer) {
        Gson gson = new Gson();
        avaliacaoDialogFragment = new AvaliacaoDialogFragment();
        try {
            avaliacao = gson.fromJson(answer, Avaliacao.class);
            bundle.putParcelable("avaliacao", avaliacao);
            avaliacaoDialogFragment.setArguments(bundle);
        }catch (Exception e){
           loadAvaliacao();
            avaliacaoDialogFragment.setArguments(getBundleAvaliacao(idProdutoAvaliacao));
            Toast.makeText(getContext(), "Sem avaliação", Toast.LENGTH_SHORT).show();
        }

        avaliacaoDialogFragment.setTargetFragment(this, 1);

        avaliacaoDialogFragment.show(ft, "dialog");

    }

    @Override
    public RequestData getRequestData() {
        HashMap<String,String> params = new HashMap<>();
        Gson gson = new Gson();
        loadAvaliacao();
        params.put("avaliacao", gson.toJson(avaliacao));
        return new RequestData(Url.getUrl()+"avaliacao/getAvaliacao/"+avaliacao.getPessoaid()+"/"+idProdutoAvaliacao+"/"+avaliacao.getTipoAvalicao(), "", params);
    }
     private void loadAvaliacao(){
         avaliacao = new Avaliacao();
         SharedData sd = new SharedData(getContext());
         avaliacao.setPessoaid(sd.getPessoaId());
         avaliacao.setAvaliadoid(idProdutoAvaliacao);
         avaliacao.setTipoAvalicao(Avaliacao.PRODUTO);

     }


}

