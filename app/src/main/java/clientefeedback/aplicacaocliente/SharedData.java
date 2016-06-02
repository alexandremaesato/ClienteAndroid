package clientefeedback.aplicacaocliente;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alexandre on 21/05/2016.
 */
public class SharedData {
    SharedPreferences sharedPreferences;
    Context context;
    public SharedData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
    }

    public int getPessoaId(){
        return sharedPreferences.getInt("pessoaId", 0);
    }

    public void setSearch(String s){
        sharedPreferences = context.getSharedPreferences("busca", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("busca", s);
        editor.commit();
    }

    public String getSearch(){
        sharedPreferences = context.getSharedPreferences("busca", Context.MODE_PRIVATE);
        return sharedPreferences.getString("busca", "");
    }

    public void limparBusca(){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("busca", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
