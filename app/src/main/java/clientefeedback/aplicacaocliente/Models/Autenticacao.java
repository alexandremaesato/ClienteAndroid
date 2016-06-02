package clientefeedback.aplicacaocliente.Models;

/**
 * Created by Alexandre on 14/03/2016.
 */
public class Autenticacao {
    private String login;
    private String senha;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
