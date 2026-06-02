package copamundo.partidas.visao;

import java.time.LocalDateTime;
import java.util.ArrayList;

import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;
import copamundo.estadios.modelo.Estadio;
import copamundo.partidas.repositorio.PartidaRepositorio;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaCadastroPartidasController {

    public CadastroPartida() {
        listaPartidas = new ArrayList();
    }

    public SalvarPartida(String dataPartida, String horarioPartida, Estadio estadioPartida, Selecao selecao1, Selecao selecao2, Fase fase, StatusPartida status) {
        Partida partida = new Partida(dataPartida, horarioPartida, estadioPartida, selecao1, selecao2, fase, status);
        listaPartidas.add(partida);
    }


    public String ExcluirPartida(String id) {
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(id)) {
                listaPartidas.remove(i);
                return "Partida removida com sucesso!\n";
            }
        }
        return "Partida não encontrada.\n";
    }


}
