package clientefeedback.aplicacaocliente;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Services.AutenticacaoHeaders;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;

public class VolleyConGET {
    private Context c;
    private Transaction transaction;
    private RequestQueue requestQueue;



    public VolleyConGET(Context c, Transaction t){
        this.c = c;
        transaction = t;
        requestQueue = ((CustomApplication) ((Activity) c).getApplication()).getRequestQueue();
    }


    public void execute(){
        transaction.doBefore();
        callByStringRequest(transaction.getRequestData());
    }


    private void callByStringRequest(final RequestData requestData){
        StringRequest request = new AutorizacaoRequest(Request.Method.GET,
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
                        transaction.doAfter(null);
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
