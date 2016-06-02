package clientefeedback.aplicacaocliente;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clientefeedback.aplicacaocliente.Empresa.CadastrarEmpresaActivity;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Endereco;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Mask;
import clientefeedback.aplicacaocliente.Models.Telefone;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;


public class CadastrarEmpresaFragment extends Fragment {
    private int REQUEST_CAMERA  = 1;
    private int SELECT_FILE     = 2;
    private List<EditText> allEdsTelefone = new ArrayList<>();
    private RequestQueue rq;
    private Resources resources;
    private static final Object TAG = new Object();
    Gson gson;
    Imagem img = new Imagem();
    Empresa emp;
    Uri imageUri;
    ContentValues values;

    EditText nome;
    EditText cnpj;
    EditText descricao;
    ProgressBar spinner;
    EditText cep;
    EditText rua;
    EditText numero;
    EditText complemento;
    EditText bairro;
    EditText cidade;
    EditText estado;
    EditText pais;
    EditText tipoTelefone;
    EditText numeroTelefone;
    ImageButton bt_AddTelefone;
    ImageView ivImage;
    static AppCompatActivity activity;

    public static CadastrarEmpresaFragment newInstance(String text, AppCompatActivity activity){
        CadastrarEmpresaFragment mFragment = new CadastrarEmpresaFragment();
        Bundle mBundle = new Bundle();
        //mBundle.putString(TEXT_FRAGMENT, text);
        mFragment.setArguments(mBundle);
        CadastrarEmpresaFragment.activity = activity;
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_cadastrar_empresa, container, false);
        View baseView = inflater.inflate(R.layout.fragment_main, container,false);

