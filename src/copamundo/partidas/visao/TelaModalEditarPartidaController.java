package copamundo.partidas.visao;

import copamundo.comum.*;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

//import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaModalEditarPartidaController {

    @FXML
    private ComboBox<StatusPartida> SeletorStatus;

    @FXML
    private Button btnCancelarEdicao;

    @FXML
    private Button btnEditarPartida;

    @FXML
    private ComboBox<Estadio> seletorEstadio;

    @FXML
    private ComboBox<Fase> seletorFase;

    @FXML
    private ComboBox<Selecao> seletorSelecao1;

    @FXML
    private ComboBox<Selecao> seletorSelecao2;

    @FXML
    private TextField textoData;

    @FXML
    private TextField textoHorario;

    @FXML
    void editarPartida(javafx.event.ActionEvent event) {

    }

    @FXML
    void fecharTelaEditarPartida(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    public String editarPartida(String id, String dataPartida, String horarioPartida, copamundo.estadios.modelo.Estadio estadioPartida, Selecao selecao1, Selecao selecao2,
                                Fase fase, StatusPartida status) throws IOException, ClassNotFoundException {
        List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(id)) {
                listaPartidas.get(i).setDataPartida(dataPartida);
                listaPartidas.get(i).setHorarioPartida(horarioPartida);
                listaPartidas.get(i).setSelecao1(selecao1);
                listaPartidas.get(i).setSelecao2(selecao2);
                listaPartidas.get(i).setEstadioPartida(estadioPartida);
                listaPartidas.get(i).setFase(fase);
                listaPartidas.get(i).setStatusPartida(status);
                PartidaRepositorio.salvarListaPartidas(listaPartidas);
                return "Partida atualizada com sucesso!\n";
            }

        }
        throw new PartidaNaoEncontradaException("Partida não encontrada");
    }

}
