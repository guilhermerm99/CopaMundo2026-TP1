package copamundo.estadios.modelo;

import java.io.Serializable;
import java.util.Objects;

public final class Arbitro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private String categoria;
    private String federacao;
    private String experiencia;

    // Construtor vazio
    public Arbitro() {
    }

    // Construtor completo
    public Arbitro(int id, String nome, String categoria, String federacao) {
        this(id, nome, categoria, federacao, "1 ano");
    }

    public Arbitro(int id, String nome, String categoria, String federacao, String experiencia) {
        setId(id);
        setNome(nome);
        setCategoria(categoria);
        setFederacao(federacao);
        setExperiencia(experiencia);
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

    public String getExperiencia() {
        return experiencia == null || experiencia.isBlank() ? "1 ano" : experiencia;
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

    public void setExperiencia(String experiencia) {
        if (experiencia == null || experiencia.isBlank()) {
            throw new IllegalArgumentException("A experiencia do arbitro e obrigatoria.");
        }
        this.experiencia = experiencia.trim();
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
