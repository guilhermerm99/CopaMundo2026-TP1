package copamundo.partidas.visao;

import copamundo.comum.Resultado;
import copamundo.comum.StatusPartida;
import copamundo.partidas.excecoes.PartidaNaoEncontradaException;
import copamundo.partidas.excecoes.PartidaNaoFinalizadaException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.awt.*;
import java.io.IOException;

import static copamundo.partidas.repositorio.PartidaRepositorio.listaPartidas;

public class TelaRegistroResultadosController {
    // AJUSTAR METODOS

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
    private ComboBox<?> seletorFase;

    @FXML
    private ComboBox<?> seletorPartida;

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

    @FXML
    void irTelaCadastroPartidas(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void irTelaConsultaPartidas(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void irTelaMenuInicial(javafx.event.ActionEvent event) throws IOException {

    }

    @FXML
    void salvarResultado(javafx.event.ActionEvent event) {

    }

    public String registrarResultado(String idPartida, int golsSelecao1, int golsSelecao2, int faltasSelecao1, int faltasSelecao2, int vermelhosSelecao1,
                                     int vermelhosSelecao2, int amarelosSelecao1, int amarelosSelecao2, float posseSelecao1, float posseSelecao2,
                                     int finalizacoesSelecao1, int finalizacoesSelecao2, int escanteiosSelecao1, int escanteiosSelecao2, int impedimentosSelecao1,
                                     int impedimentosSelecao2) {

        for (int i = 0; i < listaPartidas.size(); i++) {
            if (listaPartidas.get(i).getId().equals(idPartida)) {
                if (listaPartidas.get(i).getStatusPartida() == StatusPartida.FINALIZADA) {
                    listaPartidas.get(i).setResultado(new Resultado(golsSelecao1, golsSelecao2, faltasSelecao1, faltasSelecao2, vermelhosSelecao1,
                            vermelhosSelecao2, amarelosSelecao1, amarelosSelecao2, posseSelecao1, posseSelecao2, finalizacoesSelecao1, finalizacoesSelecao2,
                            escanteiosSelecao1, escanteiosSelecao2, impedimentosSelecao1, impedimentosSelecao2));

                    return "Resultado registrado com sucesso!";
                }
                throw new PartidaNaoFinalizadaException("A partida deve estar finalizada para registrar um resultado!");
            }
        }

        throw new PartidaNaoEncontradaException("Partida não encontrada");
    }
}
