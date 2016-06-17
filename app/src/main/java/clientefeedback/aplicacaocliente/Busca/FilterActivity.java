package clientefeedback.aplicacaocliente.Busca;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.florescu.android.rangeseekbar.RangeSeekBar;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import clientefeedback.aplicacaocliente.Interfaces.CallBack;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;

public class FilterActivity extends AppCompatActivity {
    RangeSeekBar<Integer> rangeSeekBar;
    Spinner spinnerEstados;
    Spinner spinnerCidades;
    Spinner spinnerBairros;
//    Spinner spinnerCulinaria;
//    Spinner spinnerOrdenacao;
    ArrayAdapter<String> adapterCidades;
    ArrayAdapter<String> adapterBairros;
//    Long sharedCulinaria;
    Long sharedBairro;
    Long sharedCidade;
    Long sharedEstado;
//    int sharedValorMaximo;
//    int sharedValorMinimo;
//    int sharedComentados;
//    int sharedBuscados;
//    Long sharedOrdenacao;
    ProgressBar progressBar;
    private static final Object TAG = new Object();
    RequestQueue mQueue;
    List listaCidades;
    List listaBairros;
    Button btnFiltrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        setContentView(R.layout.activity_filter);
        listaCidades = new ArrayList<String>();
        listaBairros = new ArrayList<String>();
        progressBar = (ProgressBar)findViewById(R.id.progressBarFilter);
        progressBar.setVisibility(View.GONE);
        carregarSharedPreferences();
//        spinnerOrdenacao = (Spinner) findViewById(R.id.spinnerOrdenacao);
//        ArrayAdapter<CharSequence> adapterOrdem = ArrayAdapter.createFromResource(this,
//                R.array.tipo_ordem, android.R.layout.simple_spinner_item);
//        adapterOrdem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerOrdenacao.setAdapter(adapterOrdem);
//        spinnerOrdenacao.setSelection(sharedOrdenacao.intValue());
//
//        spinnerCulinaria = (Spinner) findViewById(R.id.spinnerCulinaria);
//        ArrayAdapter<CharSequence> adapterCulinaria = ArrayAdapter.createFromResource(this,
//                R.array.tipo_cozinhas, android.R.layout.simple_spinner_item);
//        adapterCulinaria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCulinaria.setAdapter(adapterCulinaria);
//        spinnerCulinaria.setSelection(sharedCulinaria.intValue());


//        rangeSeekBar = new RangeSeekBar<Integer>(this);
//        rangeSeekBar.setRangeValues(5, 500);
//        rangeSeekBar.setSelectedMinValue(sharedValorMinimo);
//        rangeSeekBar.setSelectedMaxValue(sharedValorMaximo);
//        LinearLayout layout = (LinearLayout) findViewById(R.id.rangerSeek);
//        layout.addView(rangeSeekBar);
//        rangeSeekBar.setTextAboveThumbsColorResource(android.R.color.holo_orange_dark);

//        rgComentados = (RadioGroup)findViewById(R.id.rgComentados);
//        rgComentados.check(sharedComentados);
//        rgBuscados = (RadioGroup)findViewById(R.id.rgBuscados);
//        rgBuscados.check(sharedBuscados);


