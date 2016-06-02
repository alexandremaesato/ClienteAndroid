package clientefeedback.aplicacaocliente.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guilherme on 23/02/2016.
 */
public class Empresa implements Parcelable{

    private int empresaId;
    private String nomeEmpresa;
    private String cnpj;
    private String descricao;
    private Imagem imagemPerfil;
    private List<Imagem> imagensNaoOficiais;
    private List<Imagem> imagensOficiais;
    private List<Comentario> comentarios;
    private List<Avaliacao> avaliacoes;
    private List<Telefone> telefones;
    private Endereco endereco;
    private List<Produto> produtos;
    private int avaliacaoNota;
    private int qtdeComentarios;
    private int qtdeAvaliacoes;

    public Empresa(){

    }

    protected Empresa(Parcel in) {
        empresaId = in.readInt();
        nomeEmpresa = in.readString();
        cnpj = in.readString();
        descricao = in.readString();
        imagemPerfil = (Imagem)in.readValue(Imagem.class.getClassLoader());
        imagensNaoOficiais = (ArrayList<Imagem>)in.readArrayList(Imagem.class.getClassLoader());
        imagensOficiais = (ArrayList<Imagem>)in.readArrayList(Imagem.class.getClassLoader());
        comentarios = (ArrayList<Comentario>)in.readArrayList(Comentario.class.getClassLoader());
        avaliacoes = (ArrayList<Avaliacao>)in.readArrayList(Avaliacao.class.getClassLoader());
        telefones = (ArrayList<Telefone>)in.readArrayList(Telefone.class.getClassLoader());
        endereco = (Endereco)in.readValue(Endereco.class.getClassLoader());
        produtos = (ArrayList<Produto>)in.readArrayList(Produto.class.getClassLoader());
        avaliacaoNota = in.readInt();
        qtdeComentarios = in.readInt();
        qtdeAvaliacoes = in.readInt();
    }

    public static final Creator<Empresa> CREATOR = new Creator<Empresa>() {
        @Override
        public Empresa createFromParcel(Parcel in) {
            return new Empresa(in);
        }

        @Override
        public Empresa[] newArray(int size) {
            return new Empresa[size];
        }
    };

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
        this.empresaId = empresaId;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nome) {
        this.nomeEmpresa = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public int getAvaliacaoNota() {
        return avaliacaoNota;
    }

    public void setAvaliacaoNota(int avaliacaoNota) {
        this.avaliacaoNota = avaliacaoNota;
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

    public boolean produtosIsEmpty(){
        return produtos.isEmpty();
    }

    public boolean hasImagemPerfil() {
        if(imagemPerfil != null){
            return true;
        }
        return false;
    }

    public Map empresa(){
        HashMap<String,Object> empresa = new HashMap<String,Object>();

        empresa.put("empresaId", this.empresaId);
        empresa.put("nomeEmpresa", this.nomeEmpresa);
        empresa.put("avaliacaoNota", this.avaliacaoNota);
        empresa.put("cnpj", this.cnpj);
        empresa.put("avaliacoes", this.avaliacoes);
        empresa.put("comentarios", this.comentarios);
        empresa.put("descricao", this.descricao);
        empresa.put("endereco", this.endereco);
        empresa.put("imagemPerfil", this.imagemPerfil);
        empresa.put("imagensOficiais", this.imagensOficiais);
        empresa.put("imagensNaoOficiais", this.imagensNaoOficiais);
        empresa.put("produtos", this.produtos);
        empresa.put("qtdeAvaliacoes", this.qtdeAvaliacoes);
        empresa.put("qtdeComentarios", this.qtdeComentarios);
        empresa.put("telefones", this.telefones);

        return empresa;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(empresaId);
        parcel.writeString(nomeEmpresa);
        parcel.writeString(cnpj);
        parcel.writeString(descricao);
        parcel.writeValue(imagemPerfil);
        parcel.writeValue(imagensNaoOficiais);
        parcel.writeValue(imagensOficiais);
        parcel.writeValue(comentarios);
        parcel.writeValue(avaliacoes);
        parcel.writeValue(telefones);
        parcel.writeValue(endereco);
        parcel.writeValue(produtos);
        parcel.writeInt(avaliacaoNota);
        parcel.writeInt(qtdeComentarios);
        parcel.writeInt(qtdeAvaliacoes);
    }
}