package copamundo.partidas.visao;

import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Resultado;
import copamundo.comum.StatusPartida;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;
import copamundo.partidas.excecoes.PartidaNaoFinalizadaException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TelaRegistroResultadosController {

    @FXML
    private Button btnCancelarRegistro;

    @FXML
    private Button btnSalvarResultado;

    @FXML
    private Button btnTelaCadastroPartidas;

    @FXML
    private Button btnTelaConsultaPartidas;

    @FXML
    private Label labelSelecao1;

    @FXML
    private Label labelSelecao2;

    @FXML
    private ComboBox<Fase> seletorFase;

    @FXML
    private ComboBox<Partida> seletorPartida;

    @FXML
    private TextField textoAmarelos1;

    @FXML
    private TextField textoAmarelos2;

    @FXML
    private TextField textoEscanteios1;

    @FXML
    private TextField textoEscanteios2;

    @FXML
    private TextField textoFaltas1;

    @FXML
    private TextField textoFaltas2;

    @FXML
    private TextField textoFinalizacoes1;

    @FXML
    private TextField textoFinalizacoes2;

    @FXML
    private TextField textoGols1;

    @FXML
    private TextField textoGols2;

    @FXML
    private TextField textoImpedimentos1;

    @FXML
    private TextField textoImpedimentos2;

    @FXML
    private TextField textoPosse1;

    @FXML
    private TextField textoPosse2;

    @FXML
    private TextField textoVermelhos1;

    @FXML
    private TextField textoVermelhos2;

    // preenche os seletores com as fases e partidas
    public void initialize() {
        // determina o formato da String que irá aparecer: "seleção 1 x seleção 2" - já tinha uma toString na classe e tive que fazer assim
        seletorPartida.setConverter(new StringConverter<Partida>() {
            @Override
            public String toString(Partida p) {
                return p.nomeSelecoes();
            }

            @Override
            public Partida fromString(String s) {
                return null;
            }
        });

        seletorFase.getItems().addAll(Fase.values());
        seletorFase.setOnAction(event -> atualizarPartidasSeletor());


        // muda o texto das labels para os das seleções
        seletorPartida.setOnAction(event -> {
            labelSelecao1.setText(seletorPartida.getValue().getSelecao1().getPais());
            labelSelecao2.setText(seletorPartida.getValue().getSelecao2().getPais());
        });

    }

    // atualiza o que aparece na comboBox de partidas, a partir da seleção da fase
    private void atualizarPartidasSeletor() {
        try {
            Fase f = seletorFase.getValue();

            if (f == null) {
                seletorPartida.getItems().clear();
                return;
            }
            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();
            List<Partida> partidasPorFase = TelaConsultaPartidasController.listaPorFase(f, listaPartidas);

            seletorPartida.getItems().clear();
            seletorPartida.getItems().addAll(partidasPorFase);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irTelaCadastroPartidas(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("TelaCadastroPartidas.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irTelaConsultaPartidas(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("TelaConsultaPartidas.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irTelaMenuInicial(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/copamundo/principal/visao/TelaPrincipal.fxml"));

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void salvarResultado(javafx.event.ActionEvent event) {
        try {
            Partida p = seletorPartida.getValue();
            int amarelosSelecao1 = Integer.parseInt(textoAmarelos1.getText());
            int amarelosSelecao2 = Integer.parseInt(textoAmarelos2.getText());
            int escanteiosSelecao1 = Integer.parseInt(textoEscanteios1.getText());
            int escanteiosSelecao2 = Integer.parseInt(textoEscanteios2.getText());
            int faltasSelecao1 = Integer.parseInt(textoFaltas1.getText());
            int faltasSelecao2 = Integer.parseInt(textoFaltas2.getText());
            int finalizacoesSelecao1 = Integer.parseInt(textoFinalizacoes1.getText());
            int finalizacoesSelecao2 = Integer.parseInt(textoFinalizacoes2.getText());
            int golsSelecao1 = Integer.parseInt(textoGols1.getText());
            int golsSelecao2 = Integer.parseInt(textoGols2.getText());
            int impedimentosSelecao1 = Integer.parseInt(textoImpedimentos1.getText());
            int impedimentosSelecao2 = Integer.parseInt(textoImpedimentos2.getText());
            float posseSelecao1 = Float.parseFloat(textoPosse1.getText());
            float posseSelecao2 = Float.parseFloat(textoPosse2.getText());
            int vermelhosSelecao1 = Integer.parseInt(textoVermelhos1.getText());
            int vermelhosSelecao2 = Integer.parseInt(textoVermelhos2.getText());

            // é obrigatório selecionar uma partida para salvar um resultado
            if (p == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Selecione uma partida!");
                alert.showAndWait();
                return;
            }

            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            // verifica se a partida escolhida está finalizada antes de salvar
            for (int i = 0; i < listaPartidas.size(); i++) {
                if (listaPartidas.get(i).getId().equals(p.getId())) {
                    if (listaPartidas.get(i).getStatusPartida() == StatusPartida.FINALIZADA) {

                        // se está finalizada, salva o resultado e salva a lista nova
                        listaPartidas.get(i).setResultado(new Resultado(golsSelecao1, golsSelecao2, faltasSelecao1, faltasSelecao2, vermelhosSelecao1,
                                vermelhosSelecao2, amarelosSelecao1, amarelosSelecao2, posseSelecao1, posseSelecao2, finalizacoesSelecao1, finalizacoesSelecao2,
                                escanteiosSelecao1, escanteiosSelecao2, impedimentosSelecao1, impedimentosSelecao2));

                        PartidaRepositorio.salvarListaPartidas(listaPartidas);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sucesso");
                        alert.setHeaderText(null);
                        alert.setContentText("Resultado registrado com sucesso!");
                        alert.showAndWait();
                        return;
                    }
                    mostrarMensagemErro("A partida deve estar finalizada para registrar um resultado!");
                    throw new PartidaNaoFinalizadaException("A partida deve estar finalizada para registrar um resultado!");
                }
            }
            mostrarMensagemErro("Partida não encontrada");
            throw new PartidaNaoEncontradaException("Partida não encontrada");

        } catch (ClassNotFoundException e) {
            mostrarMensagemErro("Classe não encontrada");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    ////////////////////////////////////////////////////////////////
    ///
    /*
    public String registrarResultado(String idPartida, int golsSelecao1, int golsSelecao2, int faltasSelecao1, int faltasSelecao2, int vermelhosSelecao1,
                                     int vermelhosSelecao2, int amarelosSelecao1, int amarelosSelecao2, float posseSelecao1, float posseSelecao2,
                                     int finalizacoesSelecao1, int finalizacoesSelecao2, int escanteiosSelecao1, int escanteiosSelecao2, int impedimentosSelecao1,
                                     int impedimentosSelecao2) throws IOException, ClassNotFoundException {

        List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(idPartida)) {
                if (listaPartidas.get(i).getStatusPartida() == StatusPartida.FINALIZADA) {
                    listaPartidas.get(i).setResultado(new Resultado(golsSelecao1, golsSelecao2, faltasSelecao1, faltasSelecao2, vermelhosSelecao1,
                            vermelhosSelecao2, amarelosSelecao1, amarelosSelecao2, posseSelecao1, posseSelecao2, finalizacoesSelecao1, finalizacoesSelecao2,
                            escanteiosSelecao1, escanteiosSelecao2, impedimentosSelecao1, impedimentosSelecao2));

                    PartidaRepositorio.salvarListaPartidas(listaPartidas);
                    return "Resultado registrado com sucesso!";
                }
                throw new PartidaNaoFinalizadaException("A partida deve estar finalizada para registrar um resultado!");
            }
        }

        throw new PartidaNaoEncontradaException("Partida não encontrada");
    }

     */
}
