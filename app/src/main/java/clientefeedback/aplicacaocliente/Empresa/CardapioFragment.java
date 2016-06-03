package clientefeedback.aplicacaocliente.Empresa;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.MainFragment;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.Produto.ProdutoFragment;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.Lista;
import clientefeedback.aplicacaocliente.TabPagerItem;
import clientefeedback.aplicacaocliente.ViewPagerAdapter;

/**
 * Created by Alexandre on 29/04/2016.
 */
public class CardapioFragment extends Fragment{

        private List<TabPagerItem> mTabs = new ArrayList<>();
        private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";
        private Empresa empresa;
        private Bundle bundle;
        private Lista<Integer> categorias = new Lista<>();

        public static CardapioFragment newInstance(String text){
            CardapioFragment mFragment = new CardapioFragment();
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
            createTabPagerItem();
        }

        private void createTabPagerItem(){


            carregaCategorias();
            for(int i =0; i< categorias.size(); i++){
                Fragment cardapio = ProdutoFragment.newInstance("produto");
                cardapio.setArguments(bundle);
                mTabs.add(new TabPagerItem(Produto.CATEGORIAS[categorias.get(i)], cardapio));
            }
//            mTabs.add(new TabPagerItem("Bebidas", cardapio));
//
//            mTabs.add(new TabPagerItem("Hamburguers", MainFragment.newInstance("Hamburguers")));
//            mTabs.add(new TabPagerItem("Petiscos", MainFragment.newInstance("Petiscos")));
//            mTabs.add(new TabPagerItem("Sobremesas", MainFragment.newInstance("Sobremesas")));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cardapio_empresa, container, false);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //PagerTitleStrip pagerTitleStrip = (PagerTitleStrip)rootView.findViewById(R.id.titlestrip);
            HorizontalScrollView scrollView = new HorizontalScrollView(rootView.getContext());

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

        public void carregaCategorias(){
            List<Produto> produtos = empresa.getProdutos();
            if(!empresa.produtosIsEmpty()) {
                for (int i = 0; i <empresa.getProdutos().size(); i++){
                    if(!categorias.contains(produtos.get(i).getCategoria())){
                        categorias.add(produtos.get(i).getCategoria());
                    }

                }
            }
        }

}
