package clientefeedback.aplicacaocliente.Busca;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import clientefeedback.aplicacaocliente.R;
import clientefeedback.aplicacaocliente.SharedData;

/**
 * Created by Alexandre on 27/05/2016.
 */
public class BuscaRequest {
    FragmentManager fragmentManager;

    public  BuscaRequest(Context context, String s){
        this.fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        SharedData sd = new SharedData(context);
        sd.setSearch(s);
        fragmentManager.beginTransaction().replace(R.id.conteudo, new BuscaFragment(this.fragmentManager)).addToBackStack("busca").commit();
    }





}
