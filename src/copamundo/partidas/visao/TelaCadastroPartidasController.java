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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.List;
import java.io.IOException;
import java.util.Objects;


public class TelaCadastroPartidasController {

    private final EstadioController estadioController = new EstadioController();

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
            mostrarMensagemErro("Erro ao acessar uma das listas!");
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


            // carrega a lista de partidas
            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            // checa se as seleções são diferentes -> se uma das seleções não possui partida na mesma data
            if (selecao1 != selecao2) {
                for (Partida p : listaPartidas) {
                    if ((p.getSelecao1() == selecao1) || (p.getSelecao1() == selecao2) || (p.getSelecao2() == selecao1) || (p.getSelecao2() == selecao2)) {
                        if (p.getDataPartida().equals(data)) {
                            mostrarMensagemErro("Uma seleção já possui partida nesta data!");
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

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();

    }


}
