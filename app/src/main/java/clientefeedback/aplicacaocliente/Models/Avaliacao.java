package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexandre on 14/04/2016.
 */
public class Avaliacao implements Parcelable {

    //TIPOS DE AVALIACOES

    public static String EMPRESA = "empresa";
    public static String PRODUTO = "produto";

    private int avaliacaoid;
    private int avaliadoid;
    private int pessoaid;
    private int nota;
    private String descricao;
    private String tipoAvalicao;

    public Avaliacao(){

    }

    protected Avaliacao(Parcel in) {
        avaliacaoid = in.readInt();
        avaliadoid = in.readInt();
        pessoaid = in.readInt();
        nota = in.readInt();
        descricao = in.readString();
        tipoAvalicao = in.readString();
    }

    public static final Creator<Avaliacao> CREATOR = new Creator<Avaliacao>() {
        @Override
        public Avaliacao createFromParcel(Parcel in) {
            return new Avaliacao(in);
        }

        @Override
        public Avaliacao[] newArray(int size) {
            return new Avaliacao[size];
        }
    };

    public int getAvaliacaoid() {
        return avaliacaoid;
    }

    public void setAvaliacaoid(int avaliacaoid) {
        this.avaliacaoid = avaliacaoid;
    }

    public int getAvaliadoid() {
        return avaliadoid;
    }

    public void setAvaliadoid(int avaliadoid) {
        this.avaliadoid = avaliadoid;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoAvalicao() {
        return tipoAvalicao;
    }

    public void setTipoAvalicao(String tipoAvalicao) {
        this.tipoAvalicao = tipoAvalicao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(avaliacaoid);
        parcel.writeInt(avaliadoid);
        parcel.writeInt(pessoaid);
        parcel.writeInt(nota);
        parcel.writeString(descricao);
        parcel.writeString(tipoAvalicao);
    }
}