        Toolbar cetoolbar = (Toolbar) rootView.findViewById(R.id.cetoolbar);
//        activity.setTitle(R.string.cadastro_empresa);
        activity.setSupportActionBar(cetoolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resources      = getResources();

        nome           = (EditText)rootView.findViewById(R.id.editTextNomeEmpresa);
        cnpj           = (EditText)rootView.findViewById(R.id.editTextCnpj);
        descricao      = (EditText)rootView.findViewById(R.id.editTextDescricaoEmpresa);
        rua            = (EditText)rootView.findViewById(R.id.editTextRua);
        numero         = (EditText)rootView.findViewById(R.id.editTextNumeroCasa);
        complemento    = (EditText)rootView.findViewById(R.id.editTextComplemento);
        bairro         = (EditText)rootView.findViewById(R.id.editTextBairro);
        cep            = (EditText)rootView.findViewById(R.id.editTextCep);
        cidade         = (EditText)rootView.findViewById(R.id.editTextCidade);
        estado         = (EditText)rootView.findViewById(R.id.editTextEstado);
        pais           = (EditText)rootView.findViewById(R.id.editTextPais);
        tipoTelefone   = (EditText)rootView.findViewById(R.id.editTextTipoTelefone);
        numeroTelefone = (EditText)rootView.findViewById(R.id.editTextNumeroTelefone);
        bt_AddTelefone = (ImageButton)rootView.findViewById(R.id.bt_AddTelefone);
        ivImage        = (ImageView)rootView.findViewById(R.id.ivImage);

        // Masks
        TextWatcher cnpjMask = Mask.insert("##.###.###/####-##", cnpj);
        cnpj.addTextChangedListener(cnpjMask);

        TextWatcher cepMask = Mask.insert("#####-###", cep);
        cep.addTextChangedListener(cepMask);

        TextWatcher telefoneMask = Mask.insert("(##)#####-####", numeroTelefone);
        numeroTelefone.addTextChangedListener(telefoneMask);


        assert cetoolbar != null;
        cetoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch( item.getItemId() ) {
                    case R.id.action_save:

                        if( validateFields() ) {

                            new Thread() {
                                public void run() {

                                    emp.setNomeEmpresa(nome.getText().toString());
                                    emp.setCnpj(cnpj.getText().toString().replaceAll("[^0123456789]", ""));
                                    emp.setDescricao(descricao.getText().toString());

                                    Endereco endereco = new Endereco(
                                            rua.getText().toString(),
                                            numero.getText().toString(),
                                            complemento.getText().toString(),
                                            bairro.getText().toString(),
                                            cep.getText().toString().replaceAll("[^0123456789]", ""),
                                            cidade.getText().toString(),
                                            estado.getText().toString(),
                                            pais.getText().toString()
                                    );
                                    emp.setEndereco(endereco);

                                    List<Telefone> telefones = new ArrayList<Telefone>();
                                    Telefone telefone = new Telefone(
                                            tipoTelefone.getText().toString(),
                                            numeroTelefone.getText().toString().replaceAll("[^0123456789]", "")
                                    );

                                    telefones.add(telefone);

                                    if (allEdsTelefone.size() > 0) {
                                        int i = 0;

                                        while (i < allEdsTelefone.size()) {

                                            Telefone tel = new Telefone(
                                                    allEdsTelefone.get(i).getText().toString(),
                                                    allEdsTelefone.get(i + 1).getText().toString().replaceAll("[^0123456789]", "")
                                            );
                                            telefones.add(tel);

                                            i += 2;
                                        }
                                    }

                                    emp.setTelefones(telefones);

                                    img.setTipoImagem(1); // Temporario
                                    emp.setImagemPerfil(img);

                                    doRequest();
                                }
                            }.start();
                        }

                        break;
                }
                return false;
            }
        });

        bt_AddTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addTelefone(v);
            }
        });

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String img_cam = getString(R.string.add_imagem_camera);
                final String img_lib = getString(R.string.add_imagem_galeria);
                final String cancel = getString(R.string.add_imagem_cancel);

                final CharSequence[] items = {img_cam, img_lib, cancel};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.add_imagem);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals(img_cam)) {

                            values = new ContentValues();
                            imageUri = activity.getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, REQUEST_CAMERA);

                        } else if (items[item].equals(img_lib)) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);

                        } else if (items[item].equals(cancel)) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Busca");

        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(onQuerySearchView);

        menu.findItem(R.id.menu_add).setVisible(true);

        //mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_search:
                //mSearchCheck = true;
                Toast.makeText(getActivity(), "Busca" , Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
//            if (mSearchCheck){
//                // implement your search here
//            }
            return false;
        }
    };

    public void addTelefone( View view ){

        TableLayout tableLayoutCE = (TableLayout)view.findViewById(R.id.tableAddTelefone);


        // Linha 1
        // TableRow
        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);

        TableRow tableRow = new TableRow( view.getContext() );
        tableRow.setPadding(0,0,0,10);
        tableRow.setLayoutParams(tableRowParams);

        // Text
        TextView textViewTipoTel = new TextView(view.getContext());
        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        textViewParams.gravity = Gravity.LEFT;
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        textViewParams.column  = 1;
        textViewTipoTel.setLayoutParams(textViewParams);
        textViewTipoTel.setTextSize(16);
        textViewTipoTel.setText("Tipo:");
        textViewTipoTel.setTextColor(Color.BLACK);

        // Edit
        EditText editTextTipoTel = new EditText(view.getContext());
        TableRow.LayoutParams editTextParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        editTextParams.gravity = Gravity.CENTER_VERTICAL;
        editTextParams.column  = 2;
        editTextParams.weight  = 1;
        editTextTipoTel.setLayoutParams(editTextParams);
        editTextTipoTel.setBackgroundResource(R.drawable.border_fields);
        editTextTipoTel.setPadding(0,0,0,0);
        editTextTipoTel.requestFocus();

        // ADD
        allEdsTelefone.add(editTextTipoTel);
        tableRow.addView(textViewTipoTel);
        tableRow.addView(editTextTipoTel);
        tableLayoutCE.addView(tableRow);

        // linha 2
        // TableRow
        TableRow tableRow2 = new TableRow( view.getContext() );
        tableRow2.setPadding(0,0,0,10);
        tableRow2.setLayoutParams(tableRowParams);

        // Text
        TextView textViewNumeroTel = new TextView(view.getContext());
        TableRow.LayoutParams textView2Params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        textView2Params.gravity = Gravity.LEFT;
        textView2Params.gravity = Gravity.CENTER_VERTICAL;
        textView2Params.column  = 1;
        textViewNumeroTel.setLayoutParams(textView2Params);
        textViewNumeroTel.setTextSize(16);
        textViewNumeroTel.setText("Numero:");
        textViewNumeroTel.setTextColor(Color.BLACK);

        // Edit
        EditText editTextNumeroTel = new EditText(view.getContext());
        TableRow.LayoutParams editText2Params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        editText2Params.gravity = Gravity.CENTER_VERTICAL;
        editText2Params.column  = 2;
        editText2Params.weight  = 1;
        editTextNumeroTel.setLayoutParams(editText2Params);
        editTextNumeroTel.setBackgroundResource(R.drawable.border_fields);
        editTextNumeroTel.setPadding(0,0,0,0);

        // ADD
        allEdsTelefone.add(editTextNumeroTel);
        tableRow2.addView(textViewNumeroTel);
        tableRow2.addView(editTextNumeroTel);
        tableLayoutCE.addView(tableRow2);

    }

    private boolean validateFields() {

        String sNome           = nome.getText().toString().trim();
        String sDescricao      = descricao.getText().toString().trim();
        String sRua            = rua.getText().toString().trim();
        String sNumero         = numero.getText().toString().trim();
        String sBairro         = bairro.getText().toString().trim();
        String sCidade         = cidade.getText().toString().trim();
        String sEstado         = estado.getText().toString().trim();
        String sPais           = pais.getText().toString().trim();

        return ( !isEmptyFields(
                sNome, sDescricao, sRua, sNumero,
                sBairro, sCidade, sEstado, sPais
        ));
    }

    private boolean isEmptyFields(String sNome, String sDescricao, String sRua, String sNumero,
                                  String sBairro, String sCidade, String sEstado, String sPais) {

        if (TextUtils.isEmpty(sNome)) {
            nome.requestFocus();
            nome.setError(resources.getString( R.string.nome_obrigatorio ));
            return true;
        } else if (TextUtils.isEmpty(sRua)) {
            rua.requestFocus();
            rua.setError(resources.getString(R.string.rua_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sNumero)) {
            numero.requestFocus();
            numero.setError(resources.getString(R.string.numero_local_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sBairro)) {
            bairro.requestFocus();
            bairro.setError(resources.getString(R.string.bairro_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sCidade)) {
            cidade.requestFocus();
            cidade.setError(resources.getString(R.string.cidade_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sEstado)) {
            estado.requestFocus();
            estado.setError(resources.getString(R.string.estado_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sPais)) {
            pais.requestFocus();
            pais.setError(resources.getString(R.string.pais_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sDescricao)) {
            descricao.requestFocus();
            descricao.setError(resources.getString(R.string.dscr_obrigatorio));
            return true;
        }
        return false;
    }

    private void doRequest(){
        String url = Url.getUrl()+"Empresa/cadastrarEmpresa";
        final Map<String, String> params = new HashMap();

        params.put("empresa", gson.toJson(emp));

        StringRequest jsonRequest = new AutorizacaoRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    public void onResponse(String result) {
                        Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 401) {
                                Toast.makeText(getContext(), "Não autorizado", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), "Erro de conexao", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                String encodedCredentials = Base64.encodeToString(gson.toJson(emp).getBytes(), Base64.NO_WRAP);
                params.put("empresa", encodedCredentials);
                return params;
            }
        };

        jsonRequest.setTag(TAG);

        rq.add(jsonRequest);

    }
}
