package copamundo.partidas.visao;

import copamundo.comum.Resultado;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaRegistroResultadosController {

    public RegistrarResultado(String idPartida, int golsSelecao1, int golsSelecao2, int faltasSelecao1, int faltasSelecao2, int vermelhosSelecao1,
                              int vermelhosSelecao2, int amarelosSelecao1, int amarelosSelecao2, float posseSelecao1, float posseSelecao2,
                              int finalizacoesSelecao1, int finalizacoesSelecao2, int escanteiosSelecao1, int escanteiosSelecao2, int impedimentosSelecao1,
                              int impedimentosSelecao2) {

        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(idPartida)) {
                listaPartidas.get(i).setResultado(new Resultado(golsSelecao1, golsSelecao2, faltasSelecao1, faltasSelecao2, vermelhosSelecao1,
                vermelhosSelecao2, amarelosSelecao1, amarelosSelecao2, posseSelecao1, posseSelecao2, finalizacoesSelecao1, finalizacoesSelecao2,
                escanteiosSelecao1, escanteiosSelecao2, impedimentosSelecao1, impedimentosSelecao2));
            }

        }
    }
}
