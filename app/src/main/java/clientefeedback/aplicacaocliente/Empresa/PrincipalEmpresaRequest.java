package clientefeedback.aplicacaocliente.Empresa;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.CustomApplication;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.Transaction;

/**
 * Created by Alexandre on 07/05/2016.
 */
public class PrincipalEmpresaRequest {
    private Context c;
    private Transaction transaction;
    private RequestQueue requestQueue;



    public PrincipalEmpresaRequest(Context c, Transaction t){
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
