package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guilherme on 01/04/2016.
 */
public class Endereco implements Parcelable{

    private int enderecoid;
    private String cep;
    private String pais;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;

    public Endereco(String rua, String numero, String complemento, String bairro, String cep, String cidade, String estado, String pais) {
        this.cep = cep;
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    protected Endereco(Parcel in) {
        enderecoid = in.readInt();
        cep = in.readString();
        pais = in.readString();
        estado = in.readString();
        cidade = in.readString();
        bairro = in.readString();
        rua = in.readString();
        numero = in.readString();
        complemento = in.readString();
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };

    public int getEnderecoid() {
        return enderecoid;
    }

    public void setEnderecoid(int enderecoid) {
        this.enderecoid = enderecoid;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(enderecoid);
        parcel.writeString(cep);
        parcel.writeString(pais);
        parcel.writeString(estado);
        parcel.writeString(cidade);
        parcel.writeString(bairro);
        parcel.writeString(rua);
        parcel.writeString(numero);
        parcel.writeString(complemento);
    }
}
