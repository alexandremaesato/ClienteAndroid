package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Alexandre on 14/04/2016.
 */
public class Produto implements Parcelable {

    public static String[] CATEGORIAS = {"Bebida", "Aperitivos e porções", "Sanduíches", "Pratos"};

    private int produtoid;
    private String nomeProduto;
    private int categoria;
    private int culinaria;
    private String descricao;
    private Double preco;
    private Imagem imagemPerfil;
    private List<Imagem> imagensNaoOficiais;
    private List<Imagem> imagensOficiais;
    private List<Comentario> comentarios;
    private List<Avaliacao> avaliacoes;
    private int qtdeComentarios;
    private int qtdeAvaliacoes;
    private int empresaid;

    public Produto(){

    }

    protected Produto(Parcel in) {
        produtoid = in.readInt();
        nomeProduto = in.readString();
        categoria = in.readInt();
        culinaria = in.readInt();
        descricao = in.readString();
        preco = in.readDouble();
        imagemPerfil = in.readParcelable(Imagem.class.getClassLoader());
        imagensNaoOficiais = in.createTypedArrayList(Imagem.CREATOR);
        imagensOficiais = in.createTypedArrayList(Imagem.CREATOR);
        comentarios = in.createTypedArrayList(Comentario.CREATOR);
        avaliacoes = in.createTypedArrayList(Avaliacao.CREATOR);
        qtdeComentarios = in.readInt();
        qtdeAvaliacoes = in.readInt();
        empresaid = in.readInt();
    }

    public static final Creator<Produto> CREATOR = new Creator<Produto>() {
        @Override
        public Produto createFromParcel(Parcel in) {
            return new Produto(in);
        }

        @Override
        public Produto[] newArray(int size) {
            return new Produto[size];
        }
    };

    public int getProdutoid() {
        return produtoid;
    }

    public void setProdutoid(int produtoid) {
        this.produtoid = produtoid;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nome) {
        this.nomeProduto = nome;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getCulinaria() {
        return culinaria;
    }

    public void setCulinaria(int culinaria) {
        this.culinaria = culinaria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Imagem getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(Imagem imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    public List<Imagem> getImagensNaoOficiais() {
        return imagensNaoOficiais;
    }

    public void setImagensNaoOficiais(List<Imagem> imagensNaoOficiais) {
        this.imagensNaoOficiais = imagensNaoOficiais;
    }

    public List<Imagem> getImagensOficiais() {
        return imagensOficiais;
    }

    public void setImagensOficiais(List<Imagem> imagensOficiais) {
        this.imagensOficiais = imagensOficiais;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public int getQtdeComentarios() {
        return qtdeComentarios;
    }

    public void setQtdeComentarios(int qtdeComentarios) {
        this.qtdeComentarios = qtdeComentarios;
    }

    public int getQtdeAvaliacoes() {
        return qtdeAvaliacoes;
    }

    public void setQtdeAvaliacoes(int qtdeAvaliacoes) {
        this.qtdeAvaliacoes = qtdeAvaliacoes;
    }


    public int getEmpresaid() {
        return empresaid;
    }

    public void setEmpresaid(int empresaid) {
        this.empresaid = empresaid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(produtoid);
        parcel.writeString(nomeProduto);
        parcel.writeInt(categoria);
        parcel.writeInt(culinaria);
        parcel.writeString(descricao);
        parcel.writeDouble(preco);
        parcel.writeValue(imagemPerfil);
        parcel.writeValue(imagensNaoOficiais);
        parcel.writeValue(imagensOficiais);
        parcel.writeValue(comentarios);
        parcel.writeValue(avaliacoes);
        parcel.writeInt(qtdeComentarios);
        parcel.writeInt(qtdeAvaliacoes);
        parcel.writeInt(empresaid);
    }
}

