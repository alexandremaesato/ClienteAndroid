package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guilherme on 01/04/2016.
 */
public class Telefone implements Parcelable {

    private int telefoneid;
    private String tipoTelefone;
    private String numero;

    public Telefone(String tipoTelefone, String numero) {
        this.tipoTelefone = tipoTelefone;
        this.numero = numero;
    }

    protected Telefone(Parcel in) {
        telefoneid = in.readInt();
        tipoTelefone = in.readString();
        numero = in.readString();
    }

    public static final Creator<Telefone> CREATOR = new Creator<Telefone>() {
        @Override
        public Telefone createFromParcel(Parcel in) {
            return new Telefone(in);
        }

        @Override
        public Telefone[] newArray(int size) {
            return new Telefone[size];
        }
    };

    public int getTelefoneid() {
        return telefoneid;
    }

    public void setTelefoneid(int telefoneid) {
        this.telefoneid = telefoneid;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(telefoneid);
        parcel.writeString(tipoTelefone);
        parcel.writeString(numero);
    }
}