        spinnerCidades = (Spinner) findViewById(R.id.spinnerCidade);
        adapterCidades = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaCidades);
        adapterCidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCidades.setAdapter(adapterCidades);


        spinnerBairros = (Spinner) findViewById(R.id.spinnerBairro);
        adapterBairros = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaBairros);
        adapterBairros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBairros.setAdapter(adapterBairros);
        spinnerBairros.setSelection(sharedBairro.intValue());


        spinnerEstados = (Spinner) findViewById(R.id.spinnerEstados);
        ArrayAdapter<CharSequence> adapterEstados = ArrayAdapter.createFromResource(this,
                R.array.estados, android.R.layout.simple_spinner_item);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstados.setAdapter(adapterEstados);
        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterBairros.clear();
                adapterBairros.clear();
                carregarCidadesNoSpinner(adapterView.getSelectedItem().toString());
                spinnerCidades.setSelection(sharedCidade.intValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerEstados.setSelection(sharedEstado.intValue());

        spinnerCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                carregarBairrosNoSpinner(adapterView.getSelectedItem().toString());
                spinnerBairros.setSelection(sharedBairro.intValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerCidades.setSelection(sharedCidade.intValue());


        btnFiltrar = (Button)findViewById(R.id.btnFiltrar);
        btnFiltrar.setEnabled(true);
        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1);
                salvarSharedPreferences();
                finish();

            }
        });

        Button btnLimparFiltro = (Button)findViewById(R.id.btnLimparFiltro);
        btnLimparFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparFiltro();
            }
        });



    }

    public void carregarCidadesNoSpinner(String estado){
        progressBar.setVisibility(View.VISIBLE);
        btnFiltrar.setEnabled(false);
        adapterCidades.clear();
        adapterCidades.add("Todos");
        adapterBairros.add("Todos");
        requestCidades(estado);
    }

    public void carregarBairrosNoSpinner(String cidade){
        progressBar.setVisibility(View.VISIBLE);
        btnFiltrar.setEnabled(false);
        adapterBairros.clear();
        adapterBairros.add("Todos");
        requestBairros(cidade);
    }

    public void carregarSharedPreferences(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("filtro", Context.MODE_PRIVATE);
//        sharedOrdenacao = sharedPreferences.getLong("ordenacaoId", 0);
//        sharedCulinaria = sharedPreferences.getLong("culinariaId", 0);
        sharedEstado = sharedPreferences.getLong("estadoId", 0);
        sharedCidade = sharedPreferences.getLong("cidadeId",0);
        sharedBairro = sharedPreferences.getLong("bairroId",0);
//        sharedValorMinimo = sharedPreferences.getInt("valorMinimo", 5);
//        sharedValorMaximo = sharedPreferences.getInt("valorMaximo", 500);
//        sharedComentados = sharedPreferences.getInt("comentadosId",0);
//        sharedBuscados = sharedPreferences.getInt("buscadosId", 0);
    }

    public void salvarSharedPreferences(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("filtro", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong("ordenacaoId", spinnerOrdenacao.getSelectedItemId());
//        editor.putString("ordenacao", spinnerOrdenacao.getSelectedItem().toString());
//
//        editor.putLong("culinariaId", spinnerCulinaria.getSelectedItemId());
//        editor.putString("culinaria", spinnerCulinaria.getSelectedItem().toString());

        editor.putLong("estadoId", spinnerEstados.getSelectedItemId());
        editor.putString("estado", spinnerEstados.getSelectedItem().toString());

        editor.putLong("cidadeId", spinnerCidades.getSelectedItemId());
        editor.putString("cidade", spinnerCidades.getSelectedItem().toString());

        editor.putLong("bairroId", spinnerBairros.getSelectedItemId());
        editor.putString("bairro", spinnerBairros.getSelectedItem().toString());

//        editor.putInt("valorMinimo", rangeSeekBar.getSelectedMinValue());
//
//        editor.putInt("valorMaximo", rangeSeekBar.getSelectedMaxValue());


//        editor.putInt("comentadosId", rgComentados.getCheckedRadioButtonId());
//        editor.putString("comentados", rgComentados.);
//
//        editor.putInt("buscadosId", rgBuscados.getCheckedRadioButtonId());
//        editor.putString("buscados", rgBuscados.toString());

        editor.commit();
    }

    public void limparFiltro(){
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("filtro", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putLong("cidadeId", 0);
        editor.putString("cidade", "");
        editor.commit();
        finish();
        startActivity(getIntent());
    }

    public void requestCidades(String estado){
        String url = Url.getUrl() + "Endereco/getCidades/" + estado;


        final StringRequest jsonRequest = new AutorizacaoRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String result) {
                        progressBar.setVisibility(View.GONE);
                        btnFiltrar.setEnabled(true);

                        try {
                            listaCidades = jsonStringToArray(result);
                            adapterCidades.addAll(listaCidades);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                btnFiltrar.setEnabled(true);
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getApplicationContext(), "Senha ou Usuário incorreto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro de conexao", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonRequest.setTag(TAG);
        mQueue.add(jsonRequest);
    }

    public void requestBairros(String cidade){
        String url = Url.getUrl() + "Endereco/getBairros/" + cidade;

        final StringRequest jsonRequest = new AutorizacaoRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String result) {
                        progressBar.setVisibility(View.GONE);
                        btnFiltrar.setEnabled(true);

                        try {
                            listaBairros = jsonStringToArray(result);
                            adapterBairros.addAll(listaBairros);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                btnFiltrar.setEnabled(true);
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getApplicationContext(), "Senha ou Usuário incorreto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro de conexao", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jsonRequest.setTag(TAG);
        mQueue.add(jsonRequest);
    }

    public ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }






}
