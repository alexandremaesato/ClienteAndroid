package clientefeedback.aplicacaocliente.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;


import java.util.Date;
import java.util.List;

import clientefeedback.aplicacaocliente.R;

/**
 * Created by Guilherme on 26/02/2016.
 */
public class Pessoa implements Parcelable {

    private int pessoaid;
    private String login;
    private String senha;
    private String cpf;
    private String nome;
    private String sobrenome;
    private Date dataNascimento;
    private Imagem imagemPerfil;
    private int numeroFavoritados;
    private int numeroDesejados;
    private List listaFavoritos;
    private List listaDesejos;

    public Pessoa(){

    }

    protected Pessoa(Parcel in) {
        senha = in.readString();
        cpf = in.readString();
        nome = in.readString();
        sobrenome = in.readString();
        Date dataNascimento = in.readParcelable(Date.class.getClassLoader());
        imagemPerfil = in.readParcelable(Imagem.class.getClassLoader());
        numeroFavoritados  = in.readInt();
        numeroDesejados  = in.readInt();
    }

    public static final Parcelable.Creator<Pessoa> CREATOR = new Parcelable.Creator<Pessoa>() {
        @Override
        public Pessoa createFromParcel(Parcel in) {
            return new Pessoa(in);
        }

        @Override
        public Pessoa[] newArray(int size) {
            return new Pessoa[size];
        }
    };


    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Imagem getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(Imagem imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public int getNumeroFavoritados() {
        return numeroFavoritados;
    }

    public void setNumeroFavoritados(int numeroFavoritados) {
        this.numeroFavoritados = numeroFavoritados;
    }

    public int getNumeroDesejados() {
        return numeroDesejados;
    }

    public void setNumeroDesejados(int numeroDesejados) {
        this.numeroDesejados = numeroDesejados;
    }

    public List getListaFavoritos() {
        return listaFavoritos;
    }

    public void setListaFavoritos(List listaFavoritos) {
        this.listaFavoritos = listaFavoritos;
    }

    public List getListaDesejos() {
        return listaDesejos;
    }

    public void setListaDesejos(List listaDesejos) {
        this.listaDesejos = listaDesejos;
    }

    public static String getLoginFromSharedPreferences(Context c){
        SharedPreferences s = c.getSharedPreferences("account", Context.MODE_PRIVATE);
        return s.getString(c.getString(R.string.login),"");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pessoaid);
        parcel.writeString(login);
        parcel.writeString(senha);
        parcel.writeString(cpf);
        parcel.writeString(nome);
        parcel.writeString(sobrenome);
        parcel.writeValue(dataNascimento);
        parcel.writeValue(imagemPerfil);
        parcel.writeInt(numeroFavoritados);
        parcel.writeInt(numeroDesejados);
    }
}
