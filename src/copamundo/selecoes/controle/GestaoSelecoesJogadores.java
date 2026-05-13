package copamundo.selecoes.controle;

import copamundo.comum.Selecao;
import copamundo.comum.Jogador;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.excecoes.ElencoCheioException;
import copamundo.selecoes.excecoes.SelecaoNaoEncontradaException;
import copamundo.selecoes.excecoes.JogadorNaoEncontradoException;

import java.util.List;
import java.util.ArrayList;

public class GestaoSelecoesJogadores {

    // ========== OPERAÇÕES COM SELEÇÕES ==========

    /**
     * Cadastra uma nova seleção.
     * @throws IllegalArgumentException se já existir seleção com o mesmo país.
     */
    public void cadastrarSelecao(Selecao selecao) throws IllegalArgumentException {
        // TODO: validar país único
    }

    /**
     * Edita os dados de uma seleção existente.
     * @throws SelecaoNaoEncontradaException se a seleção não for localizada.
     */
    public void editarSelecao(String pais, Selecao novosDados) throws SelecaoNaoEncontradaException {
        // TODO: atualizar atributos permitidos (grupo, técnico)
    }

    /**
     * Exclui uma seleção, se não houver jogadores vinculados.
     * @throws SelecaoNaoEncontradaException se a seleção não existir.
     * @throws IllegalStateException se a seleção possuir jogadores.
     */
    public void excluirSelecao(String pais) throws SelecaoNaoEncontradaException, IllegalStateException {
        // TODO: verificar lista de jogadores vazia
    }

    /**
     * Lista todas as seleções cadastradas.
     */
    public List<Selecao> listarSelecoes() {
        // TODO: retornar cópia da lista
        return new ArrayList<>();
    }

    /**
     * Busca seleções por grupo (opcional: todos).
     */
    public List<Selecao> buscarSelecoesPorGrupo(String grupo) {
        // TODO: filtrar por grupo (se "Todos", retornar todas)
        return new ArrayList<>();
    }

    /**
     * Busca seleções por nome do país (parcial).
     */
    public List<Selecao> buscarSelecoesPorPais(String texto) {
        // TODO: busca case-insensitive
        return new ArrayList<>();
    }

    /**
     * Busca seleções por nome do técnico (parcial).
     */
    public List<Selecao> buscarSelecoesPorTecnico(String texto) {
        // TODO: busca case-insensitive
        return new ArrayList<>();
    }

    // ========== OPERAÇÕES COM JOGADORES ==========

    /**
     * Cadastra um novo jogador, vinculando-o a uma seleção.
     * @throws ElencoCheioException se a seleção já tiver 26 jogadores.
     * @throws IllegalArgumentException se o número da camisa já existir na mesma seleção.
     */
    public void cadastrarJogador(Jogador jogador) throws ElencoCheioException, IllegalArgumentException {
        // TODO: validar elenco e número único
    }

    /**
     * Edita os dados de um jogador existente.
     * @throws JogadorNaoEncontradoException se o jogador não for encontrado.
     */
    public void editarJogador(String nome, Jogador novosDados) throws JogadorNaoEncontradoException {
        // TODO: localizar por nome e atualizar
    }

    /**
     * Exclui um jogador.
     * @throws JogadorNaoEncontradoException se o jogador não existir.
     */
    public void excluirJogador(String nome) throws JogadorNaoEncontradoException {
        // TODO: remover da lista da seleção
    }

    /**
     * Lista todos os jogadores de uma determinada seleção.
     */
    public List<Jogador> listarJogadoresPorSelecao(Selecao selecao) {
        // TODO: retornar lista de jogadores da seleção
        return new ArrayList<>();
    }

    /**
     * Lista todos os jogadores cadastrados.
     */
    public List<Jogador> listarTodosJogadores() {
        // TODO: retornar todos os jogadores
        return new ArrayList<>();
    }

    /**
     * Busca jogadores por nome (parcial).
     */
    public List<Jogador> buscarJogadoresPorNome(String texto) {
        // TODO: busca case-insensitive
        return new ArrayList<>();
    }

    /**
     * Busca jogadores por posição.
     */
    public List<Jogador> buscarJogadoresPorPosicao(String posicao) {
        // TODO: filtrar por posição (se "Todas", retornar todos)
        return new ArrayList<>();
    }

    /**
     * Busca jogadores por status (Ativo, Lesionado, Suspenso).
     */
    public List<Jogador> buscarJogadoresPorStatus(StatusJogador status) {
        // TODO: filtrar por status
        return new ArrayList<>();
    }

    /**
     * Busca jogadores pela seleção a que pertencem.
     */
    public List<Jogador> buscarJogadoresPorSelecao(Selecao selecao) {
        // TODO: mesmo que listarJogadoresPorSelecao, mas mantido para clareza da interface
        return new ArrayList<>();
    }

    // ========== VALIDAÇÕES AUXILIARES ==========

    /**
     * Verifica se uma seleção atingiu o número máximo de jogadores.
     */
    private boolean isElencoCheio(Selecao selecao) {
        // TODO: implementar
        return false;
    }

    /**
     * Verifica se o número da camisa já está em uso na seleção.
     */
    private boolean isNumeroDuplicado(Selecao selecao, int numero) {
        // TODO: implementar
        return false;
    }
}