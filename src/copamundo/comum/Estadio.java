package copamundo.comum;

import java.io.Serializable;
import java.util.Objects;

public final class Estadio implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String cidade;
    private int capacidade;

    // Construtor vazio
    public Estadio() {
    }

    // Construtor completo
    public Estadio(int id, String nome, String cidade, int capacidade) {
        setId(id);
        setNome(nome);
        setCidade(cidade);
        setCapacidade(capacidade);
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
        if (id < 0) {
            throw new IllegalArgumentException("O id do estadio nao pode ser negativo.");
        }
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do estadio e obrigatorio.");
        }
        this.nome = nome.trim();
    }

    public void setCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("A cidade do estadio e obrigatoria.");
        }
        this.cidade = cidade.trim();
    }

    public void setCapacidade(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        }
        this.capacidade = capacidade;
    }

    @Override
    public String toString() {
        return nome + " - " + cidade;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Estadio)) {
            return false;
        }
        Estadio estadio = (Estadio) objeto;
        return id == estadio.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
