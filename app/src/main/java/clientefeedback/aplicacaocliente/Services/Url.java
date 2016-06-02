package clientefeedback.aplicacaocliente.Services;

import android.content.Context;

import clientefeedback.aplicacaocliente.BD.AutenticacaoDao;

/**
 * Created by Guilherme on 23/02/2016.
 */
public class Url {
    public static String IP = "http://192.168.100.11:8080/";
//    public static String IP = "http://192.168.100.11:8080/ServidorAplicativo";
//    public static String IP = "http://192.168.25.15:8080/";
    //private static String url = "http://192.168.25.15:8080/ServidorAplicativo/webresources/";
//    private static String url = "http://172.20.82.32:8080/ServidorAplicativo/webresources/";
    public static String url = IP + "ServidorAplicativo/webresources/";




    public static String getUrl() {
        return url;
    }

}
