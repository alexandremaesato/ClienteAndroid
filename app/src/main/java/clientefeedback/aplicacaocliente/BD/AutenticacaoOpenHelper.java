package clientefeedback.aplicacaocliente.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandre on 14/03/2016.
 */
public class AutenticacaoOpenHelper extends SQLiteOpenHelper {


    public static final String AUTENTICACOES = "Autenticacoes";
    public static final String AUTENTICACAO_ID = "_id";
    public static final String AUTENTICACAO_USUARIO = "_usuario";
    public static final String AUTENTICACAO_SENHA = "_senha";

    private static final String DATABASE_NAME = "Autenticacoes.db";
    private static final int DATABASE_VERSION = 1;

    //creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + AUTENTICACOES +
            "(" + AUTENTICACAO_ID + " integer primary key autoincrement, " +
            AUTENTICACAO_USUARIO + " text not null, " +
            AUTENTICACAO_SENHA + " text not null);";

    public AutenticacaoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AUTENTICACOES);
        onCreate(db);
    }

}
