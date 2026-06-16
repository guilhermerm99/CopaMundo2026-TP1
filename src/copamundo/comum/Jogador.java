package copamundo.comum;

import copamundo.selecoes.persistencia.Identificavel;
import java.io.Serializable;

public class Jogador implements Serializable, Identificavel {
    private static final long serialVersionUID = 2L;

    private String nome;
    private String posicao;
    private int numero;
    private int idade;
    private StatusJogador status;
    private String paisSelecao;

    public Jogador() {}

    public Jogador(String nome, String posicao, int numero, int idade, StatusJogador status, Selecao selecao) {
        this.nome = nome;
        this.posicao = posicao;
        this.numero = numero;
        this.idade = idade;
        this.status = status;
        this.paisSelecao = selecao != null ? selecao.getPais() : "";
    }

    // ID composto para evitar conflito entre jogadores com o mesmo nome em seleções diferentes
    @Override
    public String getId() {
        return nome + "|" + paisSelecao;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPosicao() { return posicao; }
    public void setPosicao(String posicao) { this.posicao = posicao; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    public StatusJogador getStatus() { return status; }
    public void setStatus(StatusJogador status) { this.status = status; }

    public String getPaisSelecao() { return paisSelecao; }
    public void setPaisSelecao(String paisSelecao) { this.paisSelecao = paisSelecao; }

    public String getSelecao() { return paisSelecao; }
    public void setSelecao(Selecao selecao) {
        this.paisSelecao = selecao != null ? selecao.getPais() : "";
    }

    // Retorna o status com ícone para exibição na tabela
    public String getStatusFormatado() {
        if (status == null) return "";
        return switch (status) {
            case ATIVO     -> "✅ Ativo";
            case LESIONADO -> "🟡 Lesionado";
            case SUSPENSO  -> "🔴 Suspenso";
        };
    }

    public boolean isDisponivel() { return status == StatusJogador.ATIVO; }

    @Override
    public String toString() { return nome; }
}
