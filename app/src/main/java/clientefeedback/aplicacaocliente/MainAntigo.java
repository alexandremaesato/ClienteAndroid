package clientefeedback.aplicacaocliente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import clientefeedback.aplicacaocliente.Empresa.CadastrarEmpresaActivity;
import clientefeedback.aplicacaocliente.Login.LoginActivity;
//import clientefeedback.aplicacaocliente.Login.SignInActivity;

import static android.accounts.AccountManager.newChooseAccountIntent;

public class MainAntigo extends AppCompatActivity {
    public static Context contextOfApplication;
    public static GoogleSignInAccount account;
    Button botaoCadastrar;
    Button botaoLogin;
    Button btnLoginTeste2;
    Context c;
    SharedPreferences sharedPreferences;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainantigo);
        botaoCadastrar = (Button)findViewById(R.id.button);
        botaoLogin = (Button)findViewById(R.id.buttonLogin);
        btnLoginTeste2 = (Button) findViewById(R.id.btnLoginTeste2);
        c = this;
        contextOfApplication = this;
        mQueue = Volley.newRequestQueue(getApplicationContext());
        Context context = this;
        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferences), Context.MODE_PRIVATE);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                intent = new Intent(c, CadastrarEmpresaActivity.class);
                startActivity(intent);
            }
        });

        botaoLogin.setOnClickListener(new View.OnClickListener(){
            Intent intent;
            @Override
            public void onClick(View v) {
                intent = new Intent(c,LoginActivity.class);
                startActivity(intent);
            }
        });

        btnLoginTeste2.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
//        if (id == R.id.action_logout) {
//            Intent intent = new Intent(getBaseContext(), SignInActivity.class);
//            Bundle b = new Bundle();
//            b.putBoolean("logout",true);
//            intent.putExtras(b);
//            startActivity(intent);
//
//        }

        return super.onOptionsItemSelected(item);
    }



    private void updateUI(boolean signedIn) {
        if (signedIn) {
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText("Deslogado");

//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void toast(int id) {
        String text = getResources().getString(id);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private int parseJson(JSONObject root) throws JSONException {
        boolean useNetworkImageView = true;
        int code = root.getJSONObject("meta").getInt("code");
        if (code == 200) {
            Toast.makeText(MainAntigo.this, "JSON", Toast.LENGTH_SHORT).show();
        }
        return code;
    }


    public static Context contextOfApplication() {
        return contextOfApplication;
    }
}
