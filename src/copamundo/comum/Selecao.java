package copamundo.comum;

import copamundo.selecoes.persistencia.Identificavel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Selecao implements Serializable, Identificavel {
    private static final long serialVersionUID = 2L;

    private String pais;
    private String grupo;
    private String tecnico;
    private List<Jogador> jogadores;

    public Selecao() { this.jogadores = new ArrayList<>(); }

    public Selecao(String pais, String grupo, String tecnico) {
        this.pais = pais;
        this.grupo = grupo;
        this.tecnico = tecnico;
        this.jogadores = new ArrayList<>();
    }

    public Selecao(String pais, String nacionalidade) {
        this.pais = pais;
        this.grupo = "";
        this.tecnico = "";
        this.jogadores = new ArrayList<>();
    }

    @Override
    public String getId() { return pais; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getNome() { return pais; }
    public String getNacionalidade() { return pais; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }

    public List<Jogador> getJogadores() { return jogadores; }
    public void setJogadores(List<Jogador> jogadores) { this.jogadores = jogadores; }

    public void adicionarJogador(Jogador jogador) { this.jogadores.add(jogador); }
    public void removerJogador(Jogador jogador) { this.jogadores.remove(jogador); }
    public int getQuantidadeJogadores() { return jogadores.size(); }
    public boolean isElencoCheio() { return jogadores.size() >= 26; }

    // Duas seleções são iguais se têm o mesmo país
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Selecao)) return false;
        Selecao outra = (Selecao) obj;
        return Objects.equals(pais, outra.pais);
    }

    @Override
    public int hashCode() { return Objects.hash(pais); }

    @Override
    public String toString() { return pais; }
}
