package clientefeedback.aplicacaocliente.Services;

import android.content.Context;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.MainActivity;


/**
 * Created by Alexandre on 31/03/2016.
 */
public class CadastrarAutenticacaoRequest extends StringRequest {


    Context c = MainActivity.contextOfApplication();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(c)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();

    public CadastrarAutenticacaoRequest( int method, String url,
                                        Response.Listener<String> listener,
                                        Response.ErrorListener errorListener) {

        super(method, url, listener, errorListener);

    }

    public CadastrarAutenticacaoRequest(String url,
                                        Response.Listener<String> listener,
                                        Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return AutenticacaoHeaders.basicRegister();
    }


}

