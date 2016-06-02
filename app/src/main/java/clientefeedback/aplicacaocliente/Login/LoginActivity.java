package clientefeedback.aplicacaocliente.Login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.common.AccountPicker;

//import org.apache.http.HttpStatus;

import java.util.LinkedList;
import java.util.List;

import clientefeedback.aplicacaocliente.MainActivity;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;

public class LoginActivity extends AppCompatActivity {
    AutoCompleteTextView email;
    EditText senha;
    TextView naoCadastrado;
    TextView esqueceuSenha;
    Button btnEnviar;
    private Resources resources;
    Context c;
    SharedPreferences prefs;
    String novaSenha;
    String novoLogin;
    ProgressBar spinner;
    int REQUEST_CODE = 1000;
    RequestQueue mQueue;
    private static final Object TAG = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        prefs = getSharedPreferences("account", Context.MODE_PRIVATE);
        c = this;
        initViews();
    }

    private void initViews() {
        //resources = getResources();

        email = (AutoCompleteTextView) findViewById(R.id.editEmail);
        email.setText(getUsername());
        senha = (EditText) findViewById(R.id.editPassword);
        senha.setText(prefs.getString(getString(R.string.password),""));
        naoCadastrado = (TextView) findViewById(R.id.textNaoCadastrado);
        esqueceuSenha = (TextView) findViewById(R.id.textEsqueceuSenha);
        btnEnviar = (Button) findViewById(R.id.buttonLoginEntrar);
        spinner = (ProgressBar) findViewById(R.id.progressBarLogin);
        spinner.setVisibility(View.GONE);

        //Botao de logar
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

                            } catch (Exception e) {

                            }
                        }
                    }.start();
                }

            }
        });

        naoCadastrado.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(c, CadastrarAutenticacao.class);
                startActivity(intent);
            }
        });

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(c, "Clicou em Esqueceu Senha", Toast.LENGTH_SHORT).show();
            }
        });

//        logarComGoogle.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
////                        false, null, null, null, null);
////                startActivityForResult(intent, REQUEST_CODE);
////                Toast.makeText(LoginActivity.this, String.valueOf(REQUEST_CODE), Toast.LENGTH_SHORT).show();
//                showGoogleAccountPicker();
//
//            }
//        });

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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        manager.newChooseAccountIntent(null, null,
                null, false, null, null, null, null);
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
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(LoginActivity.this, accountName, Toast.LENGTH_SHORT).show();
        }
    }

    private void showGoogleAccountPicker() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE);

    }

    private void doRequest(){
        String url = Url.getUrl()+"secured/message";

        StringRequest jsonRequet = new AutorizacaoRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String result) {
                        spinner.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                        if("Accepted".equals(result)){
                            Intent intent = new Intent(c, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(LoginActivity.this, "Senha ou Usuário incorreto", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Erro de conexao", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonRequet.setTag(TAG);

        mQueue.add(jsonRequet);

    }



}



