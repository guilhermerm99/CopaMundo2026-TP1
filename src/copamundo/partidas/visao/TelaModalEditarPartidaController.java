package copamundo.partidas.visao;

import copamundo.comum.Fase;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;
import copamundo.comum.Estadio;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaModalEditarPartidaController {

    public String editarPartida(String id, String dataPartida, String horarioPartida, Estadio estadioPartida, Selecao selecao1, Selecao selecao2, Fase fase, StatusPartida status) {
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(id)) {
                listaPartidas.get(i).setDataPartida(dataPartida);
                listaPartidas.get(i).setHorarioPartida(horarioPartida);
                listaPartidas.get(i).setSelecao1(selecao1);
                listaPartidas.get(i).setSelecao2(selecao2);
                listaPartidas.get(i).setEstadioPartida(estadioPartida);
                listaPartidas.get(i).setFase(fase);
                listaPartidas.get(i).setStatusPartida(status);
                return "Partida atualizada com sucesso!\n";
            }

        }
        throw new PartidaNaoEncontradaException("Partida não encontrada");
    }

}
