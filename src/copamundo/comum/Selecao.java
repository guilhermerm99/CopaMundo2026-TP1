package copamundo.comum;

import java.util.ArrayList;
import java.util.List;

public class Selecao {
    private String pais;
    private String grupo;
    private String tecnico;
    private List<Jogador> jogadores; // Relacionamento 1:N

    public Selecao() {
        this.jogadores = new ArrayList<>();
    }

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

    // Getters e Setters
    public String getPais() {
        return pais;
    }

    public String getNome() {
        return pais;
    }

    public String getNacionalidade() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    // Métodos auxiliares (podem ser mantidos para uso futuro)
    public void adicionarJogador(Jogador jogador) {
        this.jogadores.add(jogador);
    }

    public void removerJogador(Jogador jogador) {
        this.jogadores.remove(jogador);
    }

    public int getQuantidadeJogadores() {
        return jogadores.size();
    }

    public boolean isElencoCheio() {
        return jogadores.size() >= 26;
    }
}
