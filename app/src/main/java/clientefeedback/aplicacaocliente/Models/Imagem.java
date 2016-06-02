package clientefeedback.aplicacaocliente.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alexandre on 10/03/2016.
 */
public class Imagem implements Parcelable{

    private int imagemid;
    private int tipoImagem;
    private String img;
    private String nomeImagem;
    private String caminho;
    private String descricao;
    private int itemid;
    private int pessoaid;

    public Imagem(){

    }

    protected Imagem(Parcel in) {
        imagemid = in.readInt();
        tipoImagem = in.readInt();
        img = in.readString();
        nomeImagem = in.readString();
        descricao = in.readString();
        caminho = in.readString();
        itemid = in.readInt();
        pessoaid = in.readInt();
    }

    public static final Creator<Imagem> CREATOR = new Creator<Imagem>() {
        @Override
        public Imagem createFromParcel(Parcel in) {
            return new Imagem(in);
        }

        @Override
        public Imagem[] newArray(int size) {
            return new Imagem[size];
        }
    };

    public String getCaminho() {
        String img = caminho+nomeImagem;
        img.replace("//","/");
        return img;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getNomeImagem() {
        return nomeImagem;
    }

    public void setNomeImagem(String nomeImagem) {
        this.nomeImagem = nomeImagem;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getImagemid() {
        return imagemid;
    }

    public void setImagemid(int imagemid) {
        this.imagemid = imagemid;
    }

    public int getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(int tipoImagem) {
        this.tipoImagem = tipoImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getPessoaid() {
        return pessoaid;
    }

    public void setPessoaid(int pessoaid) {
        this.pessoaid = pessoaid;
    }

    public void imageEncode(Bitmap image){

        Bitmap bm = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        img = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap imageDecode(String img){

        byte [] encodeByte = Base64.decode(img,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public boolean hasImagem(){
        return !nomeImagem.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imagemid);
        parcel.writeInt(tipoImagem);
        parcel.writeString(img);
        parcel.writeString(nomeImagem);
        parcel.writeString(descricao);
        parcel.writeString(caminho);
        parcel.writeInt(itemid);
        parcel.writeInt(pessoaid);
    }
}
