package clientefeedback.aplicacaocliente.Models;

/**
 * Created by Alexandre on 24/04/2016.
 */
public class Filtro {
    private String nomeempresa;

    public String getNomeempresa() {
        return nomeempresa;
    }

    public void setNomeempresa(String nomeempresa) {
        this.nomeempresa = nomeempresa;
    }

    private String culinaria;
    private String estado;
    private String cidade;
    private String bairro;
    private String valorMinimo;
    private String valorMaximo;
    private String ordenacao;

    final String NOME = "nome";
    final String MENOR_VALOR = "Menor Valor";
    final String MAIOR_VALOR = "Maior Valor";
    final String MAIOR_NOTA = "Maior Nota";
    final String MENOR_NOTA = "Menor Nota";
    final String MAIS_COMENTADOS = "Mais Comentados";
    final String MENOS_COMENTADOS = "Menos Comentados";

    public String getCulinaria() {
        return culinaria;
    }

    public void setCulinaria(String culinaria) {
        this.culinaria = culinaria;
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

    public String getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(String valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public String getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(String valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordecacao) {
        this.ordenacao = ordecacao;
    }
}
