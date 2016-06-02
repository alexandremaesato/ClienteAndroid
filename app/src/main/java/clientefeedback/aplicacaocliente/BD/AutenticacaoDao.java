package clientefeedback.aplicacaocliente.BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import clientefeedback.aplicacaocliente.Models.Autenticacao;

/**
 * Created by Alexandre on 14/03/2016.
 */
public class AutenticacaoDao {
    private AutenticacaoOpenHelper dbHelper;
    private String[] AUTENTICACAO_TABLE_COLUMNS = {AutenticacaoOpenHelper.AUTENTICACAO_ID, AutenticacaoOpenHelper.AUTENTICACAO_USUARIO, AutenticacaoOpenHelper.AUTENTICACAO_SENHA};
    private SQLiteDatabase database;

    public AutenticacaoDao(Context context) {
        dbHelper = new AutenticacaoOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Autenticacao addAutenticacao(String login, String senha) {
        ContentValues values = new ContentValues();
        values.put(AutenticacaoOpenHelper.AUTENTICACAO_USUARIO, login);
        values.put(AutenticacaoOpenHelper.AUTENTICACAO_SENHA,senha);
        long id = database.insert(AutenticacaoOpenHelper.AUTENTICACOES, null, values);

        Cursor cursor = database.query(AutenticacaoOpenHelper.AUTENTICACOES, AUTENTICACAO_TABLE_COLUMNS, AutenticacaoOpenHelper.AUTENTICACAO_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();

        Autenticacao newComment = parseAutenticacao(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteAutenticacao(Autenticacao autenticacao) {
        long id = Long.parseLong(autenticacao.getId());
        System.out.println("Removido id: " + id);
        database.delete(AutenticacaoOpenHelper.AUTENTICACOES, AutenticacaoOpenHelper.AUTENTICACAO_ID + " = " + id, null);
    }

    public List getAllAutenticacoes() {
        List autenticacoes = new ArrayList();

        Cursor cursor = database.query(AutenticacaoOpenHelper.AUTENTICACOES, AUTENTICACAO_TABLE_COLUMNS
                , null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Autenticacao autenticacao = parseAutenticacao(cursor);
            autenticacoes.add(autenticacao);
            cursor.moveToNext();
        }
        cursor.close();
        return autenticacoes;
    }


    public Autenticacao parseAutenticacao(Cursor cursor) {
        Autenticacao autenticacao = new Autenticacao();
        autenticacao.setId(String.valueOf(cursor.getInt(0)));
        autenticacao.setLogin(cursor.getString(1));
        autenticacao.setSenha(cursor.getString(2));
        return autenticacao;
    }

    public String getAutenticacao(){
        Cursor cursor = database.query(AutenticacaoOpenHelper.AUTENTICACOES, AUTENTICACAO_TABLE_COLUMNS
                , null, null, null, null, null);
        cursor.moveToFirst();
        Autenticacao autenticacao = null;
        String result = null;
        while (!cursor.isAfterLast()) {
            autenticacao = parseAutenticacao(cursor);
            cursor.moveToNext();
        }
        if (autenticacao != null) {
            result = autenticacao.getLogin() + ":" + autenticacao.getSenha();
        }

        cursor.close();
        return result;
    }
}
