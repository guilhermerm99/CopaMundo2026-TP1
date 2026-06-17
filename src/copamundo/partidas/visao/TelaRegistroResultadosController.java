package copamundo.partidas.visao;

import copamundo.comum.Fase;
import copamundo.comum.Partida;
import copamundo.comum.Resultado;
import copamundo.comum.StatusPartida;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;
import copamundo.partidas.excecoes.PartidaNaoFinalizadaException;
import copamundo.partidas.repositorio.PartidaRepositorio;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.io.IOException;
import java.util.List;


public class TelaRegistroResultadosController {

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
        // determina o formato da String que irá aparecer: "seleção 1 x seleção 2"
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
    void salvarResultado(javafx.event.ActionEvent event) {
        try {
            Partida p = seletorPartida.getValue();
            String amarelos1 = textoAmarelos1.getText();
            String amarelos2 = textoAmarelos2.getText();
            String escanteios1 = textoEscanteios1.getText();
            String escanteios2 = textoEscanteios2.getText();
            String faltas1 = textoFaltas1.getText();
            String faltas2 = textoFaltas2.getText();
            String finalizacoes1 = textoFinalizacoes1.getText();
            String finalizacoes2 = textoFinalizacoes2.getText();
            String gols1 = textoGols1.getText();
            String gols2 = textoGols2.getText();
            String impedimentos1 = textoImpedimentos1.getText();
            String impedimentos2 = textoImpedimentos2.getText();
            String posse1 = textoPosse1.getText();
            String posse2 = textoPosse2.getText();
            String vermelhos1 = textoVermelhos1.getText();
            String vermelhos2 = textoVermelhos2.getText();

            // é obrigatório selecionar uma partida para salvar um resultado
            if (p == null) {
                mostrarMensagemErro("Selecione uma partida!");
                return;
            }

            if (amarelos1 == null || amarelos1.trim().isEmpty() || amarelos2 == null || amarelos2.trim().isEmpty()
                    || escanteios1 == null || escanteios1.trim().isEmpty() || escanteios2 == null || escanteios2.trim().isEmpty()
                    || faltas1 == null || faltas1.trim().isEmpty() || faltas2 == null || faltas2.trim().isEmpty()
                    || finalizacoes1 == null || finalizacoes1.trim().isEmpty() || finalizacoes2 == null || finalizacoes2.trim().isEmpty()
                    || gols1 == null || gols1.trim().isEmpty() || gols2 == null || gols2.trim().isEmpty()
                    || impedimentos1 == null || impedimentos1.trim().isEmpty() || impedimentos2 == null || impedimentos2.trim().isEmpty()
                    || posse1 == null || posse1.trim().isEmpty() || posse2 == null || posse2.trim().isEmpty()
                    || vermelhos1 == null || vermelhos1.trim().isEmpty() || vermelhos2 == null || vermelhos2.trim().isEmpty()) {


                mostrarMensagemErro("Todos os campos devem estar preenchidos!");
                return;
            }

            List<Partida> listaPartidas = PartidaRepositorio.carregarListaPartidas();

            int amarelosSelecao1 = Integer.parseInt(amarelos1.trim());
            int amarelosSelecao2 = Integer.parseInt(amarelos2.trim());
            int escanteiosSelecao1 = Integer.parseInt(escanteios1.trim());
            int escanteiosSelecao2 = Integer.parseInt(escanteios2.trim());
            int faltasSelecao1 = Integer.parseInt(faltas1.trim());
            int faltasSelecao2 = Integer.parseInt(faltas2.trim());
            int finalizacoesSelecao1 = Integer.parseInt(finalizacoes1.trim());
            int finalizacoesSelecao2 = Integer.parseInt(finalizacoes2.trim());
            int golsSelecao1 = Integer.parseInt(gols1.trim());
            int golsSelecao2 = Integer.parseInt(gols2.trim());
            int impedimentosSelecao1 = Integer.parseInt(impedimentos1.trim());
            int impedimentosSelecao2 = Integer.parseInt(impedimentos2.trim());
            float posseSelecao1 = Float.parseFloat(posse1.trim());
            float posseSelecao2 = Float.parseFloat(posse2.trim());
            int vermelhosSelecao1 = Integer.parseInt(vermelhos1.trim());
            int vermelhosSelecao2 = Integer.parseInt(vermelhos2.trim());

            // verifica se a partida escolhida está finalizada antes de salvar
            for (int i = 0; i < listaPartidas.size(); i++) {
                if (listaPartidas.get(i).getId().equals(p.getId())) {
                    if (listaPartidas.get(i).getStatusPartida() == StatusPartida.FINALIZADA) {

                        // se está finalizada, salva o resultado e salva a lista nova
                        listaPartidas.get(i).setResultado(new Resultado(golsSelecao1, golsSelecao2, faltasSelecao1, faltasSelecao2, vermelhosSelecao1,
                                vermelhosSelecao2, amarelosSelecao1, amarelosSelecao2, posseSelecao1, posseSelecao2, finalizacoesSelecao1,
                                finalizacoesSelecao2, escanteiosSelecao1, escanteiosSelecao2, impedimentosSelecao1, impedimentosSelecao2));

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


}
