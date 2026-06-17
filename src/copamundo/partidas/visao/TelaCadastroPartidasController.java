package copamundo.partidas.visao;

import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Selecao;
import copamundo.comum.StatusPartida;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.partidas.excecoes.PartidaMesmaDataException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import copamundo.selecoes.persistencia.PersistenciaSelecoesJogadores;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.io.IOException;
import java.util.Objects;


public class TelaCadastroPartidasController {

    private final EstadioController estadioController = new EstadioController();

    @FXML
    private Button btnTelaPrincipal;

    @FXML
    private Button btnSalvarCadastroPartida;

    @FXML
    private Button btnTelaConsultaPartidas;

    @FXML
    private Button btnTelaRegistroResultado;

    @FXML
    private ComboBox<copamundo.estadios.modelo.Estadio> seletorEstadio;

    @FXML
    private ComboBox<Selecao> seletorSelecao1;

    @FXML
    private ComboBox<Selecao> seletorSelecao2;

    @FXML
    private TextField textoData;

    @FXML
    private TextField textoHorario;

    @FXML
    private ComboBox<Fase> seletorFase;

    @FXML
    private ComboBox<StatusPartida> seletorStatusPartida;

    // preenche os seletores com o conteúdo das listas
    public void initialize() {

        try {
            PersistenciaSelecoesJogadores selecoesObjeto = new PersistenciaSelecoesJogadores();
            EstadioController estadioObjeto = new EstadioController();

            List<Selecao> listaSelecoes = selecoesObjeto.carregarSelecoes();
            List<copamundo.estadios.modelo.Estadio> listaEstadios = estadioObjeto.listarEstadios();

            seletorFase.getItems().addAll(Fase.values());
            seletorStatusPartida.getItems().addAll(StatusPartida.values());
            seletorSelecao1.getItems().addAll(listaSelecoes);
            seletorSelecao2.getItems().addAll(listaSelecoes);
            seletorEstadio.getItems().addAll(listaEstadios);

        } catch (PersistenciaException | copamundo.selecoes.excecoes.PersistenciaException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Erro ao acessar uma das listas!");
            alert.showAndWait();
        }

    }


    @FXML
    void salvarPartida(javafx.event.ActionEvent event) {

        try {
            // pega os valores informados
            Fase fase = seletorFase.getValue();
            StatusPartida status = seletorStatusPartida.getValue();
            Selecao selecao1 = seletorSelecao1.getValue();
            Selecao selecao2 = seletorSelecao2.getValue();
            copamundo.estadios.modelo.Estadio estadio = seletorEstadio.getValue();
            String data = textoData.getText();
            String horario = textoHorario.getText();

            // avisos caso haja uma seção não preenchida - avisa e encerra o metodo antes de salvar
            if (Objects.equals(data, "")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Informe uma data!");
                alert.showAndWait();
                return;
            }
            if (Objects.equals(horario, "")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Informe um horário!");
                alert.showAndWait();
                return;
            }
            if (estadio == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Selecione um estádio!");
                alert.showAndWait();
                return;
            }
            if ((selecao1 == null) || (selecao2 == null)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Selecione as seleções!");
                alert.showAndWait();
                return;
            }
            if (fase == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Selecione a fase!");
                alert.showAndWait();
                return;
            }
            if (status == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Selecione o status da partida!");
                alert.showAndWait();
                return;
            }


            // carrega a lista de partidas
            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            // checa se as seleções são diferentes -> se uma das seleções não possui partida na mesma data
            if (selecao1 != selecao2) {
                for (Partida p : listaPartidas) {
                    if ((p.getSelecao1() == selecao1) || (p.getSelecao1() == selecao2) || (p.getSelecao2() == selecao1) || (p.getSelecao2() == selecao2)) {
                        if (p.getDataPartida().equals(data)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Uma seleção já possui partida nesta data!");
                            alert.showAndWait();
                            throw new PartidaMesmaDataException("Uma seleção já possui partida nesta data.");
                        }
                    }
                }
                // se tudo certo, cria uma partida e salva
                Partida partida = new Partida(data, horario, estadio, selecao1, selecao2, fase, status);

                estadioController.validarEstadioDisponivel(partida);

                listaPartidas.add(partida);

                // salva a lista novamente no repositório
                PartidaRepositorio.salvarListaPartidas(listaPartidas);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("A partida foi salva com sucesso!");
                alert.showAndWait();
                return;
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("As seleções devem ser diferentes!");
                alert.showAndWait();
                return;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RegraNegocioException | PersistenciaException e) {
            throw new RuntimeException(e);
        }
    }


}
