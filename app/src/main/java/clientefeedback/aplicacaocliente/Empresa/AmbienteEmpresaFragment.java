package clientefeedback.aplicacaocliente.Empresa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.Url;

/**
 * Created by Alexandre on 14/06/2016.
 */
public class AmbienteEmpresaFragment extends Fragment {

    public static AmbienteEmpresaFragment newInstance(String text){
        AmbienteEmpresaFragment mFragment = new AmbienteEmpresaFragment();
        Bundle mBundle = new Bundle();
        //mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ambiente_empresa, container, false);
        WebView wv = (WebView)rootView.findViewById(R.id.webAmbiente);
        WebSettings webSettings = wv.getSettings();
        webSettings.setSupportZoom(false);
        wv.loadUrl(Url.URL_IMAGEM+"/teste/index.html");
        return rootView;

    }

}
