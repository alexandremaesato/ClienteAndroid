package clientefeedback.aplicacaocliente.Login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.LinkedList;
import java.util.List;

import clientefeedback.aplicacaocliente.MainActivity;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.CadastrarAutenticacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;

public class CadastrarAutenticacao extends AppCompatActivity {

    AutoCompleteTextView email;
    EditText senha;
    TextView naoCadastrado;
    TextView esqueceuSenha;
    Button btnEnviar;
    private Resources resources;
    Context c;
    String autenticacao;
    SharedPreferences prefs;
    String novaSenha;
    String novoLogin;
    private AutoCompleteTextView mEmailView;
    ProgressBar spinner;
    DialogFragment newFragment;
    RequestQueue mQueue;
    private static final Object TAG = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_autenticacao);
        resources = getResources();
        initViews();
    }

    private void initViews() {
        prefs = getSharedPreferences("account", Context.MODE_PRIVATE);
        c = this;
        email = (AutoCompleteTextView) findViewById(R.id.editEmail);
        //email.setText(getUsername());
        senha = (EditText) findViewById(R.id.editPassword);
        //senha.setText(prefs.getString(getString(R.string.senha),""));
        btnEnviar = (Button) findViewById(R.id.buttonLoginEntrar);
        spinner = (ProgressBar) findViewById(R.id.progressBarLogin);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        spinner.setVisibility(View.GONE);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateFields()) {
                    spinner.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {
                            try {
                                novoLogin = email.getText().toString();
                                novaSenha = senha.getText().toString();
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(getString(R.string.login), novoLogin);
                                editor.putString(getString(R.string.password), novaSenha);
                                novaSenha = Base64.encodeToString( //Criptografa apenas a senha
                                        (novaSenha).getBytes(),
                                        Base64.NO_WRAP);
                                editor.commit();
                                doRequest();
                            }catch(Exception e){
                                System.out.println(e);

                            }
                        }
                    }.start();
                }

            }
        });

    }


    private boolean validateFields() {
        String user = email.getText().toString().trim();
        String pass = senha.getText().toString().trim();
        if (!isEmailValid(email.getText().toString().trim())) {
            email.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return (!isEmptyFields(user, pass) && hasSizeValid(user, pass));
    }

    private boolean isEmptyFields(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            email.requestFocus(); //seta o foco para o campo user
            email.setError(resources.getString( R.string.email_required));
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            senha.requestFocus(); //seta o foco para o campo password
            senha.setError(resources.getString(R.string.password_required));
            return true;
        }
        return false;
    }

    private boolean hasSizeValid(String user, String pass) {

        if (!(user.length() > 3)) {
            email.requestFocus();
            email.setError(resources.getString(R.string.email_required_size_invalid));
            return false;
        } else if (!(pass.length() > 5)) {
            senha.requestFocus();
            senha.setError(resources.getString(R.string.password_required_size_invalid));
            return false;
        }
        return true;
    }

    private void clearErrorFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            Toast.makeText(CadastrarAutenticacao.this, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.GONE);
        }
    };

    public void msgHandler(String msg){
        Message m = new Message();
        Bundle b = new Bundle();
        b.putString("msg", msg);
        m.setData(b);
        //m.setTarget(handler);
        handler.sendMessage(m);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");


            if (parts.length > 1)
                //return parts[0];
                return email;
        }
        return null;
    }

    private void doRequest(){
//        String url = Url.getUrl()+"secured/message";
//
//        StringRequest jsonRequet = new CadastrarAutenticacaoRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//
//                    public void onResponse(String result) {
//                        spinner.setVisibility(View.GONE);
//                        //Toast.makeText(CadastrarAutenticacao.this, result.toString(), Toast.LENGTH_SHORT).show();
//                        if("Accepted".equals(result)){
//                            Intent intent = new Intent(c, MainActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            public void onErrorResponse(VolleyError error) {
//                spinner.setVisibility(View.GONE);
//                if (error.networkResponse != null) {
//                    if (error.networkResponse.statusCode == 401) {
//                        Toast.makeText(CadastrarAutenticacao.this, "Usuario já está cadastrado", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(CadastrarAutenticacao.this, "Erro de conexao", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        jsonRequet.setTag(TAG);
//
//        mQueue.add(jsonRequet);
    }
}
