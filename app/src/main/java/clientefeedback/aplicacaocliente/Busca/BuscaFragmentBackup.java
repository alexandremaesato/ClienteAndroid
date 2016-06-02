//package clientefeedback.aplicacaocliente.Busca;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.SearchView;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import clientefeedback.aplicacaocliente.Models.Empresa;
//import clientefeedback.aplicacaocliente.R;
//import clientefeedback.aplicacaocliente.RequestData;
//import clientefeedback.aplicacaocliente.Services.Url;
//import clientefeedback.aplicacaocliente.Transaction;
//import clientefeedback.aplicacaocliente.VolleyConn;
//
//
//public class BuscaFragmentBackup extends Fragment implements Transaction, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
//
//    private boolean mSearchCheck;
//    private static final String TEXT_FRAGMENT = "Busca";
//    private ListView listView;
//    private BuscaEmpresaAdapter adapter;
//    private ProgressBar progressBar;
//    private Empresa empresa;
//    private boolean isThereMore;
//    private List<Empresa> list;
//
//    public static BuscaFragmentBackup newInstance(String text){
//        BuscaFragmentBackup mFragment = new BuscaFragmentBackup();
//        Bundle mBundle = new Bundle();
//        mBundle.putString(TEXT_FRAGMENT, text);
//        mFragment.setArguments(mBundle);
//
//        return mFragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        View rootView = inflater.inflate(R.layout.fragment_busca, container, false);
//
//        TextView mTxtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
//        mTxtTitle.setText(TEXT_FRAGMENT);
//
//        empresa = new Empresa();
//        list = new ArrayList<Empresa>();
//
//        progressBar = (ProgressBar) rootView.findViewById(R.id.pbProxy);
//
//        listView = (ListView) rootView.findViewById(R.id.listView);
//        listView.setOnItemClickListener(this);
//        listView.setOnScrollListener(this);
//        (new VolleyConn(getContext(), this)).execute();
//
//        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_busca, menu);
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
//
//        mSearchCheck = false;
//
//        final MenuItem menuFilter = menu.findItem(R.id.menu_filter);
//        menuFilter.setVisible(true);
//
//        menuFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Intent intent = new Intent(getContext(), FilterActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//
//        switch (item.getItemId()) {
//
//            case R.id.menu_add:
//                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.menu_search:
//                mSearchCheck = true;
//                Toast.makeText(getActivity(), "Busca" , Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }
//
//    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
//        @Override
//        public boolean onQueryTextSubmit(String s) {
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextChange(String s) {
//            if (mSearchCheck){
//                // implement your search here
//            }
//            return false;
//        }
//    };
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////        Intent it = new Intent(getContext(), DetailsActivity.class);
////        it.putExtra("car", list.get(position));
////        Bundle b = new Bundle();
////        b.put
////        startActivity(it);
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int i) {
//        if(view.getId() == listView.getId() && isThereMore){
//            if(listView.getLastVisiblePosition() + 1 == list.size()){
//                empresa.setEmpresaId(list.get(list.size() - 1).getEmpresaId());
//                isThereMore = false;
//                (new VolleyConn(getContext(), this)).execute();
//            }
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void doBefore() {
//        progressBar.setVisibility(View.VISIBLE);
//
//    }
//
//    @Override
//    public void doAfter(String answer) {
//        try{
//            JSONObject json = new JSONObject(answer);
//            JSONArray empresasJson = json.getJSONArray("Empresas");
//            Gson gson = new Gson();
//            List<Empresa> empresas= new ArrayList<Empresa>();
//
//            empresas = gson.fromJson(empresasJson.toString(),  new TypeToken<ArrayList<Empresa>>() {
//            }.getType());
//
//            if(empresas.size() > 0){
//                if(adapter == null){
//                    adapter = new BuscaEmpresaAdapter(getContext(), empresas);
//                    listView.setAdapter(adapter);
//                }
//                else{
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        finally {
//            progressBar.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public RequestData getRequestData() {
//        return( new RequestData(Url.getUrl()+"Empresa/buscarEmpresas", "Empresa/pegarEmpresas", empresa) );
//    }
//}
