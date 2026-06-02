package copamundo.partidas.visao;
import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;

import java.util.ArrayList;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaConsultaPartidasController {


    public ListarPartidas() {
        return listaPartidas;
    }

    public ListaPorSelecao (Selecao selecao){
        ArrayList<Partida> listaFiltradaPartidas;
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getSelecao1().equals(selecao) || listaPartidas.get(i).getSelecao2().equals(selecao)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public ListaPorData (String data){
        ArrayList<Partida> listaFiltradaPartidas;
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getDataPartida().equals(data)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }
        }
        return listaFiltradaPartidas;
    }

    public ListaPorFase (Fase fase){
        ArrayList<Partida> listaFiltradaPartidas;
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getFase().equals(fase)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }

        }
    }

    public ListaPorStatusPartida (StatusPartida status){
        ArrayList<Partida> listaFiltradaPartidas;
        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getStatusPartida().equals(status)) {
                listaFiltradaPartidas.add(listaPartidas.get(i));
            }

        }
    }
}
