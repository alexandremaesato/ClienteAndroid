package clientefeedback.aplicacaocliente.Login;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.CustomApplication;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.CadastrarAutenticacaoRequest;
import clientefeedback.aplicacaocliente.Transaction;

/**
 * Created by Alexandre on 20/05/2016.
 */
public class VolleyConnCadastrar {
    private Context c;
    private Transaction transaction;
    private RequestQueue requestQueue;



    public VolleyConnCadastrar(Context c, Transaction t){
        this.c = c;
        transaction = t;
        requestQueue = ((CustomApplication) ((Activity) c).getApplication()).getRequestQueue();
    }


    public void execute(){
        transaction.doBefore();
        callByStringRequest(transaction.getRequestData());
    }


    private void callByStringRequest(final RequestData requestData){
//        HashMap<String,String> hash = (HashMap<String,String>)transaction.getRequestData().getObj();
//        String login = hash.get("login");
//        String senha = hash.get("senha");
        StringRequest request = new CadastrarAutenticacaoRequest(Request.Method.POST,
                requestData.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        transaction.doAfter(response);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        String message = "Erro";
                        if(error != null) {
                            if (error.networkResponse.statusCode == 401) {
                                message = c.getString(R.string.erro401Cadastro);
                            }
                        }
                        transaction.doAfter(message);
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("method", requestData.getMethod());
                params.put("teste","true");


                if(requestData.getObj() != null){
                    params.putAll((Map<String,String>)requestData.getObj());
//                    Empresa empresa = (Empresa)requestData.getObj();
//                    params.put("lastId", Integer.toString(empresa.getEmpresaId()));
                }

                return(params);
            }

        };

        request.setTag(new Object());

        requestQueue.add(request);
        System.out.println("============================================================>>>>>>>>>>>>>>>>>>>>>>>>> " + request.getUrl());
    }


}


