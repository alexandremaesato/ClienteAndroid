package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexandre on 14/04/2016.
 */
public class Comentario implements Parcelable{

    private int comentarioid;
    private String descricao;
    private int pessoaid;
    private int comentarioDependenteid;
    private int comentadoid;
    private int modificado;
    private String tipoComentado;

    protected Comentario(Parcel in) {
        comentarioid = in.readInt();
        descricao = in.readString();
        pessoaid = in.readInt();
        comentarioDependenteid = in.readInt();
        comentadoid = in.readInt();
        modificado = in.readInt();
        tipoComentado = in.readString();
    }

    public static final Creator<Comentario> CREATOR = new Creator<Comentario>() {
        @Override
        public Comentario createFromParcel(Parcel in) {
            return new Comentario(in);
        }

        @Override
        public Comentario[] newArray(int size) {
            return new Comentario[size];
        }
    };

    public int getComentarioid() {
        return comentarioid;
    }

    public void setComentarioid(int comentarioid) {
        this.comentarioid = comentarioid;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public int getComentarioDependenteid() {
        return comentarioDependenteid;
    }

    public void setComentarioDependenteid(int comentarioDependenteid) {
        this.comentarioDependenteid = comentarioDependenteid;
    }

    public int getComentadoid() {
        return comentadoid;
    }

    public void setComentadoid(int comentadoid) {
        this.comentadoid = comentadoid;
    }

    public int getModificado() {
        return modificado;
    }

    public void setModificado(int modificado) {
        this.modificado = modificado;
    }

    public String getTipoComentado() {
        return tipoComentado;
    }

    public void setTipoComentado(String tipoComentado) {
        this.tipoComentado = tipoComentado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(comentarioid);
        parcel.writeString(descricao);
        parcel.writeInt(pessoaid);
        parcel.writeInt(comentarioDependenteid);
        parcel.writeInt(comentadoid);
        parcel.writeInt(modificado);
        parcel.writeString(tipoComentado);
    }
}
