package clientefeedback.aplicacaocliente;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.Busca.BuscaFragment;
import clientefeedback.aplicacaocliente.Empresa.CadastrarEmpresaActivity;
import clientefeedback.aplicacaocliente.Empresa.PrincipalEmpresaFragment;
import clientefeedback.aplicacaocliente.Login.VolleyConnCadastrar;
import clientefeedback.aplicacaocliente.Produto.CadastrarProdutoActivity;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.CadastrarAutenticacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener, Transaction {
    android.app.FragmentManager fm = getFragmentManager();
    Fragment mFragment = null;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    public static Context contextOfApplication;
    SharedPreferences sharedPreferences;
    EditText email;
    EditText senha;
    ProgressBar progressBar;
    private static final Object TAG = new Object();
    RequestQueue mQueue;
    Transaction t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        contextOfApplication = this;
        setSupportActionBar(toolbar);
        mFragment = MainFragment.newInstance("MAIN");
        mFragmentManager.beginTransaction().replace(R.id.conteudo, mFragment,"main").addToBackStack("main").commit();
        sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
//        mQueue = Volley.newRequestQueue(getApplicationContext());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationViewInit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            mFragment = MainFragment.newInstance("teste");

        } else if (id == R.id.tabPagerExemplo) {
            mFragment = new ViewPagerFragment();
        } else if (id == R.id.cadastrarEmpresa) {
            Intent it = new Intent(this, CadastrarEmpresaActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_share) {
            mFragment = new PrincipalEmpresaFragment();

        } else if (id == R.id.nav_send) {
            mFragment = new MainFragment();
        } else if (id == R.id.nav_busca) {
            mFragment = new BuscaFragment(mFragmentManager);
        }

        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.conteudo, mFragment).addToBackStack("main").commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public static Context contextOfApplication() {
        return contextOfApplication;
    }

    public String getUser() {
        return sharedPreferences.getString(getString(R.string.login), "");
    }

    public void navigationViewInit() {

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(getString(R.string.login), "");
//        editor.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        initButtonsHeader(header);
        progressBar = (ProgressBar) header.findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);
        email = (EditText) header.findViewById(R.id.editEmail);
        senha = (EditText) header.findViewById(R.id.editPassword);
        Button btnLogar = (Button) header.findViewById(R.id.btnLogin);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateFields()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {

                            try {
                                String novoLogin = email.getText().toString();
                                String novaSenha = senha.getText().toString();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.login), novoLogin);
                                editor.putString(getString(R.string.password), novaSenha);
                                editor.commit();
                                doRequest();

                            } catch (Exception e) {

                            }
                        }
                    }.start();
                }

            }
        });

        Button btnCadastrar = (Button) header.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateFields()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new Thread() {
                        public void run() {

                            try {
                                String novoLogin = email.getText().toString();
                                String novaSenha = senha.getText().toString();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(getString(R.string.login), novoLogin);
                                editor.putString(getString(R.string.password), novaSenha);
                                editor.commit();
                                doRequestCadastrar(novoLogin, novaSenha);

                            } catch (Exception e) {

                            }
                        }
                    }.start();
                }

            }
        });

        TextView textSair = (TextView) header.findViewById(R.id.txtSair);
        textSair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cleanSharedPreferences();
                finish();
                startActivity(getIntent());

            }
        });


        ViewGroup.LayoutParams params = header.getLayoutParams();
        params.height = 600;
        header.setLayoutParams(params);

        String user = getUser();

        LinearLayout logado = (LinearLayout) header.findViewById(R.id.headerLogado);
        LinearLayout naoLogado = (LinearLayout) header.findViewById(R.id.headerNaoLogado);

        TextView email = (TextView) header.findViewById(R.id.userEmail);
        //ImageView image = (ImageView) header.findViewById(R.id.userImage);


        logado.setVisibility(View.GONE);
        naoLogado.setVisibility(View.VISIBLE);

        if (user != "") {
            params.height = 300;
            header.setLayoutParams(params);
            logado.setVisibility(View.VISIBLE);
            naoLogado.setVisibility(View.GONE);
            email.setText(user);
        }
    }

    public void initButtonsHeader(View view) {


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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isEmptyFields(String user, String pass) {
        if (TextUtils.isEmpty(user)) {
            email.requestFocus(); //seta o foco para o campo user
            email.setError(getResources().getString(R.string.email_required));
            return true;
        } else if (TextUtils.isEmpty(pass)) {
            senha.requestFocus(); //seta o foco para o campo password
            senha.setError(getResources().getString(R.string.password_required));
            return true;
        }
        return false;
    }

    private boolean hasSizeValid(String user, String pass) {

        if (!(user.length() > 3)) {
            email.requestFocus();
            email.setError(getResources().getString(R.string.email_required_size_invalid));
            return false;
        } else if (!(pass.length() > 5)) {
            senha.requestFocus();
            senha.setError(getResources().getString(R.string.password_required_size_invalid));
            return false;
        }
        return true;
    }

    private void doRequest() {
        (new VolleyConn(contextOfApplication, this)).execute();

    }

    private void doRequestCadastrar(String login, String senha){
        (new VolleyConnCadastrar(contextOfApplication, getTransactionCadastrar(this))).execute();
    }

    private void cleanSharedPreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.login), "");
        editor.commit();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lista_desejos:
                Toast.makeText(this, "Foi para Lista de Desejo", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.algo:
                Toast.makeText(this, "foi para algo", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    public void setFavorito(View v){
        Toast.makeText(this, "Foi para Favoritos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void doAfter(String answer) {
        if(answer != null) {
            Gson gson = new Gson();
            try {
                String id = gson.fromJson(answer, String.class);
                String novoLogin = email.getText().toString();
                String novaSenha = senha.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.login), novoLogin);
                editor.putString(getString(R.string.password), novaSenha);
                editor.putInt(getString(R.string.id_pessoa), Integer.parseInt(id));
                editor.commit();
                finish();
                startActivity(getIntent());
            }catch (Exception e){
                Toast.makeText(MainActivity.this, answer, Toast.LENGTH_LONG).show();
                cleanSharedPreferences();
            }
        }else {
            cleanSharedPreferences();
            Toast.makeText(MainActivity.this, answer, Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public RequestData getRequestData() {
        return new RequestData(Url.getUrl() + "autenticacao/logar", "",new HashMap<String,String>() );
    }


    public Transaction getTransactionCadastrar(final MainActivity m){
        t = new Transaction() {
            @Override
            public void doBefore() {
//                this.doBefore();
                m.doBefore();

            }

            @Override
            public void doAfter(String answer) {
                m.doAfter(answer);
            }

            @Override
            public RequestData getRequestData() {

                return new RequestData(Url.getUrl() + "autenticacao/cadastrar", "",new HashMap<String,String>() );
            }
        };
        return t;
    }
}
