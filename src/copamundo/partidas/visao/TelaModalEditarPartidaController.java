package copamundo.partidas.visao;

import copamundo.comum.*;
import copamundo.estadios.controle.EstadioController;
import copamundo.estadios.excecoes.PersistenciaException;
import copamundo.estadios.excecoes.RegraNegocioException;
import copamundo.partidas.excecoes.PartidaMesmaDataException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import copamundo.selecoes.persistencia.PersistenciaSelecoesJogadores;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class TelaModalEditarPartidaController {

    private Partida partida;

    @FXML
    private ComboBox<StatusPartida> seletorStatus;

    @FXML
    private ComboBox<copamundo.estadios.modelo.Estadio> seletorEstadio;

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

    // preenche os seletores
    public void initialize() {
        try {
            PersistenciaSelecoesJogadores selecoesObjeto = new PersistenciaSelecoesJogadores();
            EstadioController estadioObjeto = new EstadioController();

            List<Selecao> listaSelecoes = selecoesObjeto.carregarSelecoes();
            List<copamundo.estadios.modelo.Estadio> listaEstadios = estadioObjeto.listarEstadios();

            seletorFase.getItems().addAll(Fase.values());
            seletorStatus.getItems().addAll(StatusPartida.values());
            seletorSelecao1.getItems().addAll(listaSelecoes);
            seletorSelecao2.getItems().addAll(listaSelecoes);
            seletorEstadio.getItems().addAll(listaEstadios);

        } catch (PersistenciaException | copamundo.selecoes.excecoes.PersistenciaException e) {
            e.printStackTrace();
            System.out.println("Erro ao acessar lista.");
        }

    }

    // o controller da tela de consulta manda a partida a ser editada por parâmetro
    public void setPartida(Partida partida) {
        this.partida = partida;

        // define o que mostrar nos campos - no caso, os valores atuais do objeto
        textoData.setText(partida.getDataPartida());
        textoHorario.setText(partida.getHorarioPartida());
        seletorStatus.setValue(partida.getStatus());
        seletorEstadio.setValue(partida.getEstadioPartida());
        seletorFase.setValue(partida.getFasePartida());
        seletorSelecao1.setValue(partida.getSelecao1());
        seletorSelecao2.setValue(partida.getSelecao2());

    }

    @FXML
    void editarPartida(javafx.event.ActionEvent event) {
        try {
            Fase fase = seletorFase.getValue();
            StatusPartida status = seletorStatus.getValue();
            Selecao selecao1 = seletorSelecao1.getValue();
            Selecao selecao2 = seletorSelecao2.getValue();
            copamundo.estadios.modelo.Estadio estadio = seletorEstadio.getValue();
            String data = textoData.getText();
            String horario = textoHorario.getText();

            // verifica se há um campo em branco
            if (Objects.equals(data, "")) {
                mostrarMensagemErro("Informe uma data!");
                return;
            }
            if (Objects.equals(horario, "")) {
                mostrarMensagemErro("Informe um horário!");
                return;
            }
            if (estadio == null) {
                mostrarMensagemErro("Selecione um estádio!");
                return;
            }
            if ((selecao1 == null) || (selecao2 == null)) {
                mostrarMensagemErro("Selecione as seleções!");
                return;
            }
            if (fase == null) {
                mostrarMensagemErro("Selecione a fase!");
                return;
            }
            if (status == null) {
                mostrarMensagemErro("Selecione o status da partida!");
                return;
            }

            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            // verifica se há impedimentos para essa partida
            if (selecao1 != selecao2) {
                for (Partida p : listaPartidas) {
                    if (((p.getSelecao1() == selecao1) || (p.getSelecao1() == selecao2) || (p.getSelecao2() == selecao1) || (p.getSelecao2() == selecao2)) && (!Objects.equals(p.getId(), partida.getId()))) {
                        if (p.getDataPartida().equals(data)) {

                            mostrarMensagemErro("Uma seleção já possui partida nesta data!");

                            throw new PartidaMesmaDataException("Uma seleção já possui partida nesta data.");
                        }
                    }
                }

                // se tudo certo, altera os valores da partida e salva a lista editada no repositório
                estadioController.validarEstadioDisponivel(partida);

                for (int i = 0; i < listaPartidas.size(); i++) {
                    if (listaPartidas.get(i).getId().equals(partida.getId())) {
                        Partida partidaEditada = listaPartidas.get(i);

                        partidaEditada.setStatusPartida(status);
                        partidaEditada.setFase(fase);
                        partidaEditada.setSelecao1(selecao1);
                        partidaEditada.setSelecao2(selecao2);
                        partidaEditada.setEstadioPartida(estadio);
                        partidaEditada.setDataPartida(data);
                        partidaEditada.setHorarioPartida(horario);

                        PartidaRepositorio.salvarListaPartidas(listaPartidas);
                    }
                }


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Partida editada com sucesso!");
                alert.showAndWait();

            }
            else {
                mostrarMensagemErro("As seleções devem ser diferentes!");

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RegraNegocioException | PersistenciaException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void fecharTelaEditarPartida(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    private final EstadioController estadioController = new EstadioController();

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();

    }


}
