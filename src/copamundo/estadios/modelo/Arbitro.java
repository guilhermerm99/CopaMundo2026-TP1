package copamundo.estadios.modelo;

public class Arbitro {

    private int id;
    private String nome;
    private String categoria;
    private String federacao;

    // Construtor vazio
    public Arbitro() {
    }

    // Construtor completo
    public Arbitro(int id, String nome, String categoria, String federacao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.federacao = federacao;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFederacao() {
        return federacao;
    }

    // SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setFederacao(String federacao) {
        this.federacao = federacao;
    }
}