package clientefeedback.aplicacaocliente.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.MainActivity;
import clientefeedback.aplicacaocliente.R;

/**
 * Created by Alexandre on 31/03/2016.
 */
public class AutenticacaoHeaders {

    private static Context c = MainActivity.contextOfApplication();
    private static String login;
    private static String senha;

    public static Map<String,String> basicAuthentication(){
        String usuario;
        String senha;
        SharedPreferences prefs = c.getSharedPreferences("account", Context.MODE_PRIVATE);
        prefs.getString(c.getString(R.string.id), "");
        usuario = prefs.getString(c.getString(R.string.login), "");
        senha = prefs.getString(c.getString(R.string.password), "");

        senha = Base64.encodeToString( //Criptografa apenas a senha
                (senha).getBytes(),
                Base64.NO_WRAP);
        System.out.println("Senha Criptografada: " + senha);
        Log.e("Senha Criptografada: " , senha);
        return createBasicAuthHeader(usuario, senha);
    }

    public static Map<String,String> basicRegister(){

        SharedPreferences prefs = c.getSharedPreferences("account", Context.MODE_PRIVATE);
        prefs.getString(c.getString(R.string.id), "");
        login = prefs.getString(c.getString(R.string.login), "");
        senha = prefs.getString(c.getString(R.string.password), "");

        senha = Base64.encodeToString( //Criptografa apenas a senha
                (senha).getBytes(),
                Base64.NO_WRAP);
        System.out.println("Senha Criptografada: " + senha);
        Log.e("Senha Criptografada: " , senha);
        return createBasicRegisterHeader(login, senha);
    }

    private static Map<String, String> createBasicAuthHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Autentication", "Basic " + encodedCredentials);
        return headerMap;
    }

    private static Map<String, String> createBasicRegisterHeader(String username, String password) {
        Map<String, String> headerMap = new HashMap<String, String>();
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerMap.put("Register", "Basic " + encodedCredentials);
        return headerMap;
    }
}
