package clientefeedback.aplicacaocliente.Services;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.text.format.Time;

import com.google.gson.Gson;
import com.google.gson.JsonParser;


/**
 * Created by Alexandre on 05/03/2016.
 */
public class GeradorIdentificacaoRequisicao {

    private static final String DELIMITADOR = "\n";
    private String sharedKey;
    private String metodo;

    public static String geraIdDaRequisicao(String login, String senha, String sharedKey, String metodo) {
        String id = login + senha + metodo + sharedKey;

        try {
            return Criptografia.gerarCriptografia(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        //return criptografia.criptografa(id);
    }



}