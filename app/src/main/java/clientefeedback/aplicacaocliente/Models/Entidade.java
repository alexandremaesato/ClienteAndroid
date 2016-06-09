package clientefeedback.aplicacaocliente.Models;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author Alexandre
 */
public class Entidade implements Parcelable{
    private int identidade;
    private int identidade_criada;
    private int deletado;
    private String tabela;
    private int idresponsavel;
    private Date data_criacao;
    private Date data_modificacao;
    private int idcriador;

    protected Entidade(Parcel in) {
        identidade = in.readInt();
        identidade_criada = in.readInt();
        deletado = in.readInt();
        tabela = in.readString();
        idresponsavel = in.readInt();
        data_criacao = (Date)in.readValue(Date.class.getClassLoader());
        data_modificacao = (Date)in.readValue(Date.class.getClassLoader());
        idcriador = in.readInt();

    }

    public static final Parcelable.Creator<Entidade> CREATOR = new Parcelable.Creator<Entidade>() {
        @Override
        public Entidade createFromParcel(Parcel in) {
            return new Entidade(in);
        }

        @Override
        public Entidade[] newArray(int size) {
            return new Entidade[size];
        }
    };


    public int getIdentidade() {
        return identidade;
    }

    public void setIdentidade(int identidade) {
        this.identidade = identidade;
    }

    public int getIdentidade_criada() {
        return identidade_criada;
    }

    public void setIdentidade_criada(int identidade_criada) {
        this.identidade_criada = identidade_criada;
    }

    public int getDeletado() {
        return deletado;
    }

    public void setDeletado(int deletado) {
        this.deletado = deletado;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public int getIdresponsavel() {
        return idresponsavel;
    }

    public void setIdresponsavel(int idresponsavel) {
        this.idresponsavel = idresponsavel;
    }

    public Date getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(Date data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Date getData_modificacao() {
        return data_modificacao;
    }

    public void setData_modificacao(Date data_modificacao) {
        this.data_modificacao = data_modificacao;
    }

    public int getIdcriador() {
        return idcriador;
    }

    public void setIdcriador(int idcriador) {
        this.idcriador = idcriador;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(identidade);
        parcel.writeInt(identidade_criada);
        parcel.writeInt(deletado);
        parcel.writeString(tabela);
        parcel.writeInt(idresponsavel);
        parcel.writeValue(data_criacao);
        parcel.writeValue(data_modificacao);
        parcel.writeInt(idcriador);
    }
}
