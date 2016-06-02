package clientefeedback.aplicacaocliente.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import clientefeedback.aplicacaocliente.MainActivity;
import clientefeedback.aplicacaocliente.R;

public class CustomRequest extends Request<JSONArray> {

    Context c = MainActivity.contextOfApplication();
    private Listener<JSONArray> listener;
    private Map<String, String> params;

    public CustomRequest(int method, String url, Map<String, String> params,
                         Listener<JSONArray> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        String usuario;
        String senha;
        SharedPreferences prefs = c.getSharedPreferences("account", Context.MODE_PRIVATE);
        prefs.getString(c.getString(R.string.id), "");
        usuario = prefs.getString(c.getString(R.string.login), "");
        senha = prefs.getString(c.getString(R.string.password), "");

        senha = Base64.encodeToString( //Criptografa apenas a senha
                (senha).getBytes(),
                Base64.NO_WRAP);

        return createBasicAuthHeader(usuario, senha);
    }

    private Map<String, String> createBasicAuthHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();

        String credentials = username + ":" + password;
        String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Authorization", "Basic " + encodedCredentials);
        //headerMap.put("Token", getToken());

        return headerMap;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        listener.onResponse(response);
    }
}
