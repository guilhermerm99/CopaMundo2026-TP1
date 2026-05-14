package copamundo.estadios.modelo;

public class Estadio {

    private int id;
    private String nome;
    private String cidade;
    private int capacidade;

    // Construtor vazio
    public Estadio() {
    }

    // Construtor completo
    public Estadio(int id, String nome, String cidade, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
    }

    // GETTERS

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public int getCapacidade() {
        return capacidade;
    }

    // SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return nome + " - " + cidade;
    }
}