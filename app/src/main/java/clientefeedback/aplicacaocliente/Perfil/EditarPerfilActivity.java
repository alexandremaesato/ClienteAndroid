package clientefeedback.aplicacaocliente.Perfil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import clientefeedback.aplicacaocliente.Interfaces.CallBack;
import clientefeedback.aplicacaocliente.Models.Imagem;
import clientefeedback.aplicacaocliente.Models.Pessoa;
import clientefeedback.aplicacaocliente.Models.Produto;
import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.RequestData;
import clientefeedback.aplicacaocliente.Services.AutorizacaoRequest;
import clientefeedback.aplicacaocliente.Services.SnackMessage;
import clientefeedback.aplicacaocliente.Services.Url;
import clientefeedback.aplicacaocliente.SharedData;
import clientefeedback.aplicacaocliente.Transaction;
import clientefeedback.aplicacaocliente.VolleyConn;

public class EditarPerfilActivity extends AppCompatActivity implements Transaction{
    private int REQUEST_CAMERA  = 1;
    private int SELECT_FILE     = 2;
    private Resources resources;
    private Imagem img = new Imagem();
    private Uri imageUri;
    private ContentValues values;
    private Pessoa perfil;
    private ProgressBar progressBar;
    private EditText nome;
    private EditText sobrenome;
    private TextView dataNascimento;
    private ImageButton btnData;
    private ImageView ivImage;
    private Date convertedDate;
    private SharedData sharedData;
    private Pessoa pessoa;
    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedData = new SharedData(getApplicationContext());
        perfil = new Pessoa();
        perfil.setPessoaid(sharedData.getPessoaId());
        sharedData.getPessoaId();
        initViews();
        loadPessoa();



    }

    public void initViews(){
        Toolbar cptoolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(R.string.title_activity_editar_perfil);
        setSupportActionBar(cptoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resources = getResources();
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        nome = (EditText)findViewById(R.id.etNome);
        sobrenome = (EditText)findViewById(R.id.etSobrenome);
        dataNascimento = (TextView)findViewById(R.id.tvDataNascimento);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        btnData = (ImageButton)findViewById(R.id.btnData);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        dataNascimento.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(EditarPerfilActivity.this, "clicou", Toast.LENGTH_SHORT).show();
                showDialog(DATE_PICKER_ID);
            }
        });

        assert cptoolbar != null;
        cptoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_save:

                        if (validateFields()) {
                            // tem q ser chamado fora da thread
                            progressBar.setVisibility(View.VISIBLE);
                            final java.util.Date dateUtil;

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                            try {
                                dateUtil = formatter.parse(year + "/" + month + "/" + day);
                                convertedDate = new Date(dateUtil.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //convertedDate.
                            new Thread() {
                                public void run() {
                                    perfil.setNome(nome.getText().toString());
                                    perfil.setSobrenome(sobrenome.getText().toString());
                                    //perfil.setDataNascimento(new Date());
                                    img.setTipoImagem(1);
                                    perfil.setImagemPerfil(img);
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
                            int permissionCheck = ContextCompat.checkSelfPermission(EditarPerfilActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                values = new ContentValues();
                                imageUri = getContentResolver().insert(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, REQUEST_CAMERA);

                            } else {

                                if (ActivityCompat.shouldShowRequestPermissionRationale(EditarPerfilActivity.this,
                                        Manifest.permission.READ_CONTACTS)) {
                                } else {
                                    ActivityCompat.requestPermissions(EditarPerfilActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
        String sSobrenome      = sobrenome.getText().toString().trim();
        String sDataNascimento = dataNascimento.getText().toString().trim();
        return(!isEmptyFields( sNome, sSobrenome, sDataNascimento));
    }

    private boolean isEmptyFields(String sNome, String sSobrenome, String sDataNascimento) {

        if (TextUtils.isEmpty(sNome)) {
            nome.requestFocus();
            nome.setError(resources.getString( R.string.nome_perfil_obrigatorio ));
            return true;
        } else if (TextUtils.isEmpty(sSobrenome)) {
            sobrenome.requestFocus();
            sobrenome.setError(resources.getString(R.string.sobrenome_obrigatorio));
            return true;
        } else if (TextUtils.isEmpty(sDataNascimento)) {
            dataNascimento.requestFocus();
            dataNascimento.setError(resources.getString(R.string.dataNascimento_obrigatorio));
            return true;
        }
        return false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tb_perfil, menu);
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
        (new VolleyConn(this,this)).execute();
    }
//    private void doRequest(){
//        String url = Url.getUrl()+"Produto/cadastrarProduto";
//        final Map<String, String> params = new HashMap();
//
//        StringRequest jsonRequest = new AutorizacaoRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    public void onResponse(String result) {
//                        progressBar.setVisibility(View.GONE);
//                        finish();
//                        Toast.makeText(EditarPerfilActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        if (error.networkResponse != null) {
//                            if (error.networkResponse.statusCode == 401) {
//                                progressBar.setVisibility(View.GONE);
//                                finish();
//                                Toast.makeText(getBaseContext(), R.string.nao_autorizado, Toast.LENGTH_SHORT).show();
//                            }
//                        }else {
//                            progressBar.setVisibility(View.GONE);
//                            finish();
//                            Toast.makeText(getBaseContext(), R.string.erro_conexao, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }){
//            @Override
//            public Map<String, String> getParams() throws AuthFailureError {
//                Gson gson = new Gson();
//                params.put("pessoa",gson.toJson(perfil));
//                return params;
//            }
//        };
//        (new VolleyConn(this,this)).execute();
//    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Toast.makeText(getApplicationContext(), "tostando"+i, Toast.LENGTH_SHORT).show();
                        setDate(i, i1, i2);

                    }
                },1980, 1,1).show();

        }
        return null;
    }

    //Apos dar ok no calendario, aqui recebe a data, adciona nas variaveis globais e seta a nova nada no TextView
    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;

        dataNascimento.setText(new StringBuilder()
                .append(day).append("-").append(month + 1).append("-")
                .append(year).append(" "));

    }


    @Override
    public void doBefore() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void doAfter(String answer) {

        progressBar.setVisibility(View.GONE);
        if(answer != null){
            Toast.makeText(this, answer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public RequestData getRequestData() {
        HashMap<String,String> params = new HashMap<>();
        Gson gson = new Gson();
        params.put("pessoa", gson.toJson(perfil));
        return new RequestData(Url.getUrl()+"pessoa/editarPessoa",  "", params);
    }

    public void loadPessoa(){
        PerfilRequest pr = new PerfilRequest(this);
//        Bundle b = pr.getPessoa();
//        pessoa =(Pessoa) b.getParcelable("pessoa");


    }
}

