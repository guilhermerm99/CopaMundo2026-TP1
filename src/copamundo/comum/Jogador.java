package copamundo.comum;

public class Jogador {
    private String nome;
    private String posicao;
    private int numero;
    private int idade;
    private StatusJogador status;
    private Selecao selecao;   // referência para a seleção

    public Jogador() {}

    public Jogador(String nome, String posicao, int numero, int idade, StatusJogador status, Selecao selecao) {
        this.nome = nome;
        this.posicao = posicao;
        this.numero = numero;
        this.idade = idade;
        this.status = status;
        this.selecao = selecao;
    }

    // Getters e Setters
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

    public Selecao getSelecao() { return selecao; }
    public void setSelecao(Selecao selecao) { this.selecao = selecao; }

    // Método de conveniência (não obrigatório agora, mas útil)
    public boolean isDisponivel() {
        return status == StatusJogador.ATIVO;
    }
}