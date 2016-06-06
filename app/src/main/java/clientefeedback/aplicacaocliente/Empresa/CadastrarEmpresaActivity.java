package clientefeedback.aplicacaocliente.Empresa;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clientefeedback.aplicacaocliente.MainActivity;
import clientefeedback.aplicacaocliente.Models.CustomRequest;
import clientefeedback.aplicacaocliente.Models.Empresa;
import clientefeedback.aplicacaocliente.Models.Endereco;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Mask;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Telefone;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.Url;

public class CadastrarEmpresaActivity extends AppCompatActivity {

    private int REQUEST_CAMERA  = 1;
    private int SELECT_FILE     = 2;
    private List<EditText> allEdsTelefone = new ArrayList<>();
    private RequestQueue rq;
    private Resources resources;
    private static final Object TAG = new Object();
    private ProgressBar pb_cd_empresa;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_empresa);

        pb_cd_empresa = (ProgressBar) findViewById(R.id.pb_cd_empresa);
        rq = Volley.newRequestQueue(CadastrarEmpresaActivity.this);
        gson = new Gson();
        emp = new Empresa();

        initViews();
    }

    private void initViews(){

        Toolbar cetoolbar = (Toolbar) findViewById(R.id.cetoolbar);
        setTitle(R.string.cadastro_empresa);
        setSupportActionBar(cetoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resources      = getResources();

        nome           = (EditText)findViewById(R.id.editTextNomeEmpresa);
        cnpj           = (EditText)findViewById(R.id.editTextCnpj);
        descricao      = (EditText)findViewById(R.id.editTextDescricaoEmpresa);
        rua            = (EditText)findViewById(R.id.editTextRua);
        numero         = (EditText)findViewById(R.id.editTextNumeroCasa);
        complemento    = (EditText)findViewById(R.id.editTextComplemento);
        bairro         = (EditText)findViewById(R.id.editTextBairro);
        cep            = (EditText)findViewById(R.id.editTextCep);
        cidade         = (EditText)findViewById(R.id.editTextCidade);
        estado         = (EditText)findViewById(R.id.editTextEstado);
        pais           = (EditText)findViewById(R.id.editTextPais);
        tipoTelefone   = (EditText)findViewById(R.id.editTextTipoTelefone);
        numeroTelefone = (EditText)findViewById(R.id.editTextNumeroTelefone);
        bt_AddTelefone = (ImageButton) findViewById(R.id.bt_AddTelefone);
        ivImage        = (ImageView) findViewById(R.id.ivImage);

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
                            // tem q ser chamado fora da thread
                            pb_cd_empresa.setVisibility(View.VISIBLE);

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
                            int permissionCheck = ContextCompat.checkSelfPermission(CadastrarEmpresaActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                values = new ContentValues();
                                imageUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            } else {

                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(CadastrarEmpresaActivity.this,
                                        Manifest.permission.READ_CONTACTS)) {

                                    // Show an expanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.

                                } else {

                                    // No explanation needed, we can request the permission.

                                    ActivityCompat.requestPermissions(CadastrarEmpresaActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            1);

                                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }
                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tb_empresa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        int id = item.getItemId();

        if( id == android.R.id.home ){
            finish();
        }

        return true;
    }

    public void addTelefone( View view ){

        TableLayout tableLayoutCE = (TableLayout) findViewById(R.id.tableAddTelefone);

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
        editTextTipoTel.setSingleLine(true);
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
        editTextNumeroTel.setPadding(0, 0, 0, 0);
        editTextNumeroTel.setSingleLine(true);
        TextWatcher telefoneMask = Mask.insert("(##)#####-####", editTextNumeroTel);
        editTextNumeroTel.addTextChangedListener(telefoneMask);
        editTextNumeroTel.setInputType(InputType.TYPE_CLASS_NUMBER);

        // ADD
        allEdsTelefone.add(editTextNumeroTel);
        tableRow2.addView(textViewNumeroTel);
        tableRow2.addView(editTextNumeroTel);
        tableLayoutCE.addView(tableRow2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            ivImage = (ImageView) findViewById(R.id.ivImage);

            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = null;
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ivImage.setImageBitmap(thumbnail);
                img.imageEncode(thumbnail);
                img.setNomeImagem(destination.getPath());

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                ivImage.setImageBitmap(bm);
                img.imageEncode(bm);
                img.setNomeImagem(selectedImagePath);
            }
        }
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
                        pb_cd_empresa.setVisibility(View.GONE);
                        Toast.makeText(CadastrarEmpresaActivity.this, result.toString(), Toast.LENGTH_SHORT).show();

                        Intent it = new Intent(CadastrarEmpresaActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 401) {
                                pb_cd_empresa.setVisibility(View.GONE);
                                Toast.makeText(CadastrarEmpresaActivity.this, "Não autorizado", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            pb_cd_empresa.setVisibility(View.GONE);
                            Toast.makeText(CadastrarEmpresaActivity.this, "Erro de conexao", Toast.LENGTH_SHORT).show();
                        }
                    }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
//                String encodedCredentials = Base64.encodeToString(gson.toJson(emp).getBytes(), Base64.NO_WRAP);
//                params.put("empresa", encodedCredentials);
                params.put("empresa",gson.toJson(emp));
                return params;
            }
        };

        jsonRequest.setTag(TAG);

        rq.add(jsonRequest);

    }
}
