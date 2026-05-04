package copamundo.usuarios.controle;

public class Usuario {
    private String nome;
    private String cpf;
    private String email;
    private String status;
    private String pais;
    private String funcao;
    private String senha;

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }
    public String getEmail() {
        return email;
    }
    public String getStatus() {
        return status;
    }
    public String getPais() {
        return pais;
    }
    public String getFuncao() {
        return funcao;
    }

    public boolean verificarSenha(String senha) {
        if (this.senha.equals(senha)) {
            return true;
        }
        return false;
    }

    public boolean isEmailValido(String email) {
        return false;
    }

    public boolean isSenhaValida(String email) {
        return false;
    }

    public boolean isCpfValido(String cpf) {
        return false;
    }
}
