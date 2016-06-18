package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Alexandre on 16/06/2016.
 */
public class Favorito implements Parcelable {

    private int idFavorito;
    private int idPessoa;
    private int idFavoritado;
    private String tipoFavoritado;
    private boolean check;

    public Favorito(){
        
    }

    protected Favorito(Parcel in) {
        idFavorito = in.readInt();
        idPessoa = in.readInt();
        idFavoritado = in.readInt();
        tipoFavoritado = in.readString();
        check = in.readByte() != 0;
    }

    public static final Creator<Favorito> CREATOR = new Creator<Favorito>() {
        @Override
        public Favorito createFromParcel(Parcel in) {
            return new Favorito(in);
        }

        @Override
        public Favorito[] newArray(int size) {
            return new Favorito[size];
        }
    };

    public boolean isCheck() {
        return check;
    }

    public int getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(int idFavorito) {
        this.idFavorito = idFavorito;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdFavoritado() {
        return idFavoritado;
    }

    public void setIdFavoritado(int idFavoritado) {
        this.idFavoritado = idFavoritado;
    }

    public String getTipoFavoritado() {
        return tipoFavoritado;
    }

    public void setTipoFavoritado(String tipoFavoritado) {
        this.tipoFavoritado = tipoFavoritado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeByte((byte) (check ? 1 : 0));
        parcel.writeInt(idFavorito);
        parcel.writeString(tipoFavoritado);
        parcel.writeInt(idPessoa);
        parcel.writeInt(idFavoritado);

    }
}
