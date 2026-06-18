package copamundo.selecoes.util;

import copamundo.comum.Jogador;
import copamundo.comum.Selecao;
import copamundo.comum.StatusJogador;
import copamundo.selecoes.controle.GestaoSelecoesJogadores;

public class PopularDadosCopa2026 {

    private final GestaoSelecoesJogadores gestao = new GestaoSelecoesJogadores();

    public void popular() {
        System.out.println("=== Populando dados da Copa do Mundo 2026 ===\n");
        cadastrarSelecoes();
        cadastrarJogadoresBrasil();
        cadastrarJogadoresArgentina();
        cadastrarJogadoresFranca();
        cadastrarJogadoresEspanha();
        System.out.println("\n=== Concluido! ===");
    }

    private void cadastrarSelecoes() {
        System.out.println(">> Cadastrando selecoes...");
        Object[][] selecoes = {
            {"Brasil",    "C", "Carlo Ancelotti"},
            {"Argentina", "J", "Lionel Scaloni"},
            {"Franca",    "I", "Didier Deschamps"},
            {"Espanha",   "H", "Luis de la Fuente"},
            {"Alemanha",  "E", "Julian Nagelsmann"},
            {"Inglaterra","L", "Thomas Tuchel"},
            {"Portugal",  "K", "Roberto Martinez"},
            {"Marrocos",  "C", "Walid Regragui"},
            {"Mexico",    "A", "Javier Aguirre"},
            {"Holanda",   "F", "Ronald Koeman"},
            {"Belgica",   "G", "Domenico Tedesco"},
            {"Uruguai",   "H", "Marcelo Bielsa"},
        };

        for (Object[] s : selecoes) {
            try {
                gestao.cadastrarSelecao(new Selecao((String)s[0], (String)s[1], (String)s[2]));
                System.out.println("   OK " + s[0]);
            } catch (Exception e) {
                System.out.println("   ERRO " + s[0] + " - " + e.getMessage());
            }
        }
    }

    private void cadastrarJogadoresBrasil() {
        System.out.println("\n>> Cadastrando jogadores do Brasil...");
        Object[][] jogadores = {
            {"Alisson",           "Goleiro",     1, 32, StatusJogador.ATIVO},
            {"Ederson",           "Goleiro",    12, 31, StatusJogador.ATIVO},
            {"Weverton",          "Goleiro",    23, 37, StatusJogador.ATIVO},
            {"Danilo",            "Lateral",     2, 33, StatusJogador.ATIVO},
            {"Marquinhos",        "Zagueiro",    4, 31, StatusJogador.ATIVO},
            {"Gabriel Magalhaes", "Zagueiro",    3, 27, StatusJogador.ATIVO},
            {"Guilherme Arana",   "Lateral",     6, 27, StatusJogador.ATIVO},
            {"Alex Telles",       "Lateral",    13, 31, StatusJogador.ATIVO},
            {"Casemiro",          "Meio-campo",  5, 33, StatusJogador.ATIVO},
            {"Lucas Paqueta",     "Meio-campo", 10, 27, StatusJogador.ATIVO},
            {"Bruno Guimaraes",   "Meio-campo",  8, 27, StatusJogador.ATIVO},
            {"Vinicius Jr.",      "Atacante",    7, 24, StatusJogador.ATIVO},
            {"Raphinha",          "Atacante",   11, 28, StatusJogador.ATIVO},
            {"Neymar",            "Atacante",   17, 34, StatusJogador.ATIVO},
            {"Endrick",           "Atacante",    9, 18, StatusJogador.ATIVO},
            {"Rayan",             "Atacante",   21, 18, StatusJogador.ATIVO},
            {"Igor Thiago",       "Atacante",   19, 23, StatusJogador.ATIVO},
            {"Rodrygo",           "Atacante",   22, 24, StatusJogador.LESIONADO},
            {"Estevao",           "Atacante",   20, 18, StatusJogador.LESIONADO},
        };
        cadastrarLote(jogadores, "Brasil");
    }

