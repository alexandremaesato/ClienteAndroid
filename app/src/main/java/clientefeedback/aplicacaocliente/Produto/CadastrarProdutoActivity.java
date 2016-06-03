package clientefeedback.aplicacaocliente.Produto;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.SnackMessage;
import clientefeedback.aplicacaocliente.Services.SnackMessageInterface;
import clientefeedback.aplicacaocliente.Services.Url;

public class CadastrarProdutoActivity extends AppCompatActivity {

    private int REQUEST_CAMERA  = 1;
    private int SELECT_FILE     = 2;
    private Resources resources;
    private static final Object TAG = new Object();
    private RequestQueue rq;
    Gson gson;
    Imagem img = new Imagem();
    Produto prod;
    Uri imageUri;
    ContentValues values;

    EditText nome;
    EditText descricao;
    EditText preco;
    Spinner categoria;
    Spinner culinaria;
    ImageView ivImage;
    Integer idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        rq = Volley.newRequestQueue(CadastrarProdutoActivity.this);
        gson = new Gson();
        prod = new Produto();

        initViews();
    }

    private void initViews(){

        Toolbar cptoolbar = (Toolbar) findViewById(R.id.cptoolbar);
        setTitle(R.string.cadastro_produto);
        setSupportActionBar(cptoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resources = getResources();

        Intent it = getIntent();
        Bundle param = it.getExtras();
        idEmpresa = new Integer(param.getString("id"));

        nome      = (EditText)findViewById(R.id.editTextNomeProduto);
        descricao = (EditText)findViewById(R.id.editTextDescricaoProduto);
        preco     = (EditText)findViewById(R.id.editTextPreco);
        categoria = (Spinner) findViewById(R.id.spcategoria);
        culinaria = (Spinner) findViewById(R.id.spculinaria);
        ivImage   = (ImageView) findViewById(R.id.ivImage);

        assert cptoolbar != null;
        cptoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch( item.getItemId() ) {
                    case R.id.action_save:

                        if(validateFields()){
                            new Thread() {
                                public void run() {
                                    prod.setNomeProduto(nome.getText().toString());
                                    prod.setDescricao(descricao.getText().toString());
                                    prod.setPreco(new Double(preco.getText().toString().replace(",", ".")));
                                    prod.setCategoria(categoria.getSelectedItemPosition());
                                    prod.setCulinaria(culinaria.getSelectedItemPosition());

                                    prod.setEmpresaid(idEmpresa);

                                    img.setTipoImagem(1);
                                    prod.setImagemPerfil(img);

                                    doRequest();
                                }
                            }.start();
                        }
                        break;
                }
                return false;
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
                            int permissionCheck = ContextCompat.checkSelfPermission(CadastrarProdutoActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permissionCheck!= PackageManager.PERMISSION_GRANTED) {

                                if (ActivityCompat.shouldShowRequestPermissionRationale(CadastrarProdutoActivity.this,
                                        Manifest.permission.READ_CONTACTS)) {
                                } else {
                                    ActivityCompat.requestPermissions(CadastrarProdutoActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            }

                            values = new ContentValues();
                            imageUri = getContentResolver().insert(
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

    }

    private boolean validateFields() {

        String sNome           = nome.getText().toString().trim();
        String sDescricao      = descricao.getText().toString().trim();
        String sPreco          = preco.getText().toString().trim();

        return(!isEmptyFields( sNome, sDescricao, sPreco));
    }

    private boolean isEmptyFields(String sNome, String sDescricao, String sPreco) {

        if (TextUtils.isEmpty(sNome)) {
            nome.requestFocus();
            nome.setError(resources.getString( R.string.nome_produto_obrigatorio ));
            return true;
        } else if (TextUtils.isEmpty(sDescricao)) {
            descricao.requestFocus();
            descricao.setError(resources.getString(R.string.descricao_produto_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sPreco)) {
            preco.requestFocus();
            preco.setError(resources.getString(R.string.preco_obrigatorio));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tb_produto, menu);
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
        String url = Url.getUrl()+"Produto/cadastrarProduto";
        final Map<String, String> params = new HashMap();

        StringRequest jsonRequest = new AutorizacaoRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    public void onResponse(String result) {
                        Toast.makeText(CadastrarProdutoActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 401) {
                                Toast.makeText(CadastrarProdutoActivity.this, R.string.nao_autorizado, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(CadastrarProdutoActivity.this, R.string.erro_conexao, Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                params.put("empresa",gson.toJson(prod));
                return params;
            }
        };

        jsonRequest.setTag(TAG);

        rq.add(jsonRequest);

    }
}
