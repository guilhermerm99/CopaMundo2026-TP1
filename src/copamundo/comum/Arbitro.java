package copamundo.comum;

import java.io.Serializable;
import java.util.Objects;

public final class Arbitro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String categoria;
    private String federacao;

    // Construtor vazio
    public Arbitro() {
    }

    // Construtor completo
    public Arbitro(int id, String nome, String categoria, String federacao) {
        setId(id);
        setNome(nome);
        setCategoria(categoria);
        setFederacao(federacao);
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
        if (id < 0) {
            throw new IllegalArgumentException("O id do arbitro nao pode ser negativo.");
        }
        this.id = id;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do arbitro e obrigatorio.");
        }
        this.nome = nome.trim();
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria do arbitro e obrigatoria.");
        }
        this.categoria = categoria.trim();
    }

    public void setFederacao(String federacao) {
        if (federacao == null || federacao.isBlank()) {
            throw new IllegalArgumentException("A federacao/nacionalidade do arbitro e obrigatoria.");
        }
        this.federacao = federacao.trim();
    }

    public String getNacionalidade() {
        return federacao;
    }

    public boolean possuiNacionalidade(String nacionalidade) {
        return federacao != null && federacao.equalsIgnoreCase(nacionalidade);
    }

    @Override
    public String toString() {
        return nome + " (" + federacao + ")";
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Arbitro)) {
            return false;
        }
        Arbitro arbitro = (Arbitro) objeto;
        return id == arbitro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