    private void cadastrarJogadoresArgentina() {
        System.out.println("\n>> Cadastrando jogadores da Argentina...");
        Object[][] jogadores = {
            {"Emiliano Martinez", "Goleiro",     1, 32, StatusJogador.ATIVO},
            {"Nahuel Molina",     "Lateral",    26, 26, StatusJogador.ATIVO},
            {"Cristian Romero",   "Zagueiro",   13, 26, StatusJogador.ATIVO},
            {"Lisandro Martinez", "Zagueiro",   14, 26, StatusJogador.ATIVO},
            {"Nicolas Tagliafico","Lateral",     3, 32, StatusJogador.ATIVO},
            {"Rodrigo De Paul",   "Meio-campo",  7, 30, StatusJogador.ATIVO},
            {"Enzo Fernandez",    "Meio-campo", 24, 24, StatusJogador.ATIVO},
            {"Alexis Mac Allister","Meio-campo",10, 26, StatusJogador.ATIVO},
            {"Lionel Messi",      "Atacante",   19, 38, StatusJogador.ATIVO},
            {"Lautaro Martinez",  "Atacante",   22, 27, StatusJogador.ATIVO},
            {"Julian Alvarez",    "Atacante",    9, 24, StatusJogador.ATIVO},
            {"Angel Di Maria",    "Atacante",   11, 36, StatusJogador.ATIVO},
        };
        cadastrarLote(jogadores, "Argentina");
    }

    private void cadastrarJogadoresFranca() {
        System.out.println("\n>> Cadastrando jogadores da Franca...");
        Object[][] jogadores = {
            {"Mike Maignan",       "Goleiro",    16, 29, StatusJogador.ATIVO},
            {"Jules Kounde",       "Lateral",     5, 26, StatusJogador.ATIVO},
            {"Dayot Upamecano",    "Zagueiro",    4, 26, StatusJogador.ATIVO},
            {"Ibrahima Konate",    "Zagueiro",   13, 25, StatusJogador.ATIVO},
            {"Theo Hernandez",     "Lateral",    22, 27, StatusJogador.ATIVO},
            {"Aurelien Tchouameni","Meio-campo",  8, 24, StatusJogador.ATIVO},
            {"Adrien Rabiot",      "Meio-campo", 14, 29, StatusJogador.ATIVO},
            {"Antoine Griezmann",  "Meio-campo",  7, 33, StatusJogador.ATIVO},
            {"Kylian Mbappe",      "Atacante",   10, 26, StatusJogador.ATIVO},
            {"Ousmane Dembele",    "Atacante",   11, 27, StatusJogador.ATIVO},
            {"Marcus Thuram",      "Atacante",    9, 27, StatusJogador.ATIVO},
            {"Bradley Barcola",    "Atacante",   17, 22, StatusJogador.ATIVO},
        };
        cadastrarLote(jogadores, "Franca");
    }

    private void cadastrarJogadoresEspanha() {
        System.out.println("\n>> Cadastrando jogadores da Espanha...");
        Object[][] jogadores = {
            {"Unai Simon",       "Goleiro",     1, 27, StatusJogador.ATIVO},
            {"Dani Carvajal",    "Lateral",     2, 32, StatusJogador.ATIVO},
            {"Aymeric Laporte",  "Zagueiro",   14, 30, StatusJogador.ATIVO},
            {"Robin Le Normand", "Zagueiro",   24, 27, StatusJogador.ATIVO},
            {"Marc Cucurella",   "Lateral",     3, 26, StatusJogador.ATIVO},
            {"Rodri",            "Meio-campo", 16, 28, StatusJogador.ATIVO},
            {"Pedri",            "Meio-campo", 26, 22, StatusJogador.ATIVO},
            {"Fabian Ruiz",      "Meio-campo",  8, 28, StatusJogador.ATIVO},
            {"Lamine Yamal",     "Atacante",   19, 17, StatusJogador.ATIVO},
            {"Nico Williams",    "Atacante",   11, 22, StatusJogador.ATIVO},
            {"Alvaro Morata",    "Atacante",    7, 31, StatusJogador.SUSPENSO},
            {"Mikel Oyarzabal",  "Atacante",   21, 27, StatusJogador.ATIVO},
        };
        cadastrarLote(jogadores, "Espanha");
    }

    private void cadastrarLote(Object[][] jogadores, String pais) {
        for (Object[] j : jogadores) {
            try {
                Jogador jogador = new Jogador();
                jogador.setNome((String) j[0]);
                jogador.setPosicao((String) j[1]);
                jogador.setNumero((int) j[2]);
                jogador.setIdade((int) j[3]);
                jogador.setStatus((StatusJogador) j[4]);
                jogador.setPaisSelecao(pais);
                gestao.cadastrarJogador(jogador);
                System.out.println("   OK " + j[0]);
            } catch (Exception e) {
                System.out.println("   ERRO " + j[0] + " - " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new PopularDadosCopa2026().popular();
    }
}
